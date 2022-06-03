package com.example.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration //스프링이 메모리에 올라갈때 설정정보를 같이 로딩한다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.inMemoryAuthentication()
                .withUser("mike")
                .password("{noop}1234")
                .roles("USER"); //USER권한
    }*/



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("mike")
            .password("{noop}1234")
            .roles("USER");
    }

    //Ctrl + o 로 메서드를 오버라이딩 할 수 있다.
    //h2-console~ url은 인증처리 없이 접근하도록 해준다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //antMatchers(("h2-console/**")).permitAll() h2-console/** 경로로 접근되는것은 패스
        http.authorizeRequests()
            .antMatchers(("h2-console/**")).permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
        http.csrf().disable(); //추가
        http.headers().frameOptions().disable();//추가
    }
}
