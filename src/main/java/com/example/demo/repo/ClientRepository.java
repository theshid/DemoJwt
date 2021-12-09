package com.example.demo.repo;


import com.example.demo.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByUsername(String username);
    Client findClientByEmail(String email);
    Optional<Client> findByEmail(String email);
}