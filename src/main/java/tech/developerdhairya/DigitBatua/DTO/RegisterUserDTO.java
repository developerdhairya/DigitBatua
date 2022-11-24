package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterUserDTO {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String emailId;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String mobileNumber;

}
