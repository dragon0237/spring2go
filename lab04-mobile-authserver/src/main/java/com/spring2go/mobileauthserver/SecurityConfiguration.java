package com.spring2go.mobileauthserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //配置用户的登录认证信息
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("bobo").password("xyz").roles("USER");
    }

    //以下的URL不需要校验
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console*/**", "/webjars/**", "/images/**", "/oauth2/uncache_approvals",
                "/oauth2/cache_approvals");
    }

    //对oauth2的一些端点需要进行校验
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/oauth2/**").authenticated().and().csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth2/authorize")).disable().formLogin()
                .permitAll().and().logout().permitAll().and();

    }
}
