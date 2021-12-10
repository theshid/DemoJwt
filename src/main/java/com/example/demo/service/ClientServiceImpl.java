package com.example.demo.service;


import com.example.demo.UserRole;

import com.example.demo.domain.Client;

import com.example.demo.repo.ClientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService, UserDetailsService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String signUpClient(Client client) {
        boolean emailExist = clientRepository.findByEmail(client.getEmail())
                .isPresent();
        boolean usernameExist = clientRepository.findClientByUsername(client.getUsername()).isPresent();
        if (emailExist) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use.");
           // throw new IllegalStateException("email already in use.");
        } else if (usernameExist){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use.");
        }


        String encodePassword = bCryptPasswordEncoder.encode(client.getPassword());

        client.setPassword(encodePassword);
        clientRepository.save(client);


        String token = UUID.randomUUID().toString();
        /*ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );*/
        //confirmationTokenService.saveConfirmationToken(confirmationToken);

        //Todo send email
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findClientByEmail(email);
        if (client==null) {
            log.error("User not found in the DB");
            throw new UsernameNotFoundException("User not found int the DB");
        } else {
            log.info("User found in the DC: {}", email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.CLIENT.name()));
        return new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(), authorities);
    }


    @Override
    public Client saveClient(Client client) {
        log.info("Saving new user to the DB", client.getUsername());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.save(client);
    }


    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        Client client = clientRepository.findByUsername(username);
        //Role role = roleRepo.findByName(roleName);
        client.setUserRole(UserRole.CLIENT);
    }

    @Override
    public Client getClient(String username) {
        log.info("Fetching user {}", username);
        return clientRepository.findByUsername(username);
    }

    @Override
    public List<Client> getClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }

}
