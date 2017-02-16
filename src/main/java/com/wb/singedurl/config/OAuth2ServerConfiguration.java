package com.wb.singedurl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
public class OAuth2ServerConfiguration {

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/protected/**").authenticated()
                    .antMatchers("/protected").authenticated()
                    .antMatchers("/**").permitAll()
                    .and()
                    .csrf().disable();
        }
    }
}