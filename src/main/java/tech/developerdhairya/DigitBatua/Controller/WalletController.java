package tech.developerdhairya.DigitBatua.Controller;

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
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.UnauthorizedException;
import tech.developerdhairya.DigitBatua.Service.AppUserService;
import tech.developerdhairya.DigitBatua.Service.PaymentService;
import tech.developerdhairya.DigitBatua.Service.UserDetailsServiceImpl;
import tech.developerdhairya.DigitBatua.Service.WalletService;
import tech.developerdhairya.DigitBatua.Util.JWTUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController("/wallet")
public class WalletController {

    @Autowired
    private AppUserService appUserService;


    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @GetMapping("/activate")
    public ResponseEntity<Object> activateWallet() {
        try {
            String emailId=SecurityContextHolder.getContext().getAuthentication().getName();
            Wallet wallet=walletService.activateWallet(emailId);
            return ResponseHandler.generateSuccessResponse(wallet, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }






}
