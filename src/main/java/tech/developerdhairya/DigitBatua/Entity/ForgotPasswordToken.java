package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@DynamicUpdate
public class ForgotPasswordToken implements Token{

    public ForgotPasswordToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expirationTime=AuthenticationUtil.calculateExpirationTime(10);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private Instant expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private AppUser appUser;

    @CreationTimestamp
    private Timestamp creationTimestamp = Timestamp.from(Instant.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;

    @Override
    public Instant getExpirationTime() {
        return this.expirationTime;
    }
}

