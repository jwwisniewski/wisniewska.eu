package eu.wisniewska.www.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";
    @Value("${app.admin.remember-me-key}")
    private String rememberMeKey;
    @Value("${app.admin.remember-me-cookie-name}")
    private String rememberMeCookieName;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(
                        csrf -> csrf.ignoringRequestMatchers("/h2-console/**")
                )
                .headers(
                        headers -> headers
                                .frameOptions(
                                        HeadersConfigurer.FrameOptionsConfig::disable
                                )
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/_admin/login")
                                .loginProcessingUrl("/_admin/login")
                                .defaultSuccessUrl("/_admin")
                )
                .rememberMe(
                        rememberMe -> rememberMe
                                .key(rememberMeKey)
                                .rememberMeCookieName(rememberMeCookieName)
                                .tokenValiditySeconds(86400 * 7)
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/_admin/login?logout")
                )
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/_admin/login").permitAll()
                                .requestMatchers("/_admin/**").hasRole(ROLE_ADMIN)
                                .anyRequest().permitAll()
                )
                .build()
                ;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/img/**");
    }
}
