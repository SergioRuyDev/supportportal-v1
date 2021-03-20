package com.sergioruy.supportportal.configuration;

import com.sergioruy.supportportal.constant.SecurityConstant;
import com.sergioruy.supportportal.filter.JwtAccessDeniedHandler;
import com.sergioruy.supportportal.filter.JwtAuthenticationEntryPoint;
import com.sergioruy.supportportal.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Qualifier("userDetailService") private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}
