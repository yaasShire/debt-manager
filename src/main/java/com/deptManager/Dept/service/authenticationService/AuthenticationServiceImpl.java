package com.deptManager.Dept.service.authenticationService;

import com.deptManager.Dept.DeptApplication;
import com.deptManager.Dept.configuration.JWTService;
import com.deptManager.Dept.entity.AppUser;
import com.deptManager.Dept.entity.Role;
import com.deptManager.Dept.entity.token.Token;
import com.deptManager.Dept.entity.token.TokenType;
import com.deptManager.Dept.exception.authenticationException.AuthenticationException;
import com.deptManager.Dept.model.authenticationModel.*;
import com.deptManager.Dept.repository.AppUserRepository;
import com.deptManager.Dept.repository.TokenRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.net.http.HttpRequest;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class AuthenticationServiceImpl implements AuthenticateService {
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    LocalDateTime generatedTime;
    private AppUser user;
    GeneratedOTP generatedOTP = new GeneratedOTP();
    private String currentOTP;
    private static final int OTP_LENGTH = 6;
    private static final int EXPIRATION_TIME_IN_MINUTES = 1;
    @Override
    public OTPResponseModel signUpUser(SignUpRequestModel body) throws AuthenticationException {

        if(body.getShopName() == null){
            throw new AuthenticationException("Shop name is required");
        }
        if(body.getPhoneNumber() == null){
            throw new AuthenticationException("Phone number is required");
        }
        if(body.getPassword() == null){
            throw new AuthenticationException("Password is required");
        }
        Optional<AppUser> checkIfUserExists = appUserRepository.findByPhoneNumber(body.getPhoneNumber());
        if(checkIfUserExists.isPresent()){
            throw new AuthenticationException("User already exists");
        }

        try {
            if(DeptApplication.getAccessToken() == null || DeptApplication.getAccessToken().equalsIgnoreCase("")){
                System.out.println(DeptApplication.getAccessToken());
                throw new AuthenticationException("Access token is not provide for SMS");
            }else{
                log.info("user body {}", body);
            sendOTP(body.getPhoneNumber());
                user = AppUser.builder()
                        .shopName(body.getShopName())
                        .phoneNumber(body.getPhoneNumber())
                        .password(passwordEncoder.encode(body.getPassword()))
                        .isSubscribed(false)
                        .authenticated(false)
                        .joinedDate(getCurrentDate())
                        .role(Role.USER)
                        .build();
                OTPResponseModel response = OTPResponseModel.builder()
                        .message("OTP is sent, verify")
                        .build();
                return response;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthenticationException(e.getMessage());
        }


    }

    @Override
    public SignUpResponseModel verifyOTP(OTP otp) throws AuthenticationException {
        if (otp == null){
            throw new AuthenticationException("Enter the otp sent to your phone number.");
        }
        if (isOtpValid(otp.getOtp(), generatedTime)) {
            if(otp.getOtp().equalsIgnoreCase(generatedOTP.getGeneratedOTP())){
                user.setAuthenticated(true);
                appUserRepository.save(user);
                String token = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, token);
                SignUpResponseModel signUpResponseModel = SignUpResponseModel.builder()
                        .accessToken(token)
                        .refreshToken(refreshToken)
                        .status(HttpStatus.CREATED)
                        .message("Shop User is created successfully")
                        .build();
                return signUpResponseModel;
            }
            else {
                throw new AuthenticationException("OTP is invalid");
            }
        } else {
            System.out.println("OTP is invalid");
            throw new AuthenticationException("OTP is invalid");
        }

    }

    @Override
    public SignInResponseModel signIn(SignInRequestModel body) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getPhoneNumber(), body.getPassword()));
            // User is authenticated, proceed with the request
        } catch (Exception e) {
            // Authentication failed, handle the exception
            System.out.println("Authentication failed: " + e.getMessage());
            throw new AuthenticationException("Invalid credentials");
        }

        Optional<AppUser> appUser = appUserRepository.findByPhoneNumber(body.getPhoneNumber());
        if (appUser.isPresent()){
        var jwtToken = jwtService.generateToken(appUser.get());
        var refreshToken = jwtService.generateRefreshToken(appUser.get());
        revokeAllUserTokens(appUser.get());
        saveUserToken(appUser.get(), jwtToken);
        return SignInResponseModel.builder()
                .status(HttpStatus.OK)
                .message("User successfully loged in")
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .id(appUser.get().getId())
                .authenticated(appUser.get().getAuthenticated())
                .isSubscribed(appUser.get().getIsSubscribed())
                .phoneNumber(appUser.get().getPhoneNumber())
                .shopName(appUser.get().getShopName())
                .joinedDate(appUser.get().getJoinedDate())
                .build();
        }else {
        throw new AuthenticationException("User not found");
        }
