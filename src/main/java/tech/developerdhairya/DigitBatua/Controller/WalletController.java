package tech.developerdhairya.DigitBatua.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.developerdhairya.DigitBatua.DTO.*;
import tech.developerdhairya.DigitBatua.Entity.Transaction;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Enum.TransactionType;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.ForbiddenException;
import tech.developerdhairya.DigitBatua.Exception.NotFoundException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
import tech.developerdhairya.DigitBatua.Service.TransactionService;
import tech.developerdhairya.DigitBatua.Service.WalletService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/activate")
    public ResponseEntity<Object> activateWallet() {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            Wallet wallet = walletService.activateWallet(emailId);
            return ResponseHandler.generateSuccessResponse(wallet, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (ForbiddenException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getWalletDetails() {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            Wallet wallet = walletService.getWalletDetails(emailId);
            return ResponseHandler.generateSuccessResponse(wallet, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }


    @PostMapping("/fund")
    public ResponseEntity<Object> fundWallet(@Valid @RequestBody FundWalletDTO fundWalletDTO) {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            FundWalletResponse response = walletService.fundWallet(emailId, fundWalletDTO.getAmount());
            return ResponseHandler.generateSuccessResponse(response, HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
        }
    }

    @GetMapping("/funding")
    public ResponseEntity<Object> getAllFunding(@RequestParam @Min(0) Integer pageNumber, @RequestParam @Min(1) Integer pageSize,@RequestParam Integer sort,@RequestParam String status) {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            List<FundingFilteredResponse> response = walletService.getAllFunding(emailId, pageNumber, pageSize,sort,status);
            return ResponseHandler.generateSuccessResponse(response, HttpStatus.OK);
        } catch (Exception e) {
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
        } catch (Exception e) {
            System.out.println(e.getCause().getCause().getMessage());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @PutMapping("/sendMoney")
    public ResponseEntity<Object> sendMoney(@Valid @RequestBody SendMoneyDTO sendMoneyDTO) {
        try {
            return ResponseHandler.generateSuccessResponse(transactionService.sendMoney(sendMoneyDTO, TransactionType.DirectTransfer), HttpStatus.OK);
        } catch (PaymentRequiredException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.PAYMENT_REQUIRED, e.getLocalizedMessage());
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @PutMapping("/requestMoney")
    public ResponseEntity<Object> requestMoney(@Valid @RequestBody MoneyRequestDTO moneyRequestDTO) {
        try {
            return ResponseHandler.generateSuccessResponse(transactionService.requestMoney(moneyRequestDTO), HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @PutMapping("/declineRequest")
    public ResponseEntity<Object> declineRequest(@Valid @RequestBody DeclineMoneyRequestDTO dto) {
        try {
            return ResponseHandler.generateSuccessResponse(transactionService.declineMoneyRequest(dto.getRequestId()), HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (PaymentRequiredException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.PAYMENT_REQUIRED, e.getLocalizedMessage());
        } catch (ForbiddenException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @PutMapping("/approveRequest")
    public ResponseEntity<Object> approveRequest(@Valid @RequestBody ApproveMoneyRequestDTO dto) {
        try {
            return ResponseHandler.generateSuccessResponse(transactionService.approveMoneyRequest(dto.getRequestId()), HttpStatus.OK);
        } catch (PaymentRequiredException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.PAYMENT_REQUIRED, e.getLocalizedMessage());
        } catch (ForbiddenException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @GetMapping("/receivedMoneyRequests")
    public ResponseEntity<Object> getReceivedMoneyRequests(@RequestParam @Min(0) Integer pageNumber, @RequestParam @Min(1) Integer pageSize,@RequestParam Integer sort) {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseHandler.generateSuccessResponse(transactionService.getReceivedMoneyRequests(emailId, pageNumber, pageSize,sort), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @GetMapping("/sentMoneyRequests")
    public ResponseEntity<Object> getSentMoneyRequests(@RequestParam @Min(0) Integer pageNumber, @RequestParam @Min(1) Integer pageSize,@RequestParam Integer sort) {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseHandler.generateSuccessResponse(transactionService.getSentMoneyRequests(emailId, pageNumber, pageSize,sort), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }

    @GetMapping("/transactionHistory")
    public ResponseEntity<Object> getTransactionHistory(@RequestParam @Min(0) Integer pageNumber, @RequestParam @Min(1) Integer pageSize,@RequestParam Integer sort,@RequestParam String type) {
        try {
            String emailId = SecurityContextHolder.getContext().getAuthentication().getName();
            List<TransactionFilteredResponse> list=transactionService.getTransactionHistory(emailId,pageNumber,pageSize,type,sort);
            return ResponseHandler.generateSuccessResponse(list, HttpStatus.OK);
        } catch (BadRequestException e) {
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.toString());
            return ResponseHandler.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something Went Wrong!");
        }
    }


}
