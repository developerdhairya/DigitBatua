package tech.developerdhairya.DigitBatua.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    private String emailId;
    private String currentPassword;
    private String confirmCurrentPassword;

}