//        log.info("app user = {}", appUser.get());
    }

    @Override
    public OTPResponseModel sendForgetPasswordOTP(RequestForgetPasswordOTP phoneNumber) throws AuthenticationException {
        if (phoneNumber.getPhoneNumber() ==null || phoneNumber.getPhoneNumber().equalsIgnoreCase("")){
            throw new AuthenticationException("Enter your phone number");
        }
        try {
            Optional<AppUser> appUser = appUserRepository.findByPhoneNumber(phoneNumber.getPhoneNumber());
            System.out.println("user phone number---->" + appUser.get().getPhoneNumber());
            if (appUser.isPresent()){
                sendOTP(phoneNumber.getPhoneNumber());
                generatedTime=LocalDateTime.now();
                OTPResponseModel response = OTPResponseModel
                        .builder()
                        .message("OTP is sent successfully please verify")
                        .build();
                return response;
            }else{
                throw new AuthenticationException("User does not exist");
            }
        }catch (Exception e){
            throw new AuthenticationException(e.getMessage());
        }
    }



    @Override
    public OTPResponseModel verifyForgetPasswordOTP(OTP otp) throws AuthenticationException {
        if (otp.getOtp() == null || otp.getOtp().equalsIgnoreCase("")) {
            throw new AuthenticationException("Enter the otp sent to your phone number.");
        }
        if (isOtpValid(otp.getOtp(), generatedTime)){
            System.out.println("OTP is valid");
            if (otp.getOtp().equalsIgnoreCase(generatedOTP.getGeneratedOTP())) {
                OTPResponseModel response = OTPResponseModel
                        .builder()
                        .message("OTP verified successfully")
                        .build();
                return response;
            }
            else {
                throw new AuthenticationException("Invalid OTP");
            }
        } else {
            throw new AuthenticationException("OTP is invalid");
        }


    }

    @Override
    public ForgetPasswordResponse updatePassword(ChangePasswordCredentials changePasswordCredentials) throws AuthenticationException {
        try {
            Optional<AppUser> appUser = appUserRepository.findByPhoneNumber(changePasswordCredentials.getPhoneNumber());
            if (appUser.isPresent()){
            appUser.get().setPassword(passwordEncoder.encode(changePasswordCredentials.getNewPassword()));
            appUserRepository.save(appUser.get());
            ForgetPasswordResponse forgetPasswordResponse = ForgetPasswordResponse
                    .builder()
                    .message("Password update successfully")
                    .build();
            return forgetPasswordResponse;
            }else{
                throw new AuthenticationException("User does not exist");
            }
        }catch (Exception e){
            throw new AuthenticationException("User not found");
        }
    }


    private void sendOTP(String phoneNumber) throws URISyntaxException, IOException, InterruptedException, AuthenticationException {
        generatedOTP.setGeneratedOTP(generateOTP());
        generatedTime=LocalDateTime.now();
        SendOTPRequestModel otpRequestModel = SendOTPRequestModel.builder()
                .mobile(phoneNumber)
                .message(generatedOTP.getGeneratedOTP())
                .RequestDate(getCurrentDate())
                .build();
        Gson gson = new Gson();
        String otpJsonRequest =  gson.toJson(otpRequestModel);
        log.info("SMS token for hormuud {}", DeptApplication.getAccessToken());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://smsapi.hormuud.com/api/SendSMS"))
                .setHeader("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ DeptApplication.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(otpJsonRequest))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Sent OTP response {}", response.body());
        System.out.println("otp result = "+ response.body());
        if (response.statusCode() != 200){
            throw new AuthenticationException("OTP Could not be sent");
        }
    }

    private String generateOTP(){
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
    private void saveUserToken(AppUser user, String token){
        Token jwtToken = Token.builder()
                .token(token)
                .user(user)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .build();
        tokenRepository.save(jwtToken);
    }
    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    public String getCurrentDate(){
        // Get the current date and time
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        // Convert the date to a string
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    public static boolean isOtpValid(String otp, LocalDateTime generatedTime) {
        LocalDateTime expirationTime = generatedTime.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(expirationTime);
    }

}