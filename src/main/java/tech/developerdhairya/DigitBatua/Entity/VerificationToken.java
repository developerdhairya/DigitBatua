package tech.developerdhairya.DigitBatua.Entity;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@DynamicUpdate
public class VerificationToken implements Token{
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
    @ToString.Exclude
    private AppUser appUser;

    @CreationTimestamp
    private Timestamp creationTimestamp = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;



}
