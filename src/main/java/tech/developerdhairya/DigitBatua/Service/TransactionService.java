package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.developerdhairya.DigitBatua.DTO.MoneyRequestDTO;
import tech.developerdhairya.DigitBatua.DTO.MoneyRequestFilteredResponse;
import tech.developerdhairya.DigitBatua.DTO.SendMoneyDTO;
import tech.developerdhairya.DigitBatua.DTO.TransactionFilteredResponse;
import tech.developerdhairya.DigitBatua.Entity.MoneyRequest;
import tech.developerdhairya.DigitBatua.Entity.Transaction;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Enum.MoneyRequestStatus;
import tech.developerdhairya.DigitBatua.Enum.TransactionStatus;
import tech.developerdhairya.DigitBatua.Enum.TransactionType;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Exception.ForbiddenException;
import tech.developerdhairya.DigitBatua.Exception.PaymentRequiredException;
import tech.developerdhairya.DigitBatua.Repository.MoneyRequestRepository;
import tech.developerdhairya.DigitBatua.Repository.TransactionRepository;
import tech.developerdhairya.DigitBatua.Repository.WalletRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MoneyRequestRepository moneyRequestRepository;

    @Transactional(rollbackFor = Exception.class)
    public Wallet sendMoney(SendMoneyDTO sendMoneyDTO, TransactionType transactionType) throws BadRequestException, PaymentRequiredException {
        String senderEmailId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (senderEmailId.equals(sendMoneyDTO.getReceiverEmailId())) {
            throw new BadRequestException("Cant transfer money to your own wallet.");
        }
        Wallet senderWallet = walletRepository.findByAppUser_EmailId(senderEmailId);
        if (senderWallet == null) {
            throw new BadRequestException("Wallet Not Activated.");
        }
        if (senderWallet.getBalance() < sendMoneyDTO.getAmount()) {
            throw new PaymentRequiredException("Insufficient Balance.Add Rs." + (sendMoneyDTO.getAmount() - senderWallet.getBalance() + " to transfer."));
        }
        Wallet receiverWallet = walletRepository.findByAppUser_EmailId(sendMoneyDTO.getReceiverEmailId());
        if (receiverWallet == null) {
            throw new BadRequestException("No wallet for the given account exists.");
        }
        senderWallet.setBalance(senderWallet.getBalance() - sendMoneyDTO.getAmount());
        receiverWallet.setBalance(receiverWallet.getBalance() + sendMoneyDTO.getAmount());
        Transaction transaction = new Transaction();
        transaction.setType(transactionType.name());
        transaction.setFrom(senderWallet);
        transaction.setTo(receiverWallet);
        transaction.setAmount(sendMoneyDTO.getAmount());
        transaction.setStatus(TransactionStatus.Successful.name());
        transaction.setMessage(sendMoneyDTO.getMessage());
        transactionRepository.save(transaction);
        return senderWallet;
    }

    public MoneyRequestFilteredResponse requestMoney(MoneyRequestDTO dto) throws BadRequestException {
        String requesterEmailId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (requesterEmailId.equals(dto.getRequestReceiverEmailId())) {
            throw new BadRequestException("Cant request money from your own wallet.");
        }
        Wallet requesterWallet = walletRepository.findByAppUser_EmailId(requesterEmailId);
        if (requesterWallet == null) {
            throw new BadRequestException("Wallet Not Activated.");
        }
        Wallet requestReceiverWallet = walletRepository.findByAppUser_EmailId(dto.getRequestReceiverEmailId());
        if (requestReceiverWallet == null) {
            throw new BadRequestException("No wallet exists for the given user");
        }
        MoneyRequest moneyRequest = new MoneyRequest();
        moneyRequest.setRequesterWallet(requesterWallet);
        moneyRequest.setRequestReceiverWallet(requestReceiverWallet);
        moneyRequest.setAmount(dto.getAmount());
        moneyRequest.setStatus(MoneyRequestStatus.Pending.toString());
        MoneyRequest response = moneyRequestRepository.save(moneyRequest);
        return new MoneyRequestFilteredResponse(response);
    }

    @Transactional
    public Wallet approveMoneyRequest(String requestId) throws BadRequestException, ForbiddenException, PaymentRequiredException {
        Optional<MoneyRequest> moneyRequest = moneyRequestRepository.findById(UUID.fromString(requestId));
        if (moneyRequest.isEmpty()) {
            throw new BadRequestException("Invalid requestId");
        }
        String requestReceiverEmailId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!moneyRequest.get().getRequestReceiverWallet().getAppUser().getEmailId().equals(requestReceiverEmailId)) {
            throw new ForbiddenException("You are not authorized to perform this request");
        }
        if (!moneyRequest.get().getStatus().equals(MoneyRequestStatus.Pending.name())) {
            throw new BadRequestException("The request is not in pending state");
        }

        SendMoneyDTO sendMoneyDTO = new SendMoneyDTO();
        sendMoneyDTO.setAmount(moneyRequest.get().getAmount());
        sendMoneyDTO.setReceiverEmailId(moneyRequest.get().getRequesterWallet().getAppUser().getEmailId());
        sendMoneyDTO.setMessage(moneyRequest.get().getMessage());
        Wallet response=sendMoney(sendMoneyDTO, TransactionType.RequestTransfer);
        moneyRequest.get().setStatus(MoneyRequestStatus.Approved.name());
        moneyRequestRepository.save(moneyRequest.get());
        return response;
    }


    public Object declineMoneyRequest(String requestId) throws BadRequestException, ForbiddenException, PaymentRequiredException {
        Optional<MoneyRequest> moneyRequest = moneyRequestRepository.findById(UUID.fromString(requestId));
        if (moneyRequest.isEmpty()) {
            throw new BadRequestException("Invalid requestId");
        }
        String requestReceiverEmailId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!moneyRequest.get().getRequestReceiverWallet().getAppUser().getEmailId().equals(requestReceiverEmailId)) {
            throw new ForbiddenException("You are not authorized to perform this request");
        }
        if (!moneyRequest.get().getStatus().equals(MoneyRequestStatus.Pending.name())) {
            throw new BadRequestException("The request is not in pending state");
        }
        moneyRequest.get().setStatus(MoneyRequestStatus.Declined.name());
        moneyRequestRepository.save(moneyRequest.get());
        return null;
    }


    public List<MoneyRequestFilteredResponse> getReceivedMoneyRequests(String emailId, Integer pageNumber, Integer pageSize, Integer sort) throws BadRequestException {
        if (sort != 1 && sort != -1) {
            throw new BadRequestException("Invalid sort parameter");
        }
        Sort sortCondition = Sort.by("updateTimestamp");
        Sort sortParam = sort == -1 ? sortCondition.descending() : sortCondition.ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortParam);
        List<MoneyRequest> response = moneyRequestRepository.findAllByRequestReceiverWallet_AppUser_EmailId(emailId, pageable);
        return MoneyRequestFilteredResponse.filterList(response);
    }

    public List<MoneyRequestFilteredResponse> getSentMoneyRequests(String emailId, Integer pageNumber, Integer pageSize, Integer sort) throws IllegalArgumentException, BadRequestException {
        if (sort != 1 && sort != -1) {
            throw new BadRequestException("Invalid sort parameter");
        }
        Sort sortCondition = Sort.by("updateTimestamp");
        Sort sortParam = sort == -1 ? sortCondition.descending() : sortCondition.ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortParam);
        List<MoneyRequest> response = moneyRequestRepository.findAllByRequesterWallet_AppUser_EmailId(emailId, pageable);
        return MoneyRequestFilteredResponse.filterList(response);
    }

    public List<TransactionFilteredResponse> getTransactionHistory(String emailId, Integer pageNumber, Integer pageSize, String type, Integer sort) throws BadRequestException {
        if (sort != 1 && sort != -1) {
            throw new BadRequestException("Invalid sort parameter");
        }
        if (!type.equals(TransactionType.RequestTransfer.name()) && !type.equals(TransactionType.DirectTransfer.name()) && !type.equals("All")) {
            throw new BadRequestException("Invalid type parameter");
        }
        Sort sortCondition = Sort.by("updateTimestamp");
        Sort sortParam = sort == -1 ? sortCondition.descending() : sortCondition.ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortParam);
        List<Transaction> response;
        if (type.equals("All")) {
            response = transactionRepository.getAllByTo_AppUser_EmailId_OrFrom_AppUser_EmailId(emailId,emailId,pageable);
        } else {
            response = transactionRepository.getAllByTo_AppUser_EmailId_OrFrom_AppUser_EmailIdAndType(emailId,emailId,type,pageable);
        }
        return TransactionFilteredResponse.filterList(response);
    }
}

