package com.oauth2lab.clientresttemplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

// 这个web应用是需要用户登录认证的，这里配置security做访问拦截
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // security会从数据库通过用户名加载用户信息对象
    @Autowired
    private UserDetailsService userDetailsService;

    // 将用户的信息对象传给security的用户认证管理类
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // 只有'/','/index.html'两个path不需要认证直接访问；
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/","/index.html").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().logout().permitAll()
                .and().csrf().disable();
    }
}
