package com.raintech.sportsapp.auth;

import com.raintech.sportsapp.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {


    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private Role role;
}
