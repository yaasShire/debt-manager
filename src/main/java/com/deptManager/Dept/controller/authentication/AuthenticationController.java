package com.deptManager.Dept.controller.authentication;

import com.deptManager.Dept.exception.authenticationException.AuthenticationException;
import com.deptManager.Dept.model.authenticationModel.*;
import com.deptManager.Dept.service.authenticationService.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private  final AuthenticateService authenticateService;

    @PostMapping("/signUp")
    public OTPResponseModel signUpUser(@RequestBody SignUpRequestModel body) throws AuthenticationException {
        return authenticateService.signUpUser(body);
    }

    @PostMapping("/verifyOTP")
    public SignUpResponseModel verifyOTP(@RequestBody OTP otp) throws AuthenticationException {
        return authenticateService.verifyOTP(otp);
    }

    @PostMapping("/signIn")
    public SignInResponseModel singIn(@RequestBody SignInRequestModel body) throws AuthenticationException {
        return authenticateService.signIn(body);
    }

    @PostMapping("/requestForgetPasswordOTP")
    public OTPResponseModel forgetPasswordVerificationPhoneNumber(@RequestBody RequestForgetPasswordOTP phoneNumber) throws AuthenticationException {
        return authenticateService.sendForgetPasswordOTP(phoneNumber);
    }

    @PostMapping("/verifyForgetPasswordOTP")
    public OTPResponseModel verifyForgetPasswordOTP(@RequestBody OTP otp) throws AuthenticationException {
        return authenticateService.verifyForgetPasswordOTP(otp);
    }

    @PostMapping("/changeForgottedPassword")
    public ForgetPasswordResponse updateForgettedPassword(@RequestBody ChangePasswordCredentials changePasswordCredentials) throws AuthenticationException {
        return authenticateService.updatePassword(changePasswordCredentials);
    }

}
