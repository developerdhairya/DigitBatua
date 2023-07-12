package tech.developerdhairya.DigitBatua.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotEmpty
    @Size(min = 8, max = 30, message = "Password should be between 8 and 30 characters")
    private String currentPassword;
    @NotEmpty
    @Size(min = 8, max = 30, message = "Password should be between 8 and 30 characters")
    private String confirmCurrentPassword;
    @NotEmpty
    @Size(min = 8, max = 30, message = "New Password should be between 8 and 30 characters")
    private String newPassword;

}
