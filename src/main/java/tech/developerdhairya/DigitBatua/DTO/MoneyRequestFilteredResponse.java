package tech.developerdhairya.DigitBatua.DTO;

import lombok.Data;
import tech.developerdhairya.DigitBatua.Entity.MoneyRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class MoneyRequestFilteredResponse {

    private UUID moneyRequestId;

    private String requesterEmailId;

    private String requestReceiverEmailId;

    private Integer amount;

    private String status;

    private String message;

    private Timestamp creationTimestamp;

    private Timestamp updateTimestamp;

    public MoneyRequestFilteredResponse(MoneyRequest moneyRequest) {
        this.moneyRequestId=moneyRequest.getMoneyRequestId();
        this.requesterEmailId=moneyRequest.getRequesterWallet().getAppUser().getEmailId();
        this.requestReceiverEmailId=moneyRequest.getRequestReceiverWallet().getAppUser().getEmailId();
        this.amount=moneyRequest.getAmount();
        this.status=moneyRequest.getStatus();
        this.message=moneyRequest.getMessage();
        this.creationTimestamp=moneyRequest.getCreationTimestamp();
        this.updateTimestamp=moneyRequest.getUpdateTimestamp();
    }

    public static List<MoneyRequestFilteredResponse> filterList(List<MoneyRequest> list){
        List<MoneyRequestFilteredResponse> output=new ArrayList<>();
        for(MoneyRequest ele:list){
            output.add(new MoneyRequestFilteredResponse(ele));
        }
        return output;
    }

}
