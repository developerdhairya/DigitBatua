package tech.developerdhairya.DigitBatua.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@ToString
@DynamicUpdate //To update only changed entities while saving
public class AppUser {
    @Id
    @GeneratedValue(generator = "UUiD")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID userId;

    @NotBlank
    @Size(min = 2,max = 20,message = "firstName should be between 2 and 20 characters")
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2,max = 20,message = "lastName should be between 2 and 20 characters")
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid emailId")
    private String emailId;

    @Column(nullable = false, unique = true)
    @Size(min = 10,max = 13,message = "Invalid Mobile Number")
    private String mobileNumber;

    @Column(nullable = false)
    @JsonIgnore
    private String password;          //not visible in json response

    @Column(nullable = false)
    private String role = "USER";

    @Column(nullable = false)
    private boolean isVerified = false;

    @CreationTimestamp
    private Timestamp creationTimestamp = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;


}