package tech.developerdhairya.DigitBatua.DTO;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class GetUserDTO {
    @Email
    private String emailId;
}
