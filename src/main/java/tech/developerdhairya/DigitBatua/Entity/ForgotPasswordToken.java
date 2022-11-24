package tech.developerdhairya.DigitBatua.Entity;

import lombok.Data;

import javax.persistence.*;

@Data @Entity
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser userId;

}

