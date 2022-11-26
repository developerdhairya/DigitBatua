package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ForgotPasswordToken implements Token{

    public ForgotPasswordToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expirationTime=util.calculateExpirationTime(EXPIRATION_TIME);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private AppUser appUser;

}

