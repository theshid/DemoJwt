package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.domain.Client;
import com.example.demo.repo.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    private final ClientRepository clientRepository;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Client client = clientRepository.findByUsername(user.getUsername());



        //String redirectUrl = null;

        //Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        //og.info("size of grant authority"+authorities.size());
        /*System.out.println("role " + client.getUserRole().name());
        if (client.getUserRole().name().equals("CLIENT")) {
            redirectUrl = "/userDashboard";

        } else if (client.getUserRole().name().equals("ADMIN")) {
            redirectUrl = "/adminDashboard";

        }*/
        /*for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println("role " + grantedAuthority.getAuthority());
            if (grantedAuthority.getAuthority().equals("CLIENT")) {
                redirectUrl = "/userDashboard";
                break;
            } else if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/adminDashboard";
                break;
            }
        }*/
        /*System.out.println("redirectUrl " + redirectUrl);
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        log.info("response value:"+response.getOutputStream().toString());
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);*/


        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        Date expiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 1000);
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiryDate)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        log.info("This is the username:" + user.getUsername());


        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("expiry_date", expiryDate.toString());
        tokens.put("client_id", client.getId().toString());
        tokens.put("first_name", client.getFirstName());
        tokens.put("last_name", client.getLastName());
        tokens.put("username", client.getUsername());
        tokens.put("email", client.getEmail());
        tokens.put("user_role", client.getUserRole().name());
        tokens.put("balance", client.getBalance().toString());
        //tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
