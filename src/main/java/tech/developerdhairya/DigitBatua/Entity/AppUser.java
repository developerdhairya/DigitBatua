package tech.developerdhairya.DigitBatua.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @NotBlank
    @Length(message = "Length of firstName should be b/w 2-50", min = 2, max = 50)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Length(message = "Length of lastName should be b/w 2-50", min = 2, max = 50)
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid emailId")
    private String emailId;

    @Column(nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(nullable = false)
    @JsonIgnore
    private String hashedPassword;          //not visible in json response

    @Column(nullable = false)
    private String role="user";

    @Column(nullable = false)
    private boolean isVerified=true;

    @CreationTimestamp
    private Timestamp creationTimestamp=Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;


}