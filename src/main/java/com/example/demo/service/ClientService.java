package com.example.demo.service;


import com.example.demo.domain.Client;

import java.util.List;

public interface ClientService {
    Client saveClient(Client client);

    void addRoleToUser(String username, String roleName);

    Client getClient(String username);

    List<Client> getClients();
}
