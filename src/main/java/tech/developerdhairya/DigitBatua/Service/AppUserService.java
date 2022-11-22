package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.DTO.ChangePasswordDTO;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.NotFoundException;
import tech.developerdhairya.DigitBatua.Exception.UnauthorizedException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.VerificationTokenRepository;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

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


    public AppUser registerUser(RegisterUserDTO registerUserDTO) {
        String password = registerUserDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        AppUser appUser = new AppUser();
        appUser.setFirstName(registerUserDTO.getFirstName());
        appUser.setLastName(registerUserDTO.getLastName());
        appUser.setEmailId(registerUserDTO.getEmailId());
        appUser.setMobileNumber(registerUserDTO.getMobileNumber());
        appUser.setRole("USER");
        appUser.setHashedPassword(encodedPassword);
        return userRepository.save(appUser);

    }


    public void saveUserVerificationToken(String token, AppUser appUser) {
        VerificationToken verificationToken = new VerificationToken(token, appUser);
        verificationTokenRepository.save(verificationToken);

    }

    //enable user
    public void validateVerificationToken(String token) throws BadRequestException, UnauthorizedException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new BadRequestException("Verification Token Required");
        }
        if (util.checkTokenExpiry(verificationToken)) {
            throw new UnauthorizedException("Token Expired");
        }
        AppUser appUser = verificationToken.getAppUser();
        appUser.setVerified(true);
        userRepository.save(appUser);
        verificationTokenRepository.delete(verificationToken);
    }


    public void resendVerificationToken(String email) throws NotAcceptableException {
        AppUser appUser = userRepository.findByEmailId(email);
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
        mailerService.sendVerificationToken(email,token.getToken());
    }


    public void resetPassword(ChangePasswordDTO changePassword) throws UnauthorizedException, BadRequestException {
        if (!changePassword.getCurrentPassword().equals(changePassword.getConfirmCurrentPassword())) {
            throw new BadRequestException("Passwords don't match");
        }
        String currentEncodedPassword = passwordEncoder.encode(changePassword.getCurrentPassword());
        String emailId= SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = userRepository.findByEmailId(emailId);
        if (!appUser.getHashedPassword().equals(currentEncodedPassword)){
            throw new UnauthorizedException("Current Password Is Invalid");
        }
        String newEncodedPassword = passwordEncoder.encode(changePassword.getCurrentPassword());
        appUser.setHashedPassword(newEncodedPassword);
        userRepository.save(appUser);
    }




}
