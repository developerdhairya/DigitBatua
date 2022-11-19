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
public class Funding {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID fundingId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Min(message = "Minimum Transaction amount should be Rs.1", value = 1)
    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String rzpPaymentId;

    @NotNull
    @URL
    private String rzpPaymentLink;

    @NotBlank
    private String status;


}
