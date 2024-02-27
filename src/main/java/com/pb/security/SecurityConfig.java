package com.pb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationFilter();
    }
    
    @Bean
    public DaoAuthenticationProvider authProvider(MyUserDetailsService userDetailsService) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,JwtAuthenticationEntryPoint unauthorizedHandler) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(getOtherFilters()).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
       http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private String[] getOtherFilters() {
        ArrayList<String> list = new ArrayList<>();
        list.add("/swagger-ui.html/**");
        list.add("/swagger-ui/index.html");
        list.add("/admin/login");
        list.add("/admin/retailer/login");
        list.add("/user/register");
        list.add("/webjars/**");
        list.add("/swagger-resources/**");
        list.add("/v2/api-docs");
        list.add("/admin/register");
        list.add("/admin/send-otp");
        list.add("/admin/banner/get-all-banner-by-location");
        list.add("/admin/verify-otp");
        list.add("/stripe/**");
        list.add("/user/verify-email");
        list.add("/admin/forgot-password");
        list.add("/admin/get-all-static-page");
        return list.toArray(new String[list.size()]);
    }
}
