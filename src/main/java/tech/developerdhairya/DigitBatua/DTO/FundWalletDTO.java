package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@AllArgsConstructor @Data @NoArgsConstructor
public class FundWalletDTO {
    @Min(value = 100,message = "You can't add less than Rs.1")
    private Integer amount;
}
