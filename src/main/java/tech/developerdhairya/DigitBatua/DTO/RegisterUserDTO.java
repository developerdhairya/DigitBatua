package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private String mobileNumber;

}
