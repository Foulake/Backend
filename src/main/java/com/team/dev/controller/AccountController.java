package com.team.dev.controller;

import com.team.dev.dto.*;
import com.team.dev.event.listener.AccountEventListener;
import com.team.dev.exception.ApiException;
import com.team.dev.model.User;
import com.team.dev.model.VerificationToken;
import com.team.dev.repository.TokenRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.impl.EntrepriseUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@Log4j2
@Transactional
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class AccountController {
	private final AuthService service;
	private final AccountEventListener eventListener;
	private final TokenRepository tokenRepository;
	private  final EntrepriseUserService entrepriseUserService;

	/*@PostMapping("/register")
	public ApiResponse register(
			@RequestBody @Valid RegisterRequest request
			, final HttpServletRequest servletRequest
	) {
		try {
			AuthenticateResponse response = service.register(request, servletRequest);
			return new ApiResponse(Boolean.TRUE, "Succès ! Veuillez vérifier votre courrier électronique pour finaliser votre inscription");
		}catch (MailException e){
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Veuillez verifier votre connection internet ! Puis réessayer !");
		}
	}*/


	@PostMapping("/register")
	public ApiResponse registerUserWithEntreprise(@Valid
			@RequestBody EntrepriseUserDto request
			, final HttpServletRequest servletRequest
	) {
		try {
			log.info("Data request ::: {} ", request);
			entrepriseUserService.enregistrerEntrepriseAvecUtilisateur(request, servletRequest);
			return new ApiResponse(Boolean.TRUE, "Succès ! Veuillez vérifier votre courrier électronique pour finaliser votre inscription");
		}catch (MailException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Veuillez vérifier votre connexion internet ! Puis réessayer !");
		}
	}

	//@CrossOrigin(origins = "http://localhost:4200/**")
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(
			@RequestBody @Valid AuthenticateRequest request
	){
		try {
			String username = request.getEmail();
			if(this.service.isFirstLogin(username)){
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous devez changer votre mot de passe.");
			}
			AuthenticateResponse authenticate = service.authenticate(request);
			return  ResponseEntity.ok(authenticate);
		}catch (BadCredentialsException e){
			throw  new ApiException(HttpStatus.BAD_REQUEST, "Username ou mot de passe incorrect !");
		}
	}

	@PostMapping("/refresh-token")
	public void refreshToken(
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		service.refreshToken(request, response);
	}

	@GetMapping("/verifyByEmail")
	public String verifyEmail(@RequestParam("token") String token){
		VerificationToken theToken = this.tokenRepository.findByToken(token).orElseThrow();
		if(theToken.getUser().isEnabled()){
			return "Ce compte a éte déjà vérifié, s'il vous plaît,connectez-vous.";
		}
		String verificationResult = service.valideToken(token);
		if (verificationResult.equalsIgnoreCase("valid")){
			return "Email verifié avec succès. Maintenant vous pouvez vous connectez à votre compte";
		}
		return "Invalid verification token";
	}


	@PutMapping("/change-password")
	public ApiResponse ChangerPassword(
			@RequestBody @Valid ResetPassword request
	) {
		try {
			AuthenticateResponse response = service.forgetPassword(request);
			return new ApiResponse(Boolean.TRUE, "Success ! Password changed");
		}catch (HttpServerErrorException.InternalServerError e){
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Veuillez réessayer utérieurement !");
		}

	}

	@PostMapping("/sendMail-change-password/{email}")
	public ApiResponse sendMailToChangerPassword(
			@PathVariable final String email
	) {
		try {
			//String url ="localhost:9191/api/v1/auth/verifyEmail?token="+email;
			String url ="http://localhost:4200/#/forget-password";

			service.sendVerifyEmailToSetPwd(url, email);
			return new ApiResponse(Boolean.TRUE, "Merci de vérifier votre compte email pour changer ton mot de passe !");
		}catch (HttpServerErrorException.InternalServerError e){
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Veuillez réessayer utérieurement !");
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/password-reset-request")
	public String resetPasswordRequest(@RequestBody ResetPassword resetPassword, final HttpServletRequest servletRequest) throws MessagingException, UnsupportedEncodingException {

		log.info("paylod :: {}", resetPassword);
		User userByEmail = service.getUserByEmail(resetPassword.getEmail());
		log.info("found user :: {}", userByEmail);
		String passwordResetUrl= "";
		if(userByEmail != null){
			try {
				String passwordResetToken =UUID.randomUUID().toString();
				service.createPasswordResetTokenForUser(userByEmail , passwordResetToken);
				passwordResetUrl = passwordResetEmailLink(userByEmail, applicationUrl(servletRequest),  passwordResetToken);
			}catch (MailSendException sendException){
				throw new MessagingException("Erreur de connection! Vérifier votre connexion");
			}catch (AddressException addressException){
				throw new AddressException("L'adresse e-mail n'est pas valid !");
			}catch (Exception e){
				throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"internal serveur error !");
			}

		}
		return passwordResetUrl;
	}

	private String passwordResetEmailLink(User user, String applicationUrl, String passwordToken) throws MessagingException, UnsupportedEncodingException {
		String url = applicationUrl+"/api/v1/auth/reset-password?token="+passwordToken;
		//String url = applicationUrl+"/api/v1/auth/verifyEmail?token="+passwordToken;
		//sendMailToChangerPassword(user.getEmail());
		eventListener.senPasswordResetVerifyEmail(url, user);
		//eventLister.sendPasswordResetVerification(url);
		log.info("Click the link to reset your password : {}", url);
		return  url;
	}

	@PutMapping("/reset-password")
	public String restetPassword(
			@RequestBody ResetPassword request, @RequestParam("token" ) final String token
	) {
			String tokenVerificationResult = service.validePasswordResetToken(token);
			if (!tokenVerificationResult.equalsIgnoreCase("valid")){
				return  "Invalid token";
			}
			Optional<User> theUser = Optional.ofNullable(service.findUserByPasswordToken(token));
			log.info("user foundn::: {}", theUser.get());
			if(theUser.isPresent()){
				service.resetPassword(theUser.get(), request);
				return "Mot de passe a été rénouveler avec succès";
			}
			//AuthenticateResponse response = service.forgetPassword(request);
			//return new ApiResponse(Boolean.TRUE, "Success ! Password changed");
			return "Token pour rénouvéler votre mot de passe est invalid";
	}

	private String applicationUrl(HttpServletRequest request) {
		return  "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}

	/**
	 * Forget password part
	 * **/

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	@PostMapping("/generate/{email}")
	public ResponseEntity<ApiResponse>  verifyMail(@PathVariable(value = "email") String email) throws MessagingException, UnsupportedEncodingException {
		try {
			this.service.verifyMail(email);
		}catch (MailException mailException){
			throw new MailSendException("Vérifier votre connection internet! Code OTP non envoyé");
		}
		catch (Exception ex){
			throw new ApiException("Erreur server" +ex);
		}
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE,"Code envoyer à votre email pour vérification !"), HttpStatus.OK);
	}

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	@PostMapping("/verifyOtp/{email}/{otp}")
	public ResponseEntity<ApiResponse> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
		service.verifyOtp(otp,email);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE,"Code OTP valide !"), HttpStatus.OK);
	}

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	@PostMapping("/regenerate/{email}")
	public ResponseEntity<ApiResponse> reVerifyMail(@PathVariable String email){
		try {
			this.service.reverifyOtp(email);
			service.verifyMail(email);
		}catch (MailException mailException){
			throw new MailSendException("Vérifier votre connection internet! Code OTP non envoyé");
		}
		catch (Exception ex){
			throw new ApiException("Erreur server" +ex);
		}
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE,"Code OTP envoyer à votre email pour vérification !"), HttpStatus.OK);
	}

	@PostMapping("/change-password-for-forgot/{email}")
	public ResponseEntity<ApiResponse> changePasswordHandler(@RequestBody ForgotPwd forgotPwd, @PathVariable final String email){
		service.changerPasswordForForgot(forgotPwd, email);
		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "Mot de pas changer avec succès !"), HttpStatus.OK);
		}

}
