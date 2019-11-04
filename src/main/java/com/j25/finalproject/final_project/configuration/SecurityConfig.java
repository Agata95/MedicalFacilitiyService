package com.j25.finalproject.final_project.configuration;

import com.j25.finalproject.final_project.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    public SecurityConfig(boolean disableDefaults, AuthenticationService authenticationService, PasswordEncoder passwordEncoder) {
        super(disableDefaults);
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        zmodyfikowana reguła bezpieczeństwa
//        do tej pory wszystko było dla klienta
//        teraz tylko po podanych url wszyscy mają dostęp
        http.csrf().disable()
//                rozpoczynamy definicję reguł bezpieczeństwa:
                .authorizeRequests()
                .antMatchers("/",
                        "/css/**",
                        "/js/**",
                        "/webjars/**",
                        "/user/register",
                        "/login").permitAll()
//                reguły związane z rolami (np. /admin/**)
                .anyRequest().authenticated()
//        .anyRequest().authenticated() oznacza, że dla każdej innej strony ma pytać o login i hasło
//                .antMatchers("/user/register").hasAnyRole("ADMIN", "PATIENT")
                .and()
                    .formLogin()
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(authenticationService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        auth.authenticationProvider(daoAuthenticationProvider);
    }
}
