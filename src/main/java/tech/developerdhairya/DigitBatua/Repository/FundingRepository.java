package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.Funding;

import java.util.List;
import java.util.UUID;

@Repository
public interface FundingRepository extends PagingAndSortingRepository<Funding, UUID> {


    @Query("select f from Funding f where f.wallet.appUser.emailId = ?1")
    List<Funding> findAllByWallet_AppUser_EmailId(String emailId, Pageable pageable);

}