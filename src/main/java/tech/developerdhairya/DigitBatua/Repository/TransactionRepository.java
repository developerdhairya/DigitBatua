package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.Transaction;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, UUID> {



    public List<Transaction> getAllByTo_AppUser_EmailId_OrFrom_AppUser_EmailId(String emailId1,String emailId2,Pageable pageable);

    public List<Transaction> getAllByTo_AppUser_EmailId_OrFrom_AppUser_EmailIdAndType(String emailId1,String emailId2,String type,Pageable pageable);




}