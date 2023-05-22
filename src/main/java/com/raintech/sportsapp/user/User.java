package com.raintech.sportsapp.user;

import com.raintech.sportsapp.university.Campus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
@Data
@DynamicInsert
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private int userId;

    @Column(name = "Username", unique = true)
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "First_Name")
    private String firstName;

    @Column(name = "Last_Name")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Campus_ID")
    private Campus campus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify this according to your business logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify this according to your business logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify this according to your business logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Modify this according to your business logic
    }


}
