package com.team.dev.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.dev.config.JwtService;
import com.team.dev.dto.*;
import com.team.dev.event.AccountEvent;
import com.team.dev.exception.ApiException;
import com.team.dev.exception.ResourceNotFoundException;
import com.team.dev.mapper.Mapper;
import com.team.dev.model.*;
import com.team.dev.repository.*;
import com.team.dev.service.AuthService;
import com.team.dev.service.EntrepriseService;
import com.team.dev.service.MailService;
import com.team.dev.utils.ExtendedUser;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.Instant;
import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class AccountServiceImpl implements AuthService {

    private final UserRepository repository;
    private final ForgotPasswordRepo forgotPasswordRepo;
    private final EntrepriseRepository entrepriseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final EntrepriseService entrepriseService;
    private final AuthenticationManager authenticationManager;
    private  final ApplicationEventPublisher publisher;
    private  final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private  final JavaMailSender mailSender;
    private final TokenPwdRepository pwTokenRepository;

    @Override
    public AuthenticateResponse register(RegisterRequest request, final HttpServletRequest servletRequest){

        Role roleUsers = new Role("ADMIN");
        if (roleRepository.findByRoleName("ADMIN").isPresent()) {
            roleUsers.setRoleName(roleUsers.getRoleName());
        }else {
            roleRepository.save(roleUsers);
        }
        Role roleUser = (Role) roleRepository.findByRoleName("ADMIN").orElseThrow();

        Collection<Role> roles = new HashSet<>();
        roles.add(roleUser);

        var user = User.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail())
                .imageUrl(request.getImageUrl())
                .password(passwordEncoder.encode(request.getPassword()))
                //.isEnabled(request.isEnabled())
                .isEnabled(false)
                .entreprise(request.getEntreprise())
                //.roles(request.getRoles())
                .roles(roles)
                .build();

        if(repository.findByEmail(user.getEmail()).isPresent()){
            throw new ResourceNotFoundException(" Il existe bien un compte avec cet E-mail : " +user.getEmail());
        }

        //user.getEntreprise().setEntrepriseId(request.getEntrepriseId());

        var userSaved = repository.save(user);
        publisher.publishEvent(new AccountEvent(userSaved, applicationUrl(servletRequest)));

        return AuthenticateResponse.builder()
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .isEnabled(user.isEnabled())
                .entrepriseId(user.getEntreprise().getEntrepriseId())
                .roles(user.getRoles())
                .build();
    }



    /**
     * Enrégistrer les utilisateurs pour les entréprises
     * **/
    @Override
    @Transactional
    public AuthenticateResponse registerEntrepriseUsers(RegisterRequest request){

        User authenticatedUser = getAuthUser();
        Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

        Entreprise entreprise = this.entrepriseService.getEntreprise(idEntreprise);
        request.setEntreprise(entreprise);

        if(request.getRoleName().isEmpty()){
            throw new ApiException("Le role est réquis !");
        }
        Role role = (Role) roleRepository.findByRoleName(request.getRoleName()).orElseThrow(
                ()-> new ApiException("Le role "+request.getRoleName()+" n'existe pas.")
        );

        Collection<Role> roles = new HashSet<>();
        roles.add(role);
        request.setRoles(roles);

        var user = User.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail())
                .imageUrl(request.getImageUrl())
                .password(passwordEncoder.encode(request.getPassword()))
                //.isEnabled(request.isEnabled())
                .isEnabled(true)
                .entreprise(request.getEntreprise())
                //.roles(request.getRoles())
                .roles(roles)
                .build();

        if(repository.findByEmail(user.getEmail()).isPresent()){
            throw new ResourceNotFoundException(" Il existe bien un compte avec cet E-mail : " +user.getEmail());
        }

        User saved = repository.save(user);

        return AuthenticateResponse.builder()
                .prenom(saved.getPrenom())
                .nom(saved.getNom())
                .email(saved.getEmail())
                .imageUrl(saved.getImageUrl())
                .isEnabled(saved.isEnabled())
                .entrepriseId(saved.getEntreprise().getEntrepriseId())
                .roles(saved.getRoles())
                .build();
    }

    @Override
    public boolean isFirstLogin(String username){
        var user = repository.findByEmail(username)
                .orElseThrow();
        return user.isFirstLogin();
    }

    @Override
    public User findByEmail(String email){
        User user = repository.findByEmail(email).orElseThrow();;
        return user;
    }

    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        /*var user = repository.findByEmail(request.getEmail())
                .orElseThrow();*/

        var user = getUserByEmail(request.getEmail());
        var extendetUser = new ExtendedUser(user.getEmail(), user.getPassword(),user.getAuthorities(), user.getEntreprise().getEntrepriseId());

        var jwtToken = jwtService.generateToken(extendetUser);
        var refreshToken  = jwtService.generateRefreshToken(extendetUser);
        // revokeAllUserTokens(user);
        // savedUserToken(user, jwtToken);
        return AuthenticateResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .isEnabled(user.isEnabled())
                .entreprise(user.getEntreprise().getNom())
                .isFirstLogin(user.isFirstLogin())
                .entrepriseId(user.getEntreprise().getEntrepriseId())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public PageResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keyword) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        User authenticatedUser = getAuthUser();
        Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

        Pageable p = PageRequest.of(pageNumber,pageSize,sort);

        Page<User> pageUser = this.repository.findByPrenomContainingAndEntreprise_Users(p, "%"+keyword+"%", idEntreprise);
        List<User> AllUsers = pageUser.getContent();

        List<AuthenticateResponse> content=  AllUsers.stream().map(
                Mapper::userToUserResponseDto).toList();

        PageResponse response = new PageResponse();
        response.setContent(content);
        response.setPageNo(pageUser.getNumber());
        response.setPageSize(pageUser.getSize());
        response.setTotalElements(pageUser.getTotalElements());
        response.setTotalPages(pageUser.getTotalPages());
        response.setLastPage(pageUser.isLast());

        return response;
    }

    @Override
    public PageResponse getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber,pageSize,sort);

        Page<User> pageUser = this.repository.findByPrenomContainingOrNomContaining(p, "%"+keyword+"%");
        List<User> AllUsers = pageUser.getContent();

        List<AuthenticateResponse> content=  AllUsers.stream().map(
                Mapper::userToUserResponseDto).toList();

        PageResponse response = new PageResponse();
        response.setContent(content);
        response.setPageNo(pageUser.getNumber());
        response.setPageSize(pageUser.getSize());
        response.setTotalElements(pageUser.getTotalElements());
        response.setTotalPages(pageUser.getTotalPages());
        response.setLastPage(pageUser.isLast());

        return response;
    }


    @Override
    public void deleteUser(Long idUser) {
        User user = repository.findById(idUser).orElseThrow(
                ()-> new ApiException("Il n'existe pas un user avec ID : " + idUser));


        //User user1 = this.modelMapper.map(user, User.class);
        this.repository.delete(user);
        //return this.modelMapper.map(user, AuthenticateResponse.class);
    }

    @Override
    public AuthenticateResponse getUserById(Long userId) {
        User user = this.repository.findById(userId).orElseThrow(() -> new ApiException("Il n'existe pas un user avec ID : " + userId));
        return Mapper.userToUserResponseDto(user);
    }

    public AuthenticateResponse updateUser(AuthenticateResponse authenticateResponse,  Long id){
        User user = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur no trouver avec ID : " +id));

        user.setPrenom(authenticateResponse.getPrenom());
        user.setNom(authenticateResponse.getNom());
        user.setEmail(authenticateResponse.getEmail());
        user.setImageUrl(authenticateResponse.getImageUrl());
        user.setEnabled(authenticateResponse.isEnabled());
        //user.setPassword(passwordEncoder.encode(authenticateResponse.getPassword()));
        User saved = this.repository.save(user);
        return Mapper.userToUserResponseDto(saved);
    }

    @Override
    public AuthenticateResponse updateStatusUser(Long id) {

        var user = this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur no trouver avec ID : " +id));


        // update the status
        if(user.isEnabled()){
            user.setEnabled(false);
        }else {
            user.setEnabled(true);
        }

        User saved = this.repository.save(user);
        return Mapper.userToUserResponseDto(saved);

    }

    /** private  void revokeAllUserTokens(User user){
     var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
     if(validUserTokens.isEmpty())
     return;
     validUserTokens.forEach(token -> {
     token.setExpired(true);
     token.setRevoked(true);
     });
     validUserTokens.saveAll(validUserTokens);
     } */
    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final  String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = (String) jwtService.extractUsername(refreshToken);
        if (userEmail != null){
            var user = this.repository.findByEmail(userEmail).orElseThrow();
            var extendetUser = new ExtendedUser(user.getEmail(), user.getPassword(),user.getAuthorities(), user.getEntreprise().getEntrepriseId());

            if (jwtService.tokenValid(refreshToken, extendetUser)){
                var accessToken = jwtService.generateToken(extendetUser);
                //revokeAllUserTokens(user);
                //savedUserToken(user, accessToken);
                var authResponse = AuthenticateResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .id(user.getId())
                        .prenom(user.getPrenom())
                        .nom(user.getNom())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .isEnabled(user.isEnabled())
                        .roles(user.getRoles())
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }



    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
         this.tokenRepository.save(verificationToken);
    }

    public void savePawdVerificationToken(User theUser, String token) {

        VerificationPwdToken verificationToken = new VerificationPwdToken(token, theUser);

         this.pwTokenRepository.save(verificationToken);
    }

    @Override
    public String valideToken(String theToken) {
        var token = pwTokenRepository.findByToken(theToken).orElseThrow(() -> new ApiException("Il n'existe pas un user avec ce token : " + theToken));
        if (token == null){
            return "Jeton de vérification invalide";
        }
        User user = token.getUser();
        Calendar calendar= Calendar.getInstance();
        if ((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            pwTokenRepository.delete(token);
            return  "Jeton déjà expiré";
        }
        user.setEnabled(true);
        repository.save(user);
        return "valid";
    }

    public String validPasswordResetToken(String theToken) {
        VerificationPwdToken token = pwTokenRepository.findByToken(theToken).orElseThrow(() -> new ApiException("Il n'existe pas un user avec ce token : " + theToken));
        if (token == null){
            return "Jeton de vérification invalide";
        }
        //User user = token.getUser();
        Calendar calendar= Calendar.getInstance();
        if ((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            pwTokenRepository.delete(token);
            return  "Lien déjà expiré";
        }
        //user.setEnabled(true);
        //repository.save(user);
        return "valid";
    }

    @Override
    public User getUser(Long userId) {
        return repository.findById(userId).orElseThrow(()-> new ApiException("Il n'existe pas un user avec ID : " + userId));
    }


    @Override
    public void addRoleToUser(RoleForm roleForm) {
        User user = repository.findByEmail(roleForm.getEmail()).orElseThrow(
                ()-> new ApiException("Le nom d'utilisateur "+roleForm.getEmail()+" n'existe pas")
        );

        Role role = (Role) roleRepository.findByRoleName(roleForm.getRoleName()).orElseThrow(
                ()-> new ApiException("Le role "+roleForm.getRoleName()+" n'existe pas.")
        );
        //var existedRole = repository.existsUserByRolesContaining(role);
        var existedRole = repository.findUserByRolesAndId(role, user.getId());

        if (existedRole.isPresent()) {
            throw  new ApiException(HttpStatus.FOUND, "L'Utilisateur a déjà cet role !" +roleForm.getRoleName());
        }else {
            user.getRoles().add(role);
        }
    }

    @Override
    public List<Role> getRole() {
        return roleRepository.findAll();
    }

    @Override
    public AuthenticateResponse changerPassword(ChangerPaswword changerPaswword, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(changerPaswword.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Mauvais mot de passe !!");
        }
        // check if the two new passwords are the same
        if (!changerPaswword.getNewPassword().equals(changerPaswword.getConfirmationPassword())) {
            throw new IllegalStateException("Les mots de passe ne sont pas les même !!");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(changerPaswword.getNewPassword()));
        user.setFirstLogin(false);
        /*User user = this.repository.findByEmailAndPassword(resetPassword.getEmail(), resetPassword.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouver avec l'E-mail : " +resetPassword.getEmail()+" mot de passe fournit !"));

        user.setEmail(resetPassword.getEmail());
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));*/

        User saved = this.repository.save(user);
        return Mapper.userToUserResponseDto(saved);

    }
    @Override
    public AuthenticateResponse forgetPassword(ResetPassword resetPassword ) {

        User user = this.repository.findByEmail(resetPassword.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouver avec l'E-mail :" +resetPassword.getEmail()));

        user.setEmail(resetPassword.getEmail());
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        User saved = this.repository.save(user);
        return Mapper.userToUserResponseDto(saved);
    }

    @Override
    public AuthenticateResponse resetPassword(User user, ResetPassword resetPassword ) {
        log.info("the user :: {}", user);
        User theUser = this.repository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouver avec l'E-mail :" +resetPassword.getEmail()));

        // check if the new passwords is not empty
        if (!resetPassword.getPassword().isEmpty()) {
            throw new IllegalStateException("Les mots de passe ne sont pas les même !!");
        }

        theUser.setEmail(resetPassword.getEmail());
        theUser.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        return Mapper.userToUserResponseDto(this.repository.save(user));
    }

    @Override
    public void sendVerifyEmailToSetPwd(String url, String email) throws MessagingException, UnsupportedEncodingException {

        User userByEmail = getUserByEmail(email);
        String subject = "Email Vérification";
        String senderName = "Dev teams";
        String mailContent = "<p> Hi, " + userByEmail.getPrenom() +" "+ userByEmail.getNom() + ", </p>" +
                    "<p>Veuillez suivre le lien ci-dessous pour finaliser, " +
                    "<a href=\"" +url+ "\">la création d'un nouveau mot de passe</a>" +
                    "<p>Service de portail d'enregistrement des utilisateurs";

        log.info("Url :: {}", url);
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("devopsteamlight@gmail.com", senderName);
        messageHelper.setTo(userByEmail.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    @Override
    public String validePasswordResetToken(String token) {
        return validPasswordResetToken(token);
    }

    @Override
    public User findUserByPasswordToken(String token) {
        VerificationPwdToken thetoken = pwTokenRepository.findByToken(token).orElseThrow(() -> new ApiException("Il n'existe pas un user avec ce token : " + token));
        log.info("token : {}", thetoken);
        return getUser(thetoken.getUser().getId());
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        savePawdVerificationToken(user, passwordResetToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken){
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken).orElseThrow();
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationToken(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);

    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow( ()-> new ApiException("Il n'existe pas un user avec cet email : " + email));
    }

    @Override
    public User getAuthUser() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = details.getUsername();
        return repository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé !")
        );
    }

    @Override
    public Integer generateOtp() {
        Random random = new Random();
        int min = 10_00;
        int max = 99_99;
        //return random.nextInt(100_000, 999_999);
        return random.nextInt((max - min) + 1) + min;
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    @Override
    public void verifyMail(String email) throws MessagingException, UnsupportedEncodingException {
        User user = getUserByEmail(email);
        int otp = this.generateOtp();
        //delete item if exist
        Optional<ForgotPassword> forgotp = this.forgotPasswordRepo.findByUser(user);
        forgotp.ifPresent(this.forgotPasswordRepo::delete);

        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("Code OTP")
                .text(String.valueOf(otp))
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .user(user)
                .expirationTime(new Date(System.currentTimeMillis() + 120 * 1000))
                .build();
        this.mailService.sendSimpleMail(mailBody);
        this.forgotPasswordRepo.save(fp);

    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    @Override
    public String verifyOtp(Integer otp, String email) {
        User theUser = getUserByEmail(email);

        ForgotPassword fp = this.forgotPasswordRepo.findByOtpAndUser(otp, theUser)
                .orElseThrow(() -> new ApiException("Code OTP invalide"));
        log.info("body :: {} ", fp);

        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
            this.forgotPasswordRepo.deleteById(fp.getFpid());
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,"Le code OTP est expiré !");
        }
        return "Code OTP valide";

    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    @Override
    public void reverifyOtp(String email) {
        User theUser = getUserByEmail(email);

        Optional<ForgotPassword> fp = this.forgotPasswordRepo.findByUser( theUser);
        log.info("body :: {} ", fp);

        fp.ifPresent(this.forgotPasswordRepo::delete);
    }

    @Override
    public void changerPasswordForForgot(ForgotPwd forgotPwd, String email) {

            User user = this.getUserByEmail(email);

        // check if the two new passwords are the same
        if (!forgotPwd.password().equals(forgotPwd.repeatPassword())) {
            log.info("payload : {} ", forgotPwd);
            throw new IllegalStateException("Les mots de passe ne sont pas les même !");
        }

            // update the password
            String password = passwordEncoder.encode(forgotPwd.password());
            this.repository.updatePassword(email, password);
            //return "Mot de passe changer avec succès !";


    }

    @Override
    public AuthenticateResponse removeCategoryFromProduct(Long roleId, Long userId) {
        User user = getUser(userId);
        Role role = roleRepository.findById(roleId).orElseThrow();
        if(!(Objects.nonNull(user.getRoles()))) {
            throw new ResourceNotFoundException("Il exist pas un user avec role ID " +roleRepository.findById(roleId).orElseThrow());
        }
        user.removeRole(role);
        return Mapper.userToUserResponseDto(user);
    }



    /* private void saveUserToken(User user, String jwtToken){
     var token = Token.builder()
     .user(user)
     .token(jwtToken)
     .tokenType(Token.Bearer)
     .expired(false)
     .revoked(false)
     .build();
     tokenRepository.save(token);
     }
     */
    /*private void revokeAllUsersYoken(User user){
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty){
            return;
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }*/

    private String applicationUrl(HttpServletRequest request) {
        return  "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    private Entreprise fromEntreprise( EntrepriseRequestDto request){
        return Entreprise.builder()
                .nom(request.getNom())
                .adresse(request.getAdresse())
                .email(request.getEmail())
                .contact(request.getContact())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

    }

}
