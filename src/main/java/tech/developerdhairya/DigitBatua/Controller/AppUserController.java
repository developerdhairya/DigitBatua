package tech.developerdhairya.DigitBatua.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.DTO.ResponseHandler;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
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
            AppUser data=appUserService.register(userDTO);
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
    public String reg(){
        return "l";
    }

}
