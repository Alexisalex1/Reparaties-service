package nl.backend.reparatieservice.security;
import nl.backend.reparatieservice.repository.UserRepository;
import nl.backend.reparatieservice.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService service, UserRepository userRepository) {
        this.jwtService = service;
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder, UserDetailsService udService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(udService)
                .passwordEncoder(encoder)
                .and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .httpBasic().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/auth").permitAll()

                //customer
                .requestMatchers(HttpMethod.POST, "/customers").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/customers").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/customers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/customers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/customers/**").hasAnyRole("USER", "ADMIN")


                //invoices
                .requestMatchers(HttpMethod.POST, "/invoices/attach-to-request/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/invoices/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/invoices/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/invoices/**").hasRole("ADMIN")

                //repair items
                .requestMatchers(HttpMethod.POST, "/repair-items").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/repair-items").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/repair-items/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/repair-items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/repair-items/**").hasRole("ADMIN")

                //repair options
                .requestMatchers(HttpMethod.POST, "/repair-options").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/repair-options").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/repair-options/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/repair-options/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/repair-options/**").hasRole("ADMIN")


                //repair requests
                .requestMatchers(HttpMethod.POST, "/requests").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/requests").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/requests/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/requests/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/requests/**").hasAnyRole("USER", "ADMIN")

                //repair status

                .requestMatchers(HttpMethod.POST, "/repair-status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/repair-status/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/repair-status/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/repair-status/**").hasRole("ADMIN")

                //uploaded photo
                .requestMatchers(HttpMethod.POST, "/uploaded-photos").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/uploaded-photos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/uploaded-photos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/uploaded-photos/**").hasAnyRole("USER", "ADMIN")

                .requestMatchers("/**").authenticated()
                .and()
                .addFilterBefore(new JwtRequestFilter(jwtService, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
