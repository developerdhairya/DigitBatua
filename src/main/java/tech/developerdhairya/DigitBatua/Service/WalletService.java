package tech.developerdhairya.DigitBatua.Service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.developerdhairya.DigitBatua.DTO.FundWalletResponse;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Funding;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.FundingRepository;
import tech.developerdhairya.DigitBatua.Repository.WalletRepository;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private FundingRepository fundingRepository;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    public Wallet activateWallet(String emailId) throws BadRequestException {
        AppUser appUser=appUserRepository.findByEmailId(emailId);
        Wallet wallet=walletRepository.findByAppUser_EmailId(emailId);
        if(wallet!=null){
            throw new BadRequestException("Wallet Already Activated for the given user");
        }
        Wallet wallet1=new Wallet();
        wallet1.setAppUser(appUser);
        return walletRepository.save(wallet1);
    }

    @Transactional
    public FundWalletResponse fundWallet(String emailId,Integer amount) throws BadRequestException, RazorpayException {
        AppUser appUser=appUserRepository.findByEmailId(emailId);
        Wallet wallet=walletRepository.findByAppUser_EmailId(emailId);
        if(wallet==null){
            throw new BadRequestException("Wallet is not Activated for the given user");
        }
        UUID fundingId= UUID.randomUUID();
        PaymentLink paymentLink=paymentService.generatePaymentLink(appUser,amount,fundingId.toString());
        Funding funding=new Funding();
        funding.setFundingId(fundingId);
        funding.setWallet(wallet);
        funding.setAmount(amount);
        funding.setRzpPaymentId(paymentLink.get("id"));
        funding.setRzpPaymentLink(paymentLink.get("short_url"));
        fundingRepository.save(funding);
        return new FundWalletResponse(paymentLink.get("short_url"));
    }

    @Transactional
    public void verifyFunding(String fundingId) throws BadRequestException, RazorpayException, PaymentRequiredException {
        Optional<Funding> funding=fundingRepository.findById(UUID.fromString(fundingId));
        if(funding.isEmpty()){
            throw new BadRequestException("Invalid Funding ID");
        }
        String paymentId=funding.get().getRzpPaymentId();
        boolean isVerified=paymentService.verifyPayment(paymentId,funding.get().getAmount());
        if(!isVerified){
            throw new PaymentRequiredException("Payment Required");
        }
        Wallet wallet=funding.get().getWallet();
        wallet.setBalance(wallet.getBalance()+funding.get().getAmount());
        walletRepository.save(wallet);
        funding.get().setStatus("success");
        fundingRepository.save(funding.get());
    }

}
