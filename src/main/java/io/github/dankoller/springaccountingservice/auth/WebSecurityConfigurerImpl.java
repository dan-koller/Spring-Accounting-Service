package io.github.dankoller.springaccountingservice.auth;

import io.github.dankoller.springaccountingservice.config.RestAccessDeniedHandler;
import io.github.dankoller.springaccountingservice.entity.Role;
import io.github.dankoller.springaccountingservice.entity.UserDetailsServiceImpl;
import io.github.dankoller.springaccountingservice.service.AuditorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private AccessDeniedHandler accessDeniedHandler;
    private UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfigurerImpl(RestAuthenticationEntryPoint restAuthenticationEntryPoint, AccessDeniedHandler accessDeniedHandler, UserDetailsServiceImpl userDetailsService) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .mvcMatchers("/h2-console/**").permitAll() // Allow H2-console (Do NOT use this in a productive environment!)
                .mvcMatchers("/actuator/**", "/h2/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "api/acct/payments").hasAuthority(Role.ACCOUNTANT.getAuthority())
                .mvcMatchers(HttpMethod.PUT, "api/acct/payments").permitAll()
                .mvcMatchers(HttpMethod.PUT, "api/admin/user/role", "api/admin/user/access").hasAuthority(Role.ADMINISTRATOR.getAuthority())
                .mvcMatchers(HttpMethod.GET, "api/admin/user").hasAuthority(Role.ADMINISTRATOR.getAuthority())
                .mvcMatchers(HttpMethod.DELETE, "api/admin/**").hasAuthority(Role.ADMINISTRATOR.getAuthority())
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass").authenticated()
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment").hasAuthority(Role.USER.getAuthority())
                .mvcMatchers(HttpMethod.GET, "/api/security/events").hasAuthority(Role.AUDITOR.getAuthority())
                .mvcMatchers("/api/security/**").permitAll()
                .mvcMatchers("**").denyAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint) // Handle 401
                .accessDeniedHandler(accessDeniedHandler)  // Handle 403
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no session
    }

    @Bean
    public static PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public static AccessDeniedHandler getAccessDeniedHandler(AuditorService auditorService) {
        return new RestAccessDeniedHandler(auditorService);
    }

    @Bean
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
