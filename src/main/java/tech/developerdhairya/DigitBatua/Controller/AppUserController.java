package tech.developerdhairya.DigitBatua.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.DTO.ResponseHandler;
import tech.developerdhairya.DigitBatua.Entity.AppUserEntity;
import tech.developerdhairya.DigitBatua.Service.AppUserService;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterUserDTO userDTO){
        try{
            System.out.println(userDTO.toString());
            AppUserEntity data=appUserService.register(userDTO);
            return ResponseHandler.generateSuccessResponse(data, HttpStatus.CREATED);
        }catch (InternalAuthenticationServiceException e){
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }catch (Exception e){
            System.out.println(e);
            return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND,e.toString());
        }
    }

    @GetMapping("/register")
    public String reg(){
        return "l";
    }

}
