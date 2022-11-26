package tech.developerdhairya.DigitBatua.Entity;


import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    @Column(nullable = false)
    @NotBlank
    private String type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_wallet_id", nullable = false)
    private Wallet from;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_wallet_id", nullable = false)
    private Wallet to;

    @Min(message = "Minimum Transaction amount should be Rs.1", value = 1)
    @Column(nullable = false)
    private Integer amount;

    @NotBlank
    private String status;


}
