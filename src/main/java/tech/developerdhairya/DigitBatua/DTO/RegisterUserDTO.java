package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    @NotBlank
    @Size(min = 2, max = 20, message = "firstName should be between 2 and 20 characters")
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 20, message = "lastName should be between 2 and 20 characters")
    private String lastName;
    @Email
    private String emailId;
    @NotBlank
    @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    private String password;
    @NotBlank
    @Size(min = 8, max = 30, message = "Password should be between 9 and 20 characters")
    private String confirmPassword;
    @NotBlank
    @Size(min = 10, max = 13, message = "Invalid Mobile Number")
    private String mobileNumber;
}
