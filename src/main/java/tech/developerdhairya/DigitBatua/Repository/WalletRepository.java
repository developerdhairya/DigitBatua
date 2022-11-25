package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Wallet;

import javax.validation.constraints.Email;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

//    @Query("select w from Wallet w where w.appUser = ?1")
//    public Wallet findByAppUser(AppUser appUser);

    @Query("select w from Wallet w where w.appUser.emailId = ?1")
    public Wallet findByAppUser_EmailId(@Email(message = "Invalid emailId") String appUser_emailId);

    
}