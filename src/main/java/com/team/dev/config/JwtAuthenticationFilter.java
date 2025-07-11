package com.team.dev.config;

import com.team.dev.utils.ExtendedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private  final  JwtService jwtService;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
    final  String authHeader = request.getHeader("Authorization");
    System.out.println("Authorization header : " + authHeader);
    final String jwt;
    final String userEmail;
    String entrepriseId = null;

    if(authHeader == null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request, response);
        return;
    }
    jwt = authHeader.substring(7);
    userEmail = (String) jwtService.extractUsername(jwt);
    entrepriseId = String.valueOf(jwtService.extractIdEntreprise(jwt));

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.tokenValid(jwt, userDetails)){
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                     null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    MDC.put("entrepriseId", entrepriseId);
    filterChain.doFilter(request, response);
    }
}
