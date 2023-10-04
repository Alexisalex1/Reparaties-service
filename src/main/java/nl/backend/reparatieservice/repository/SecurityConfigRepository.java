package nl.backend.reparatieservice.repository;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityConfigRepository {
    void configure(HttpSecurity http) throws Exception;
}
