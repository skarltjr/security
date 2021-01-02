package com.example.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * .mvcMatchers("/User").hasRole("USER") 일 때 Admin은 유저이면서 admin롤을 갖는다
     * 따라서 admin도 user role에 접근할 수 있어야하는데 이대로면 admin은 user role로 접근할 수 없다.
     */
    public SecurityExpressionHandler securityExpressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        /**  게층구조를 설정해주고*/
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        /** 핸들러에 계층구조를 설정*/
        return handler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**","/signup").permitAll()
                .mvcMatchers("/admin", "/dashBoard").hasRole("ADMIN")
                .mvcMatchers("/User").hasRole("USER")
                .anyRequest().authenticated() // 기타등등은 인증을 하면 접근할 수 있다
                .expressionHandler(securityExpressionHandler());
                //.and() //and로 이어가거나 아니면 Request내용 이후 다른 내용을 추가할 때 새로 http로 시작해주거나
        http
                .formLogin() //로그아웃도 가능
                .loginPage("/login").permitAll();
        http
                .logout()
                .logoutSuccessUrl("/");
       /* http
                .httpBasic().disable(); //httpBasic도 사용하겠다 - 사용하지않겠다*/

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        // 어디까지 자원을 공유할 것인가? -> 기본은 쓰레드로컬 하위 자식까지 가능하도록
        // @EanbleAync - @Async 어노테이션을 사용했을 때도 가능하도로고
    }

    /**  요청 - http filter 적용  / static resource - web */
    @Override
    public void configure(WebSecurity web) throws Exception {
       // web.ignoring().mvcMatchers("/favicon.ico"); // 파비콘 요청은 시큐리티 안걸리도록 그러나 다 설정하기 귀찮으니

        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        // 정적리소스 모두에 대해 ignore 적용
    }
}
