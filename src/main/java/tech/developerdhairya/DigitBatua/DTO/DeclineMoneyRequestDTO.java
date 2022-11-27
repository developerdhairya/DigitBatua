package tech.developerdhairya.DigitBatua.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeclineMoneyRequestDTO{
    @NotBlank @NotNull
    String requestId;
}
