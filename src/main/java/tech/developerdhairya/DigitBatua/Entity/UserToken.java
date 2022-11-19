package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userTokenId;

    @NotBlank
    private String token;

    @NotBlank
    private String type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUserEntity;

    @NotNull
    private Timestamp expirationTime;

    @CreationTimestamp
    private Timestamp creationTimestamp;


}