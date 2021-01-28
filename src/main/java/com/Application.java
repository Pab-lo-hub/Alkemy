package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initData(ProfessorsRepository professorsRepository, RegistrationRepository registrationRepository, SubjectsRepository subjectsRepository, UsersRepository usersRepository) {
        return (args) -> {
            Users user1 = new Users("15151151",  passwordEncoder.encode("45896"), "user");
            usersRepository.save(user1);

            Users admin1 = new Users("admin",  passwordEncoder.encode("24"), "admin");
            usersRepository.save(admin1);

            Users user2 = new Users("100",  passwordEncoder.encode("100"), "user");
            usersRepository.save(user2);

            Professors professor1 = new Professors(28282288, "Federico", "Pico", true);
            professorsRepository.save(professor1);

            Professors professor2 = new Professors(28999988, "John", "Keating", true);
            professorsRepository.save(professor2);

            Professors professor3 = new Professors(68686868, "Xavi", "Hernández", false);
            professorsRepository.save(professor3);

            Subjects subject1 = new Subjects("Historia", LocalDateTime.of(2020, 2, 10, 9, 30), "Historia mundial del siglo XX. Historia Argentina contemporánea.", 30);
            subjectsRepository.save(subject1);

            Subjects subject2 = new Subjects("Matemática", LocalDateTime.of(2020, 2, 15, 9, 30), "Funciones. Introducción al álgebra. Matemática aplicada", 30);
            subjectsRepository.save(subject2);

            Subjects subject3 = new Subjects("Computación", LocalDateTime.of(2020, 2, 15, 9, 30), "Revisión de hardware. Introducción a la programación",30);
            subjectsRepository.save(subject3);

            Subjects subject4 = new Subjects("Economía", LocalDateTime.of(2020, 2, 15, 9, 30), "Análisis Financiero. Economía política. Economía Mundial",15);
            subjectsRepository.save(subject4);

            Registration registration1 = new Registration(user1, subject2);
            registrationRepository.save(registration1);

            Registration registration2 = new Registration(user2, subject2);
            registrationRepository.save(registration2);
        };
    }

    //Login and security//
    @EnableWebSecurity
    @Configuration
    class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        UsersRepository usersRepository;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(inputName -> {
                Users user = usersRepository.findByDni(inputName);
                if (user != null) {
                                return new User(user.getDni(), user.getLegajo(),
                                        AuthorityUtils.createAuthorityList(user.getRol()));
                    } else {
                    throw new UsernameNotFoundException("Unknown user: " + inputName);
                }
            });
        }
    }

    @Configuration
    @EnableWebSecurity
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/api/admin").hasAuthority("ADMIN")
                    .antMatchers("/api").hasAuthority("USER")
                    .and()
                    .formLogin()
                    .usernameParameter("dni")
                    .passwordParameter("legajo")
                    .loginPage("/api/login");

            http.headers().frameOptions().disable();
            http.logout().logoutUrl("/api/logout");
            // turn off checking for CSRF tokens
            http.csrf().disable();
            // if user is not authenticated, just send an authentication failure response
            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
            // if login is successful, just clear the flags asking for authentication
            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
            // if login fails, just send an authentication failure response
            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
            // if logout is successful, just send a success response
            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        }

        private void clearAuthenticationAttributes(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
    }
}