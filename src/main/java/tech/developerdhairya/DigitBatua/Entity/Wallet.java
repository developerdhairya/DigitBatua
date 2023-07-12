package tech.developerdhairya.DigitBatua.Entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class Wallet {
    @Id
    @GeneratedValue(generator = "UUiD")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "wallet_id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID walletId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;


    @Min(message = "Negative Balance not allowed", value = 0)
    @Column(nullable = false)
    private Integer balance=0;

    @CreationTimestamp
    private Timestamp creationTimestamp;

    @UpdateTimestamp
    private Timestamp updateTimestamp;

}