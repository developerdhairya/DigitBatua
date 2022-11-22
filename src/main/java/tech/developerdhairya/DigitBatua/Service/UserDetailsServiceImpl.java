package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Repository.AppUserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user=userRepository.findByEmailId(email);
        if(user==null){
            throw new UsernameNotFoundException("User not Found");
        };
        return new User(email, user.getHashedPassword(), new ArrayList<>());
    }
}
