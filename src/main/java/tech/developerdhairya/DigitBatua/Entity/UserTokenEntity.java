package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Entity @Table(name = "user_token") @ToString
public class UserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "token")
    @NotBlank
    private String token;

    @NotNull
    private Timestamp expirationTime;

    @NotNull
    @CreationTimestamp
    private Timestamp creationTimestamp;
}