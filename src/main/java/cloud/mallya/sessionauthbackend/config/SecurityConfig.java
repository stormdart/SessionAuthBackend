package cloud.mallya.sessionauthbackend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/csrf")
                        .permitAll()
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(
                        (_, response, _) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .formLogin(form -> form.loginProcessingUrl("/login")
                        .successHandler(((_, response, _) -> response.setStatus(HttpServletResponse.SC_OK)))
                        .failureHandler(((_, response, _) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessHandler(((_, response, _) -> response.setStatus(HttpServletResponse.SC_OK))));

        return httpSecurity.build();
    }

    @Bean
    CommandLineRunner createInitialUser(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        return _ -> {
            if (!userDetailsManager.userExists("user")) {
                userDetailsManager.createUser(User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER")
                        .build());
            }

            if (!userDetailsManager.userExists("admin")) {
                userDetailsManager.createUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("password"))
                        .roles("USER", "ADMIN")
                        .build());
            }
        };
    }
}
