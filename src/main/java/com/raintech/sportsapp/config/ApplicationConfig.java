package com.raintech.sportsapp.config;

import com.raintech.sportsapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    // Bean definition for the UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        // Lambda expression defining the implementation of the UserDetailsService interface
        return (String username) -> repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

        /**
         Inside the userDetailsService() method, a lambda expression is used to define the implementation of the UserDetailsService interface.
         The lambda expression takes a String parameter username and uses it to call the findByUsername() method on the repository object.
         If the user is found, it returns the user details; otherwise, it throws a UsernameNotFoundException.
         */
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
