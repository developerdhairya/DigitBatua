package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoneyRequestDTO {
    @NotBlank @NotEmpty  @Email
    String requestReceiverEmailId;

    @NotNull @Min(value = 1,message = "You can't request amount less than Rs.1")
    private Integer amount;

    private String message;
}
