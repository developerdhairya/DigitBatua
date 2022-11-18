package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Entity @Table(name = "app_user") @ToString
public class AppUserEntity {
    @Id  @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id", nullable = false)
    private UUID id;
    @NotBlank
    @Length(message = "Length of firstName should be b/w 2-50", min = 2, max = 50)
    @Column(nullable = false)
    private String firstName;
    @NotBlank
    @Length(message = "Length of lastName should be b/w 2-50", min = 2, max = 50)
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    @Email(message = "Invalid emailId", regexp = "[A-Z0-9+_.-]+@[A-Z0-9.-]+$")
    private String emailId;
    @Column(nullable = false)
    private String hashedPassword;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private boolean isVerified;


}