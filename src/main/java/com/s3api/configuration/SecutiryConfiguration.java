/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.s3api.configuration;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author cesar
 */
@Configuration
@ComponentScan(basePackages = {"com.s3api", "com.s3api.fachada.impl"})
@EnableWebSecurity
public class SecutiryConfiguration extends WebSecurityConfigurerAdapter{
    
    private static final String APITEXT = "/api/**";
    
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        try{
            http.csrf().disable().authorizeRequests()
                    .antMatchers("/login", "/logout").permitAll()
                    .antMatchers("/index.html#/**", "/#/**").authenticated()
                    .antMatchers(HttpMethod.GET, APITEXT).authenticated()
                    .antMatchers(HttpMethod.POST, APITEXT).authenticated()
                    .antMatchers(HttpMethod.PUT, APITEXT).authenticated()
                    .antMatchers(HttpMethod.DELETE, APITEXT).authenticated()
                    .and().formLogin().loginPage("/login")
                    .usernameParameter("ssoId").passwordParameter("password")
                    .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                    .and().httpBasic();
            // Lineas para solucionar  X-frame ClickJacking
            http.headers().frameOptions().disable();
            // Lienas para XSSProtection
            http.headers().xssProtection().block(false);
            // Lienas para X-Content-Type-Options Header Missing
            http.headers().defaultsDisabled().contentTypeOptions();
        }catch(Exception e){
            throw new Exception(e);
        }
    }
}
