package tech.developerdhairya.DigitBatua;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import tech.developerdhairya.DigitBatua.Util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.developerdhairya.DigitBatua.Service.UserDetailsServiceImpl;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext context= SecurityContextHolder.getContext();
        String authHeader=request.getHeader("Authorization");
        String jwt=null;
        String username=null;
        if(authHeader!=null && authHeader.startsWith("Bearer")){
            jwt=authHeader.substring(7);
            username=jwtUtil.getUsernameFromToken(jwt);
            System.out.println(username);
        }

        if(context.getAuthentication()==null && username!=null){
            UserDetails userDetails= userDetailsServiceImpl.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt,userDetails)){
                System.out.println(105);
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //Converting HTTPServlet(Java Class) to WebAuthenticationDetails (internal spring class)
                WebAuthenticationDetails details=new WebAuthenticationDetailsSource().buildDetails(request);
                authToken.setDetails(details);
                context.setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request,response);  // same as next() node.js
    }
}
