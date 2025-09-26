package samwells.io.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import samwells.io.calendar.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserService userService) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.anonymous(Customizer.withDefaults());

        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers(
                    "/api/v1/auth/signup",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-resources/**",
                    "/webjars/**"
            ).permitAll();
            auth.anyRequest().authenticated();
        });

        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.userDetailsService(userService);

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
