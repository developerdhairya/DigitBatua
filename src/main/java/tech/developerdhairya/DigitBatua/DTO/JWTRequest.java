package tech.developerdhairya.DigitBatua.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class JWTRequest {

    private String emailId;
    private String password;

}
