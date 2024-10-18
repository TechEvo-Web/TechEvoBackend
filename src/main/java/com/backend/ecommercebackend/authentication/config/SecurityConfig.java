package com.backend.ecommercebackend.authentication.config;

import com.backend.ecommercebackend.authentication.service.CustomOauth2UserService;
import com.backend.ecommercebackend.exception.CustomAccessDeniedHandler;
import com.backend.ecommercebackend.exception.CustomAuthenticationEntryPoint;
import com.backend.ecommercebackend.authentication.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter filter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomOauth2UserService customOAuth2UserService;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("523070151170-f80npg62nl1rapmi86m2f2c2efgthibi.apps.googleusercontent.com")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/google")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                  .csrf(AbstractHttpConfigurer::disable)
                 .exceptionHandling(exception ->
                         exception.accessDeniedHandler(accessDeniedHandler)
                                 .authenticationEntryPoint(authenticationEntryPoint)
                 )
                   .authorizeHttpRequests( auth ->
                    auth
                            .requestMatchers("/api/v1/auth/**","/api/v1/product/**","/api/v1/user/**").permitAll()
                            .requestMatchers("/api/v1/product/comment/**").authenticated()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                            .anyRequest().authenticated()
                )

                 .oauth2Login(oauth2 -> oauth2
                         .loginPage("/login")
                         .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                         .successHandler((request, response, authentication) -> {
                             response.sendRedirect("/home");
                         })
                         .failureHandler((request, response, exception) -> {
                             response.sendRedirect("/login?error=true");
                         })
                 )

                 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                 .authenticationProvider(authenticationProvider)
                 .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         return http.build();
    }

}
