package tech.developerdhairya.DigitBatua.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private String password;          //not visible in json response

    @Column(nullable = false)
    private String role="USER";

    @Column(nullable = false)
    private boolean isVerified=false;

    @CreationTimestamp
    private Timestamp creationTimestamp=Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;


}