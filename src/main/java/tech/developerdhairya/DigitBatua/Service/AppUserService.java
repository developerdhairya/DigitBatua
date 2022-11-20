package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.DTO.ChangePasswordDTO;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;
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


    public AppUser registerUser(RegisterUserDTO registerUserDTO) {
        String password = registerUserDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        AppUser appUser = new AppUser();
        appUser.setFirstName(registerUserDTO.getFirstName());
        appUser.setLastName(registerUserDTO.getLastName());
        appUser.setEmailId(registerUserDTO.getEmailId());
        appUser.setRole("USER");
        appUser.setHashedPassword(encodedPassword);
        return userRepository.save(appUser);

    }


    public void saveUserVerificationToken(String token, AppUser appUser) {
        VerificationToken verificationToken = new VerificationToken(token, appUser);
        verificationTokenRepository.save(verificationToken);

    }

    //enable user
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "Verification token is Invalid";
        }

        if (util.checkTokenExpiry(verificationToken)) {
            return "Token is expired";
        }

        AppUser appUser = verificationToken.getAppUser();
        appUser.setVerified(true);
        userRepository.save(appUser);
        verificationTokenRepository.delete(verificationToken);
        return "User validation Successful";

    }


    public String resendVerificationToken(String email) {
        AppUser appUser = userRepository.findByEmailId(email);
        if (appUser == null) {
            return "No user exists with the given email-address";
        }
        if (appUser.isVerified()) {
            return "User has already been verified";
        }
        VerificationToken token = verificationTokenRepository.findByAppUser(appUser);

        if (!util.checkTokenExpiry(token)) {
            //DO mail user the same token
            return "The token has been resent to your registered email id";
        }

        //delete expired token
        verificationTokenRepository.delete(token);

        //create new token
        VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString(), appUser);
        verificationTokenRepository.save(newToken);

        //DO mail user the newly generated token.

        return "New Token has been sent to your registered email ID";
    }


    public String resetPassword(ChangePasswordDTO changePassword) {
        if (!changePassword.getCurrentPassword().equals(changePassword.getConfirmCurrentPassword())) {
            return "Passwords dont match";
        }
        String currentEncodedPassword = passwordEncoder.encode(changePassword.getCurrentPassword());
        AppUser appUser = userRepository.findByEmailId(changePassword.getEmailId());
        if (appUser.getHashedPassword().equals(currentEncodedPassword)) {
            String newEncodedPassword = passwordEncoder.encode(changePassword.getCurrentPassword());
            appUser.setHashedPassword(newEncodedPassword);
            userRepository.save(appUser);
            return "Success";
        }
        return "Current Password is invalid";

    }




}
