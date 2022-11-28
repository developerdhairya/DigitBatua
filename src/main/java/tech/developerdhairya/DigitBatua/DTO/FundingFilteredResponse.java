package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.developerdhairya.DigitBatua.Entity.Funding;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @Data
public class FundingFilteredResponse {

    private UUID fundingId;
    private Integer amount;
    private String rzpPaymentLink;
    private String status;
    private Timestamp creationTimestamp;
    private Timestamp updateTimestamp;

    FundingFilteredResponse(Funding funding){
        this.fundingId=funding.getFundingId();
        this.amount=funding.getAmount();
        this.rzpPaymentLink=funding.getRzpPaymentLink();
        this.status=funding.getStatus();
        this.creationTimestamp=funding.getCreationTimestamp();
        this.updateTimestamp=funding.getUpdateTimestamp();
    }


    public static List<FundingFilteredResponse> filterList(List<Funding> list){
        List<FundingFilteredResponse> output=new ArrayList<>();
        for(Funding ele:list){
            output.add(new FundingFilteredResponse(ele));
        }
        return output;
    }


}
