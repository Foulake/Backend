package com.team.dev.service;

import com.team.dev.dto.*;
import com.team.dev.model.Role;
import com.team.dev.model.User;
import com.team.dev.model.VerificationToken;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

public interface AuthService {

    AuthenticateResponse register(RegisterRequest request, final HttpServletRequest servletRequest);
    AuthenticateResponse registerEntrepriseUsers(RegisterRequest request);

    AuthenticateResponse authenticate(AuthenticateRequest request);
    public boolean isFirstLogin(String username);

    public User findByEmail(String email);
    AuthenticateResponse updateStatusUser(Long id);
    PageResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keyword);
    PageResponse getAllUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir, String keyword);
    void deleteUser(Long id);
    AuthenticateResponse getUserById(Long UserId);
    AuthenticateResponse updateUser(AuthenticateResponse authenticateResponse,  Long id);
    void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException;

    void saveUserVerificationToken(User theUser, String verificationToken);

    String valideToken(String theToken);

    public VerificationToken generateNewVerificationToken(String oldToken);

    User getUser(Long userId);
    void addRoleToUser(RoleForm roleForm);

    List<Role> getRole();
    AuthenticateResponse changerPassword(ChangerPaswword changerPaswword, Principal connectedUser);
    AuthenticateResponse forgetPassword(ResetPassword resetPassword);
    AuthenticateResponse resetPassword(User user , ResetPassword resetPassword );
     void sendVerifyEmailToSetPwd(String url, String email) throws MessagingException, UnsupportedEncodingException;
     String validePasswordResetToken(String token);
     User findUserByPasswordToken(String token);
     void createPasswordResetTokenForUser(User user, String passwordResetToken);
    User getUserByEmail(String email);

    User getAuthUser();

    Integer generateOtp();

    void verifyMail(String email) throws MessagingException, UnsupportedEncodingException;
    String verifyOtp(Integer otp, String email);


    void reverifyOtp(String email);

    void changerPasswordForForgot(ForgotPwd forgotPwd, String email);


    AuthenticateResponse removeCategoryFromProduct(Long roleId, Long userId);
}
