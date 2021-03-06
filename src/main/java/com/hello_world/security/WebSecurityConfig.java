package com.hello_world.security;

import com.hello_world.service.impl.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImp();
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/signin")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> {
                    res.sendRedirect("/accessDefine");
                })
                .failureHandler((req, res, exp) -> {
                    String error = "";
                    if (exp.getClass().isAssignableFrom(BadCredentialsException.class)) {
                        error = "Неверный логин или пароль.";
                    } else {
                        error = "Unknown error - " + exp.getMessage();
                    }
                    req.getSession().setAttribute("loginMessage", error);
                    res.sendRedirect("/");
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, res, auth) -> {
                    req.getSession().setAttribute("loginMessage", "Вы успешно вышли из системы.");
                    res.sendRedirect("/");
                })
                .and()
                .csrf().disable();
    }
}
