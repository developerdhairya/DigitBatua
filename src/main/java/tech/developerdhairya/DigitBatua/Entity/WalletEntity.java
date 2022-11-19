package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
//@Table(name = "wallet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Max(message = "Max balance allowed is 10000000", value = 10000000)
    @Min(message = "Negative Balance not allowed", value = 0)
    @Column(nullable = false)
    private Integer balance;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @Fetch(FetchMode.JOIN)
//    private AppUserEntity appUserEntity;


    @Column(nullable = false)
    private boolean isActivated;

    @CreationTimestamp
    private Timestamp creationTimestamp;

    @UpdateTimestamp
    private Timestamp updateTimestamp;

}