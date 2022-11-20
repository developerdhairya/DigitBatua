package tech.developerdhairya.DigitBatua.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("admin", "$2a$11$Fiblmuqt1j7oWWiBOx23h.w3D6Xt.AsYYosB7Dr2HW8oapOaSceR6", new ArrayList<>());
    }
}
