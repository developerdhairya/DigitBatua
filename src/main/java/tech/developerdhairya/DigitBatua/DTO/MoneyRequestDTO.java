package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoneyRequestDTO {
    @Email @NotNull
    String requestReceiverEmailId;

    @Min(value = 1,message = "You can't request amount less than Rs.1")
    private Integer amount;

    private String message;
}
