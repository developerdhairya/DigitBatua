package tech.developerdhairya.DigitBatua.Entity;


import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
//@Table(name = "transaction")
@ToString
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String type;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "p_1_id", nullable = false)
//    private AppUserEntity p1;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "p_2_id", nullable = false)
//    private AppUserEntity p2;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String paymentId;

    @NotNull
    @URL
    private String paymentLink;

    @NotBlank
    private String status;


}
