package com.example.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/info","/account/**").permitAll()
                .mvcMatchers("/admin", "/dashBoard").hasRole("ADMIN")
                .anyRequest().authenticated(); // 기타등등은 인증을 하면 접근할 수 있다
                //.and() //and로 이어가거나 아니면 Request내용 이후 다른 내용을 추가할 때 새로 http로 시작해주거나
        http
                .formLogin(); //로그아웃도 가능
        http
                .httpBasic(); //httpBasic도 사용하겠다
    }
}
