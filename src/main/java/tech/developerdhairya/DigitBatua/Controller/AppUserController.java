package tech.developerdhairya.DigitBatua.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.DTO.ResponseHandler;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Token;
import tech.developerdhairya.DigitBatua.Entity.UserToken;
import tech.developerdhairya.DigitBatua.Service.AppUserService;
import tech.developerdhairya.DigitBatua.Service.MailerService;
import tech.developerdhairya.DigitBatua.Service.UserTokenService;


@RestController
@RequestMapping("/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserTokenService userTokenService;

    @Autowired
    private MailerService mailerService;

    @PostMapping("/register") @Transactional
    public ResponseEntity<Object> register(@RequestBody RegisterUserDTO userDTO){
        try{
            AppUser data=appUserService.register(userDTO);
            UserToken token=userTokenService.generateVerificationToken(data);
            mailerService.sendVerificationToken(data.getEmailId(), token.getToken());
            return ResponseHandler.generateSuccessResponse(data, HttpStatus.CREATED);
        }catch (HttpServerErrorException.InternalServerError e){
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }catch (DataIntegrityViolationException e){
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT,"Duplicate Entry");
        }catch (Exception e){
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST,e.getCause().getMessage());
        }
    }

    @GetMapping("/register")
    public int reg(String email){
        return userTokenService.getTokenByUser(email, Token.verification);
    }

}
