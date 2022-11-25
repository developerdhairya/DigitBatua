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
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.NotAcceptableException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
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
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @PostMapping("/fund")
    public ResponseEntity<Object> fundWallet(@RequestBody FundWalletDTO fundWalletDTO) {
        try {
            String emailId=SecurityContextHolder.getContext().getAuthentication().getName();
            FundWalletResponse response=walletService.fundWallet(emailId,fundWalletDTO.getAmount());
            return ResponseHandler.generateSuccessResponse(response, HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/funding/verify")
    public ResponseEntity<Object> verifyFundingStatus(@RequestParam String fundingId) {
        try {
            walletService.verifyFunding(fundingId);
            return ResponseHandler.generateSuccessResponse("Money has been added to your wallet", HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (PaymentRequiredException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.PAYMENT_REQUIRED, e.getLocalizedMessage());
        }catch (Exception e){
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }








}
