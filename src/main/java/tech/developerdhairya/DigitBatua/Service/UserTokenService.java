package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Token;
import tech.developerdhairya.DigitBatua.Entity.UserToken;
import tech.developerdhairya.DigitBatua.Repository.UserTokenRepository;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken generateVerificationToken(AppUser appUser){
        UserToken token=new UserToken();
        token.setAppUser(appUser);
        token.setType(Token.verification.toString());
        token.setToken(UUID.randomUUID().toString());
        token.setExpirationTime(new Timestamp(System.currentTimeMillis()+600000));
        UserToken response=userTokenRepository.save(token);
        return response;
    }

    public int getTokenByUser(String email,Token type){
        ArrayList<UserToken> list=userTokenRepository.findUserTokenByAppUser_EmailId(email);
        return list.size();
    }

}
