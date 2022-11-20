package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.UserToken;
import tech.developerdhairya.DigitBatua.Repository.UserTokenRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken generateVerificationToken(AppUser appUser){
        UserToken token=new UserToken();
        token.setAppUser(appUser);
        token.setType("verification");
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationTime(new Timestamp(System.currentTimeMillis()+600000));
        UserToken response=userTokenRepository.save(token);
        return response;

    }

}
