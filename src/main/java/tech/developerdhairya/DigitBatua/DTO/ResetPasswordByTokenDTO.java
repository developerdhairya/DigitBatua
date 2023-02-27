package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class ResetPasswordByTokenDTO {

    @Email @NotBlank @NotEmpty
    String emailId;

    @NotBlank @NotEmpty
    String token;

    @NotBlank @NotEmpty @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    String newPassword;

    @NotBlank @NotEmpty @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    String confirmNewPassword;

}
