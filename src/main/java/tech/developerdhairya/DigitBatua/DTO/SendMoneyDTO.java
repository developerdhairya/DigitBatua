package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SendMoneyDTO {
    @NotBlank
    @NotEmpty
    @Email
    private String receiverEmailId;
    @Min(value = 100,message = "You can't send amount less than Rs.1")
    private Integer amount;

    private String message;
}
