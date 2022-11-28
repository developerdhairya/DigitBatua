package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.MoneyRequest;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoneyRequestRepository extends PagingAndSortingRepository<MoneyRequest, UUID> {

    @Query("select m from MoneyRequest m where m.requestReceiverWallet.appUser.emailId = ?1")
    public List<MoneyRequest> findAllByRequestReceiverWallet_AppUser_EmailId(String emailId, Pageable pageable);

    @Query("select m from MoneyRequest m where m.requesterWallet.appUser.emailId = ?1")
    public List<MoneyRequest> findAllByRequesterWallet_AppUser_EmailId(String emailId, Pageable pageable);


}