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
import tech.developerdhairya.DigitBatua.DTO.ResetPasswordByTokenDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.ForgotPasswordToken;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.UnauthorizedException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.ForgotPasswordTokenRepository;
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
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailerService mailerService;


    @Transactional(rollbackFor = Exception.class)
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
        AppUser x=userRepository.saveAndFlush(appUser); // using save() sends email even if DataIntegrityVoilation takes place.
        String token=UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken(token, appUser);
        verificationTokenRepository.saveAndFlush(verificationToken);
        mailerService.sendVerificationToken(registerUserDTO.getEmailId(),token);
        return appUser;
    }



    //enable user
    @Transactional(rollbackFor = Exception.class)
    public void validateVerificationToken(String token) throws BadRequestException, UnauthorizedException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            throw new BadRequestException("Invalid token");
        }
        if (AuthenticationUtil.checkTokenExpiry(verificationToken)) {
            throw new UnauthorizedException("Token Expired");
        }
        AppUser appUser = verificationToken.getAppUser();
        appUser.setVerified(true);
        userRepository.save(appUser);
        verificationTokenRepository.delete(verificationToken);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resendVerificationToken(String emailId) throws NotAcceptableException {
        AppUser appUser = userRepository.findByEmailId(emailId);
        if (appUser.isVerified()) {
           throw new NotAcceptableException("User has already been verified");
        }
        String tokenBody;
        VerificationToken token = verificationTokenRepository.findByAppUser(appUser);
        if (AuthenticationUtil.checkTokenExpiry(token)) {
            verificationTokenRepository.delete(token);
            tokenBody=UUID.randomUUID().toString();
            VerificationToken newToken = new VerificationToken(tokenBody, appUser);
            verificationTokenRepository.save(newToken);
        }else{
            tokenBody=token.getToken();
        }
        mailerService.sendVerificationToken(emailId,tokenBody);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendForgotPasswordToken(String emailId) throws BadRequestException {
        AppUser appUser = userRepository.findByEmailId(emailId);
        if(appUser==null){
            throw new BadRequestException("No user with this emailId exists");
        }
        String tokenBody=UUID.randomUUID().toString();
        ForgotPasswordToken newToken = new ForgotPasswordToken(tokenBody, appUser);
        forgotPasswordTokenRepository.save(newToken);
        mailerService.sendForgotPasswordToken(emailId,tokenBody);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetForgottenPassword(ResetPasswordByTokenDTO tokenDTO) throws BadRequestException, UnauthorizedException {
        if(!tokenDTO.getNewPassword().equals(tokenDTO.getConfirmNewPassword())){
            throw new BadRequestException("Passwords don't match");
        }
        ForgotPasswordToken token=forgotPasswordTokenRepository.findByToken(tokenDTO.getToken());
        if(token==null){
            throw new BadRequestException("Invalid Token");
        }
        if(AuthenticationUtil.checkTokenExpiry(token)){
            throw new UnauthorizedException("Token Expired");
        }

        AppUser appUser=token.getAppUser();
        forgotPasswordTokenRepository.delete(token);
        if(!appUser.getEmailId().equals(tokenDTO.getEmailId())){
            throw new UnauthorizedException("Invalid Details");
        }
        String hashedPassword=passwordEncoder.encode(tokenDTO.getNewPassword());
        appUser.setPassword(hashedPassword);
        userRepository.saveAndFlush(appUser);
    }


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
