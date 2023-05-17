package com.raintech.sportsapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    // The doFilterInternal function is responsible for authenticating the user based on the JWT token.
    // This function is part of the OncePerRequestFilter, which ensures that it only runs once per request.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Get the Authorization header from the HTTP request.
        final String autHeader = request.getHeader("Authorization");
        // Initialize variables for the JWT token and username.
        final String jwtToken;
        final String username;

        // Check if the Authorization header is null or does not start with "Bearer ".
        if (autHeader == null || !autHeader.startsWith("Bearer ")) {
            // If so, pass the request and response to the next filter in the chain.
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header.
        jwtToken = autHeader.substring(7);
        // Extract the username from the JWT token using the JwtService.
        username = jwtService.extractUsername(jwtToken);
        //It checks if the extracted username is not null and if there is no existing authentication in the SecurityContextHolder.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //If the conditions are met, it retrieves the user details from the userDetailsService based on the extracted username.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            //It checks if the JWT token is valid for the user details using the jwtService.
            if (jwtService.isTokenValid(jwtToken,userDetails)) {
                //If the token is valid, it creates an UsernamePasswordAuthenticationToken with the user details and sets it as the authentication in the SecurityContextHolder.
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
        filterChain.doFilter(request, response);
    }
}
