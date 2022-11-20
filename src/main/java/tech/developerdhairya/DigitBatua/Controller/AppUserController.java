package tech.developerdhairya.DigitBatua.Controller;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import tech.developerdhairya.DigitBatua.DTO.ChangePasswordDTO;
import tech.developerdhairya.DigitBatua.DTO.JWTRequest;
import tech.developerdhairya.DigitBatua.DTO.JWTResponse;
import tech.developerdhairya.DigitBatua.DTO.RegisterUserDTO;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;
import tech.developerdhairya.DigitBatua.Util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import tech.developerdhairya.DigitBatua.Service.AppUserService;
import tech.developerdhairya.DigitBatua.Service.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;


    @Autowired
    private AuthenticationUtil util;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/register")
    public boolean registerUser(@RequestBody RegisterUserDTO registerUserDTO, HttpServletRequest httpServletRequest) {
        try {
            AppUser user = appUserService.registerUser(registerUserDTO);
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }

    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        return appUserService.validateVerificationToken(token);
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("email") String email) {
        return appUserService.resendVerificationToken(email);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return appUserService.resetPassword(changePasswordDTO);
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {

            System.out.println(jwtRequest.getUsername()+jwtRequest.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtRequest.getUsername(),
                    jwtRequest.getPassword()
            );
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            System.out.println("Invalid credentials"+e);
            throw new Exception("Invalid Credentials", e);
        }

        final UserDetails userDetails
                = userDetailsServiceImpl.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtil.generateToken(userDetails);

        System.out.println("moo");
        return new JWTResponse(token);

    }
}
