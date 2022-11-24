package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Entity.Wallet;
import tech.developerdhairya.DigitBatua.Exception.BadRequestException;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;
import tech.developerdhairya.DigitBatua.Repository.WalletRepository;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public Wallet activateWallet(String emailId) throws BadRequestException {
        AppUser appUser=appUserRepository.findByEmailId(emailId);
        Wallet wallet=walletRepository.findByAppUser(appUser);
        if(wallet!=null){
            throw new BadRequestException("Wallet Already Activated for the given user");
        }
        Wallet wallet1=new Wallet();
        wallet1.setAppUser(appUser);
        return walletRepository.save(wallet1);
    }

}
