package at.carcar.carcarbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.logging.Filter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig{

    private final VerifyUserFilter verifyUserFilter;

    // Constructor injection for VerifyUserFilter
    public SecurityConfig(VerifyUserFilter verifyUserFilter) {
        this.verifyUserFilter = verifyUserFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(verifyUserFilter, UsernamePasswordAuthenticationFilter.class)  // Add your custom filter
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests//.requestMatchers("/users/addUser", "/users/getUser").permitAll()
                .requestMatchers("/users/register", "/users/login").permitAll()
                .anyRequest().authenticated());  // Require authentication for any other request
        return http.build();
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.httpBasic(withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests//.requestMatchers("/users/addUser", "/users/getUser").permitAll()
                        //.anyRequest().permitAll());
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }*/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

