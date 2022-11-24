package tech.developerdhairya.DigitBatua.Controller;

import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tech.developerdhairya.DigitBatua.DTO.*;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.UnauthorizedException;
import tech.developerdhairya.DigitBatua.Service.AppUserService;
import tech.developerdhairya.DigitBatua.Service.PaymentService;
import tech.developerdhairya.DigitBatua.Service.UserDetailsServiceImpl;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;
import tech.developerdhairya.DigitBatua.Util.JWTUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthenticationUtil util;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        try {
            AppUser appUser = appUserService.registerUser(registerUserDTO);
            return ResponseHandler.generateSuccessResponse(appUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<Object> verifyRegistration(@RequestParam("token") String token) {
        try {
            appUserService.validateVerificationToken(token);
            return ResponseHandler.generateSuccessResponse(null, HttpStatus.ACCEPTED);
        } catch (BadRequestException e) {
            e.printStackTrace(); //log error
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/resendVerificationToken")
    public ResponseEntity<Object> resendVerificationToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        try {
            appUserService.resendVerificationToken(email);
            return ResponseHandler.generateSuccessResponse(null, HttpStatus.ACCEPTED);
        } catch (NotAcceptableException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@NotNull @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            appUserService.resetPassword(changePasswordDTO);
            return ResponseHandler.generateSuccessResponse(null, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@NotNull @RequestBody JWTRequest jwtRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtRequest.getEmailId(),
                    jwtRequest.getPassword()
            );
            authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails
                    = userDetailsServiceImpl.loadUserByUsername(jwtRequest.getEmailId());
            String jwtToken = jwtUtil.generateToken(userDetails);
            return ResponseHandler.generateSuccessResponse(new JWTResponse(jwtToken), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.UNAUTHORIZED, e.toString());
        } catch (UsernameNotFoundException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/test")
    public String test() {
        try {
            return paymentService.generatePaymentLink(20000, "jf6766esgER$j", "taneja.dhairya12@gmail.com");
        } catch (RazorpayException e) {
            e.printStackTrace();
            return null;
        }
    }


}
