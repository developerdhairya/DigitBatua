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
    private Wallet p1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_wallet_id", nullable = false)
    private AppUser p2;

    @Min(message = "Minimum Transaction amount should be Rs.1", value = 1)
    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String paymentId;

    @NotNull
    @URL
    private String paymentLink;

    @NotBlank
    private String status;


}
