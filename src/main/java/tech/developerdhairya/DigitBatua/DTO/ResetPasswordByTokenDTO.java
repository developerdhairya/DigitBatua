package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor @NoArgsConstructor @Data
public class ResetPasswordByTokenDTO {

    @Email
    String emailId;

    @NotBlank
    String token;

    @NotBlank
    @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    String newPassword;

    @NotBlank
    @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    String confirmNewPassword;

}
