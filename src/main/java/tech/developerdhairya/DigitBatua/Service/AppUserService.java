package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.developerdhairya.DigitBatua.DTO.ChangePasswordDTO;
import tech.developerdhairya.DigitBatua.DTO.GetUserResponse;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.UnauthorizedException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.VerificationTokenRepository;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AuthenticationUtil util;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailerService mailerService;


    @Transactional
    public AppUser registerUser(RegisterUserDTO registerUserDTO) throws DataIntegrityViolationException {
        String password = registerUserDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        AppUser appUser = new AppUser();
        appUser.setFirstName(registerUserDTO.getFirstName());
        appUser.setLastName(registerUserDTO.getLastName());
        appUser.setEmailId(registerUserDTO.getEmailId());
        appUser.setMobileNumber(registerUserDTO.getMobileNumber());
        appUser.setRole("USER");
        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);
        String token=UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken(token, appUser);
        verificationTokenRepository.save(verificationToken);
        mailerService.sendVerificationToken(registerUserDTO.getEmailId(),token);
        return appUser;
    }



    //enable user
    public void validateVerificationToken(String token) throws BadRequestException, UnauthorizedException {
        System.out.println(token);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new BadRequestException("Invalid token");
        }
        if (util.checkTokenExpiry(verificationToken)) {
            throw new UnauthorizedException("Token Expired");
        }
        AppUser appUser = verificationToken.getAppUser();
        appUser.setVerified(true);
        userRepository.save(appUser);
        verificationTokenRepository.delete(verificationToken);
    }


    public void resendVerificationToken(String emailId) throws NotAcceptableException {
        AppUser appUser = userRepository.findByEmailId(emailId);
        if (appUser.isVerified()) {
           throw new NotAcceptableException("User has already been verified");
        }
        String emailBody;
        VerificationToken token = verificationTokenRepository.findByAppUser(appUser);
        if (util.checkTokenExpiry(token)) {
            verificationTokenRepository.delete(token);
            emailBody=UUID.randomUUID().toString();
            VerificationToken newToken = new VerificationToken(emailBody, appUser);
            verificationTokenRepository.save(newToken);
        }else{
            emailBody=token.getToken();
        }
        mailerService.sendVerificationToken(emailId,emailBody);
    }

//    public void


    public void resetPassword(ChangePasswordDTO changePassword) throws UnauthorizedException, BadRequestException {
        if (!changePassword.getCurrentPassword().equals(changePassword.getConfirmCurrentPassword())) {
            throw new BadRequestException("Passwords don't match");
        }
        String emailId= SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = userRepository.findByEmailId(emailId);
        if (!passwordEncoder.matches(changePassword.getCurrentPassword(),appUser.getPassword())){
            throw new UnauthorizedException("Current Password Is Invalid");
        }
        String newEncodedPassword = passwordEncoder.encode(changePassword.getNewPassword());
        appUser.setPassword(newEncodedPassword);
        userRepository.save(appUser);
    }

    public GetUserResponse getUserByEmailId(String emailId) throws BadRequestException {
        AppUser appUser=userRepository.findByEmailId(emailId);
        if(appUser==null){
            throw new BadRequestException("No user exists with the given emailId");
        }
        return new GetUserResponse(appUser.getEmailId(),appUser.getFirstName(),appUser.getLastName(),appUser.getMobileNumber());
    }





}
