package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor @Data
public class GetUserResponse {
    String emailId;
    String firstName;
    String lastName;
    String mobileNumber;
}
