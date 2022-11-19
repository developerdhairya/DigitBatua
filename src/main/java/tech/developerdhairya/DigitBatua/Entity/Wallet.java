package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID walletId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @Max(message = "Max balance allowed is 10000000", value = 10000000)
    @Min(message = "Negative Balance not allowed", value = 0)
    @Column(nullable = false)
    private Integer balance;

    @Column(nullable = false)
    private boolean isActivated;

    @CreationTimestamp
    private Timestamp creationTimestamp;

    @UpdateTimestamp
    private Timestamp updateTimestamp;

}