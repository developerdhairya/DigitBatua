package tech.developerdhairya.DigitBatua.Service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.developerdhairya.DigitBatua.DTO.FundWalletResponse;
import tech.developerdhairya.DigitBatua.DTO.FundingFilteredResponse;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Funding;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Enum.TransactionType;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.ForbiddenException;
import tech.developerdhairya.DigitBatua.Exception.NotFoundException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.FundingRepository;
import tech.developerdhairya.DigitBatua.Repository.WalletRepository;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;
import tech.developerdhairya.DigitBatua.Enum.FundingStatus;

import java.util.List;
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

    // Make a new entry in wallet table
    public Wallet activateWallet(String emailId) throws BadRequestException, ForbiddenException {
        AppUser appUser=appUserRepository.findByEmailId(emailId);
        if(!appUser.isVerified()){
            throw new ForbiddenException("User not verified");
        }
        Wallet wallet=walletRepository.findByAppUser_EmailId(emailId);
        if(wallet!=null){
            throw new BadRequestException("Wallet Already Activated for the given user");
        }
        Wallet wallet1=new Wallet();
        wallet1.setAppUser(appUser);
        return walletRepository.save(wallet1);
    }

    public Wallet getWalletDetails(String emailId) throws NotFoundException {
        Wallet wallet=walletRepository.findByAppUser_EmailId(emailId);
        if(wallet==null){
            throw new NotFoundException("Wallet Not Activated for the given account");
        }
        return walletRepository.save(wallet);
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

    @Transactional(rollbackFor = Exception.class)
    public void verifyFunding(String fundingId) throws BadRequestException, RazorpayException, PaymentRequiredException {
        Optional<Funding> funding=fundingRepository.findById(UUID.fromString(fundingId));
        if(funding.isEmpty()){
            throw new BadRequestException("Invalid Funding ID");
        }
        if(!funding.get().getStatus().equals(FundingStatus.Pending.name())){
            throw new BadRequestException("The funding request is already "+funding.get().getStatus());
        }
        String paymentId=funding.get().getRzpPaymentId();
        boolean isVerified=paymentService.verifyPayment(paymentId,funding.get().getAmount());
        if(!isVerified){
            throw new PaymentRequiredException("Payment Required");
        }
        Wallet wallet=funding.get().getWallet();
        wallet.setBalance(wallet.getBalance()+funding.get().getAmount());
        walletRepository.save(wallet);
        funding.get().setStatus(FundingStatus.Successful.name());
        fundingRepository.save(funding.get());
    }

    public List<FundingFilteredResponse> getAllFunding(String emailId, Integer pageNumber, Integer pageSize, Integer sort,String status) throws BadRequestException {
        if(sort!=1 && sort!=-1){
            throw new BadRequestException("Invalid sort parameter");
        }
        if (!status.equals(FundingStatus.Successful.name()) && !status.equals(FundingStatus.Pending.name()) && !status.equals("All")){
            throw new BadRequestException("Invalid status parameter");
        }
        Sort sortCondition=Sort.by("updateTimestamp");
        Sort sortParam=sort==-1?sortCondition.descending():sortCondition.ascending();
        Pageable pageable =PageRequest.of(pageNumber,pageSize,sortParam);
        List<Funding> list;
        if (status.equals("All")){
            list=fundingRepository.findAllByWallet_AppUser_EmailId(emailId,pageable);
        }else {
            list=fundingRepository.findAllByWallet_AppUser_EmailIdAndStatus(emailId,status,pageable);
        }
        return FundingFilteredResponse.filterList(list);
    }

}
