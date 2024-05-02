package com.rra.meetingRoomMgt.Config;

import com.rra.meetingRoomMgt.Controller.ErrorResponse;
import com.rra.meetingRoomMgt.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/rra/v1/home/**").permitAll()
                        .requestMatchers("/rra/v1/admin/**").hasAuthority("admin")
                        .requestMatchers("/rra/v1/client/**").hasAuthority("user")
                        .anyRequest().permitAll()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .cors(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            handleAuthenticationEntryPoint(response, authException);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            handleAccessDenied(response, accessDeniedException);
                        })
                );
        return http.build();
    }


    private void handleAuthenticationEntryPoint(HttpServletResponse response, Exception authException) throws IOException {
        logError("Unauthorized access. Authorized access only.", (Exception) authException.getCause());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Unauthorized access. Authorized access only.");

        ResponseEntity<ErrorResponse> responseEntity =
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        respondWithJson(response, responseEntity);
    }

    private void handleAccessDenied(HttpServletResponse response, Exception accessDeniedException) throws IOException {
        logError("Access denied.", accessDeniedException);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Access denied.");

        ResponseEntity<ErrorResponse> responseEntity =
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

        respondWithJson(response, responseEntity);
    }

    private void respondWithJson(HttpServletResponse response, ResponseEntity<ErrorResponse> responseEntity) throws IOException {
        response.setContentType("application/json");
        response.setStatus(responseEntity.getStatusCodeValue());
        response.getWriter().write(responseEntity.getBody().toString());
    }

    private void logError(String message, Exception exception) {
        // Log the error
        log.error(message, exception);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
