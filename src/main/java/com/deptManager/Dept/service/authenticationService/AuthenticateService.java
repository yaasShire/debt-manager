package com.deptManager.Dept.service.authenticationService;

import com.deptManager.Dept.exception.authenticationException.AuthenticationException;
import com.deptManager.Dept.model.authenticationModel.*;

public interface AuthenticateService {
    public OTPResponseModel signUpUser(SignUpRequestModel body) throws AuthenticationException;

    SignUpResponseModel verifyOTP(OTP otp) throws AuthenticationException;

    SignInResponseModel signIn(SignInRequestModel body) throws AuthenticationException;

    OTPResponseModel sendForgetPasswordOTP(RequestForgetPasswordOTP phoneNumber) throws AuthenticationException;

    ForgetPasswordResponse updatePassword(ChangePasswordCredentials changePasswordCredentials) throws AuthenticationException;

    OTPResponseModel verifyForgetPasswordOTP(OTP otp) throws AuthenticationException;
}
