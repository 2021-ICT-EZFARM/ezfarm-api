package com.ezfarm.ezfarmback.config.security;

import com.ezfarm.ezfarmback.alert.service.AlertService;
import com.ezfarm.ezfarmback.security.filter.LoginFilter;
import com.ezfarm.ezfarmback.security.filter.TokenAuthenticationFilter;
import com.ezfarm.ezfarmback.security.handler.LoginSuccessHandler;
import com.ezfarm.ezfarmback.security.local.CustomAuthenticationEntryPoint;
import com.ezfarm.ezfarmback.security.local.CustomUserDetailsService;
import com.ezfarm.ezfarmback.security.local.TokenProvider;
import com.ezfarm.ezfarmback.user.domain.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER = "USER";

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final AlertService alertService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

            .headers().frameOptions().disable().and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()

            .addFilter(corsFilter)
            .addFilter(loginFilter())
            .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenProvider,
                customUserDetailsService))

            .exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
            .and()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .authorizeRequests()
            .antMatchers(
                "/",
                "/swagger-ui/",
                "/api/user/signup")
            .permitAll()
            .antMatchers("/api/**").hasRole(USER)
            .and()
            .logout()
            .addLogoutHandler(new LogoutHandler() {
                @Override
                public void logout(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) {
                    User logoutUser = (User) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
                    alertService.deleteToken(logoutUser.getId());
                }
            });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
            );
    }

    private LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager());
        loginFilter.setFilterProcessesUrl("/api/user/login");
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(tokenProvider));
        return loginFilter;
    }
}
