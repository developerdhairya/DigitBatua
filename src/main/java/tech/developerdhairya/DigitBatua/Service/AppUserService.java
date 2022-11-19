package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUserEntity;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;

@Service
public class AppUserService {

    @Autowired
    AppUserRepository userRepository;

    public AppUserEntity register(RegisterUserDTO DTO){
        AppUserEntity entity=new AppUserEntity();
        entity.setFirstName(DTO.getFirstName());
        entity.setLastName(DTO.getFirstName());
        entity.setEmailId(DTO.getEmailId());
        entity.setHashedPassword("");
        entity.setMobileNumber(DTO.getMobileNumber());
        entity.setVerified(false);
        entity.setRole("user");
        System.out.println(entity.toString());

        AppUserEntity response=userRepository.save(entity);
        return response;
    }


}
