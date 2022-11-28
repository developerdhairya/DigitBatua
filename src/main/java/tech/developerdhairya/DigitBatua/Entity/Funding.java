package tech.developerdhairya.DigitBatua.Entity;


import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.URL;
import tech.developerdhairya.DigitBatua.Enum.FundingStatus;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@DynamicUpdate
public class Funding {

    @Id
    @Column(name = "funding_id", columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID fundingId;


    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Min(message = "Minimum Transaction amount should be Rs.1", value = 100)
    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String rzpPaymentId;

    @NotNull
    @URL
    private String rzpPaymentLink;

    @NotBlank @NotNull
    private String status= FundingStatus.Pending.name();

    @CreationTimestamp
    private Timestamp creationTimestamp = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    private Timestamp updateTimestamp;

}
