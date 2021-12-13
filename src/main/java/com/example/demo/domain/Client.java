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
public class Client  extends  AppUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double balance;


    public Client(String firstName, String lastName, String email, String username, String password, UserRole userRole, Double balance) {
        super(firstName, lastName, email, username, password, userRole);
        this.balance = balance;
    }



}
