package com.example.demo.security;

import com.example.demo.filter.CustomAuthenticationFilter;
import com.example.demo.filter.CustomAuthorizationFilter;
import com.example.demo.repo.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class  config  extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ClientRepository clientRepository;

    @Autowired
    CustomSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");*/
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers("/registration/**").permitAll();

        /*http.authorizeRequests().antMatchers(GET,"/api/user/**").hasAnyAuthority("CLIENT") ;
        http.authorizeRequests().antMatchers(POST,"api/user/save/**").hasAnyAuthority("ADMIN") ;*/

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(),clientRepository));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        /*http.authorizeRequests().antMatchers("/login")
                .permitAll().and().formLogin().defaultSuccessUrl("/redirect",true)
                .successHandler(new CustomSuccessHandler(clientRepository));*/

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws  Exception{
        return  super.authenticationManagerBean();
    }
}
