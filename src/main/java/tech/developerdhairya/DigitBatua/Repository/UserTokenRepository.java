package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends PagingAndSortingRepository<UserToken, UUID> {
    
    ArrayList<UserToken> findUserTokenByAppUser_EmailId(String appUser_emailId);

}