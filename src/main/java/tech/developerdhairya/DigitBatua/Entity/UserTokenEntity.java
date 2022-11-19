package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
//@Table(name = "user_token")
@ToString
public class UserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    private String token;

    @NotBlank
    private String type;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "user_id", nullable = false, foreignKey = "")
//    private AppUserEntity appUserEntity;

    @NotNull
    private Timestamp expirationTime;

    @CreationTimestamp
    private Timestamp creationTimestamp;


}