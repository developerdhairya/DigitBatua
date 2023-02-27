package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class TransactionFilteredResponse {
    private UUID transactionId;
    private String type;
    private AppUser from;
    private AppUser to;
    private Integer amount;
    private String status;
    private String message;

    TransactionFilteredResponse(Transaction transaction){
        this.transactionId=transaction.getTransactionId();
        this.type=transaction.getType();
        this.from=transaction.getFrom().getAppUser();
        this.amount=transaction.getAmount();
        this.status=transaction.getStatus();
        this.message=transaction.getMessage();
    }

    public static List<TransactionFilteredResponse> filterList(List<Transaction> list){
        List<TransactionFilteredResponse> output=new ArrayList<>();
        for(Transaction ele:list){
            output.add(new TransactionFilteredResponse(ele));
        }
        return output;
    }

}
