package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.ForgotPasswordToken;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken,Long> {

    ForgotPasswordToken findByToken(String token);

    ForgotPasswordToken findByAppUser(AppUser appUser);
}
