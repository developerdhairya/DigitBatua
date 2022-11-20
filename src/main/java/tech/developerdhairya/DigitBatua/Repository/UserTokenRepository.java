package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.UserToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends PagingAndSortingRepository<UserToken, UUID> {
    
    ArrayList<UserToken> findUserTokenByAppUser_EmailId(String appUser_emailId);

}