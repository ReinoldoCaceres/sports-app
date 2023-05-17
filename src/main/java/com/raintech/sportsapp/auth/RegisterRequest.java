package com.raintech.sportsapp.auth;

import com.raintech.sportsapp.university.Campus;
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


    private int userId;

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private Role role;
}
