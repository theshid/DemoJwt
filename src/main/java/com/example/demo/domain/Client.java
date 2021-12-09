package com.example.demo.domain;


import com.example.demo.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "client")
public class Client  implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private Double balance;

    public Client(String firstName, String lastName, String username, String email, String password, UserRole userRole, Double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.balance = balance;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(getUserRole().name());
        return Collections.singleton(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
