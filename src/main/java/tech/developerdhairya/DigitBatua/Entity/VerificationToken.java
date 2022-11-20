package tech.developerdhairya.DigitBatua.Entity;


import Util.AuthenticationUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity @Data
@NoArgsConstructor
public class VerificationToken{
    private static AuthenticationUtil util=new AuthenticationUtil();

    private static final int EXPIRATION_TIME=10;

    public VerificationToken(String token, AppUser appUser) {
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
    private AppUser appUser;




}
