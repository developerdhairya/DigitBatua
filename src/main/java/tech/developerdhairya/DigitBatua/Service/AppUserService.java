package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AppUserService {

    @Autowired
    AppUserRepository userRepository;

    public AppUser register(RegisterUserDTO DTO){
        AppUser entity=new AppUser();
        entity.setFirstName(DTO.getFirstName());
        entity.setLastName(DTO.getFirstName());
        entity.setEmailId(DTO.getEmailId());
        entity.setHashedPassword("fgygdf6h65urtfutdjud");
        entity.setMobileNumber(DTO.getMobileNumber());
        entity.setUpdateTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        return userRepository.save(entity);
    }



}
