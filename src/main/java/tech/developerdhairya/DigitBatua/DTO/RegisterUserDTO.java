package tech.developerdhairya.DigitBatua.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;

@Data @EqualsAndHashCode @ToString
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String emailId;
    private String Password;
    private String role;
    private Timestamp creationTimestamp;
    private Timestamp updateTimestamp;
}
