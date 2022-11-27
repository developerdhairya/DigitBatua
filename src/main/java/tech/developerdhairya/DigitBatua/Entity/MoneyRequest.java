package tech.developerdhairya.DigitBatua.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MoneyRequest {

    @Id
    @GeneratedValue(generator = "UUiD")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "money_request_id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID moneyRequestId;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_wallet_id", nullable = false)
    private Wallet requesterWallet;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    @JoinColumn(name = "request_receiver_wallet_id", nullable = false)
    private Wallet requestReceiverWallet;

    @Min(message = "Minimum Transaction amount should be Rs.1", value = 100)
    @Column(nullable = false)
    private Integer amount;

    @NotBlank @NotNull
    private String status="Pending";

    private String message;


}