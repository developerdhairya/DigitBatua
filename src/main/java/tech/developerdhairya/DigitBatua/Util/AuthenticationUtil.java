package tech.developerdhairya.DigitBatua.Util;

import org.springframework.stereotype.Component;
import tech.developerdhairya.DigitBatua.Entity.Token;
import tech.developerdhairya.DigitBatua.Entity.VerificationToken;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Component
public class AuthenticationUtil {


    /*
    public String getApplicationUrl(HttpServletRequest httpServletRequest){
        System.out.println(httpServletRequest.getServletPath().toString());;
        return "http://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort()+"/"+httpServletRequest.getContextPath();
    }
    */

    public static Instant calculateExpirationTime(int additiveMinutes){
        Instant instant= Instant.now();
        return instant.plus(additiveMinutes, ChronoUnit.MINUTES);

        /* Legacy Code(Caused timezone issue on ec2)
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());       //Setting calender time
        calendar.add(Calendar.MINUTE,expirationTime);        //Adding EXPIRATION_TIME to total time
        return calendar.getTime();
        */
    }

    public static boolean checkTokenExpiry(Token token){

        Instant currentInstant= Instant.now();
        Instant expirationInstant=token.getExpirationTime();
        System.out.println(currentInstant+"  "+expirationInstant);
        return expirationInstant.compareTo(currentInstant) <= 0;

        /* Legacy Code(Caused timezone issue on ec2)
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());       //Setting calender time
        calendar.add(Calendar.MINUTE,0);              // To prevent timezone error
        System.out.println(token.getExpirationTime()+"     "+calendar.getTime());
        return token.getExpirationTime().getTime()<calendar.getTime().getTime();
        */

    }

}
