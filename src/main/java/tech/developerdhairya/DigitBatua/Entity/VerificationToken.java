package tech.developerdhairya.DigitBatua.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity @Data
@NoArgsConstructor
public class VerificationToken{
    private static AuthenticationUtil util=new AuthenticationUtil();

    private static final int EXPIRATION_TIME=10;

    public VerificationToken(String token, UserEntity userEntity) {
        this.token = token;
        this.userEntity = userEntity;
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
