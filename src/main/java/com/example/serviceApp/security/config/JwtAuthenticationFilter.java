package com.example.serviceApp.security.config;

import com.example.serviceApp.UserDetailsServiceImplementation;
import com.example.serviceApp.customer.CustomerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceImplementation userDetailsService;
    private final CustomerUserDetailsService customerUserDetailsService;
    @Override

    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        final String jwt;
        final String email;
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(jwt);
        String userType = jwtService.extractUserType(jwt); // this method should extract the userType claim from the JWT

        if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails;
            log.info(userType);
            if ("customer".equals(userType)) {
                userDetails = customerUserDetailsService.loadUserByUsername(email);
            } else {
                userDetails = userDetailsService.loadUserByUsername(email);
            }

            if(jwtService.isTokenValid(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info(SecurityContextHolder.getContext().getAuthentication().toString()); //todo to jest testwowo w logach!!!!
            }
        }
        log.info(SecurityContextHolder.getContext().getAuthentication().getName()+ "to jest name");
        log.info(SecurityContextHolder.getContext().getAuthentication().toString());//todo to jest testwowo w logach!!!!
        filterChain.doFilter(request,response);

    }
}
