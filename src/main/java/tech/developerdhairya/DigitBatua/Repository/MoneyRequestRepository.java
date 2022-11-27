package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.MoneyRequest;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoneyRequestRepository extends JpaRepository<MoneyRequest, UUID> {

    @Query("select m from MoneyRequest m where m.requestReceiverWallet.appUser.emailId = ?1")
    public List<MoneyRequest> findAllByRequestReceiverWallet_AppUser_EmailId(String emailId);

    @Query("select m from MoneyRequest m where m.requesterWallet.appUser.emailId = ?1")
    public List<MoneyRequest> findAllByRequesterWallet_AppUser_EmailId(String emailId);


}