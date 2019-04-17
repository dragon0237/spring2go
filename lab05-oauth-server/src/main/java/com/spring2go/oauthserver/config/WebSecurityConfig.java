package com.spring2go.oauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// 关闭springboot给于security的默认配置
@EnableWebSecurity
// 配置启动顺序，用来覆盖访问规则而不改变其他自动配置的特性
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
// WebSecurityConfigurerAdapter用于保护oauth相关的endpoints，同时主要作用于用户的登录(form login,Basic auth)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //全局的用户认证信息：预注册一些用户，并配上一些权限
    @Autowired
    public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("john").password("123").roles("USER").and()
                .withUser("tom").password("111").roles("ADMIN").and()
                .withUser("user1").password("pass").roles("USER").and()
                .withUser("admin").password("nimda").roles("ADMIN");
    }

    // 默认的AuthenticationManager只有一个用户（用户名user，密码在启动时随机生成，以INFO日志级别打印出来）
    // 使用password授权模式时，我们会注册一些用户名密码，所以不能使用默认的配置，
    // 需要重新注入配置有注册用户的自定义 authenticationManagerBean 用来认证用户
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //对部分oauth2相关请求放行，其他的需要授权认证才能访问
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/oauth/token/revokeById/**").permitAll()
                .antMatchers("/tokens/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().csrf().disable();
    }
}
