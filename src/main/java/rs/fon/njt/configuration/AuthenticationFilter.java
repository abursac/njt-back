package rs.fon.njt.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.fon.njt.models.Teacher;
import rs.fon.njt.repository.TeachersRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TeachersRepository teachersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || header.isEmpty() || !header.startsWith("Bearer ")){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            SecurityContextHolder.getContext().setAuthentication(null);
            return;
        }

        final String token = header.split(" ")[1].trim();
        DecodedJWT decodedToken = JwtTokenUtil.verifyToken(token);
        if(decodedToken == null)
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            SecurityContextHolder.getContext().setAuthentication(null);
            return;
        }
        String username = decodedToken.getSubject();

        Optional<Teacher> teacher = teachersRepository.getTeacherByUsername(username);
        if(!teacher.isPresent())
            throw new UsernameNotFoundException(username);
        UserDetails userDetails = new User(teacher.get().getUsername(), teacher.get().getPassword(), Collections.emptyList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null? Collections.emptyList() : userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
