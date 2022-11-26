package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.MoneyRequest;
import tech.developerdhairya.DigitBatua.Entity.Transaction;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.MoneyRequestRepository;
import tech.developerdhairya.DigitBatua.Repository.TransactionRepository;
import tech.developerdhairya.DigitBatua.Repository.WalletRepository;
import tech.developerdhairya.DigitBatua.Util.TransactionStatus;
import tech.developerdhairya.DigitBatua.Util.TransactionType;

@Service
public class TransactionService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MoneyRequestRepository moneyRequestRepository;

    @Transactional
    public void sendMoney(String receiverEmailId,int amount) throws BadRequestException, PaymentRequiredException {
        String senderEmailId= SecurityContextHolder.getContext().getAuthentication().getName();
        Wallet senderWallet=walletRepository.findByAppUser_EmailId(senderEmailId);
        if(senderWallet==null){
            throw new BadRequestException("Wallet Not Activated.");
        }
        if(senderWallet.getBalance()<amount){
            throw new PaymentRequiredException("Insufficient Balance.Add Rs."+(amount-senderWallet.getBalance()+" to transfer."));
        }
        Wallet receiverWallet=walletRepository.findByAppUser_EmailId(senderEmailId);
        if(receiverWallet==null){
            throw new BadRequestException("No wallet for the given account exists.");
        }
        senderWallet.setBalance(senderWallet.getBalance()-amount);
        receiverWallet.setBalance(receiverWallet.getBalance()+amount);
        Transaction transaction=new Transaction();
        transaction.setFrom(senderWallet);
        transaction.setTo(receiverWallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DirectTransfer.toString());
        transactionRepository.save(transaction);
    }

    public void requestMoney(String requestReceiverEmailId,Integer amount) throws BadRequestException {
        String requesterEmailId= SecurityContextHolder.getContext().getAuthentication().getName();
        Wallet requesterWallet=walletRepository.findByAppUser_EmailId(requesterEmailId);
        if(requesterWallet==null){
            throw new BadRequestException("Wallet Not Activated.");
        }
        Wallet requestReceiverWallet=walletRepository.findByAppUser_EmailId(requestReceiverEmailId);
        if(requestReceiverWallet==null){
            throw new BadRequestException("Wallet Not Activated.");
        }
        MoneyRequest moneyRequest=new MoneyRequest();
        moneyRequest.setRequesterWallet(requesterWallet);
        moneyRequest.setRequestReceiverWallet(requestReceiverWallet);
        moneyRequest.setAmount(amount);
        moneyRequest.setStatus(TransactionStatus.Pending.name());
        moneyRequestRepository.save(moneyRequest);
    }

    //list all requests
    //accept requests
    //decline requests

}

