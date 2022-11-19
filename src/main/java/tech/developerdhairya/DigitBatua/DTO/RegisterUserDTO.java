package tech.developerdhairya.DigitBatua.DTO;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor @NoArgsConstructor @Data @ToString
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String mobileNumber;
}
