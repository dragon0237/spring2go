package com.spring2go.mobileauthserver.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OAuth2Configuration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;//身份认证端点

    @Bean
    public TokenGranter tokenGranter() {

        DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService());

        AuthorizationCodeServices codeServices = authorizationCodeServices();
        // 代码拼装的方法实现同时支持三种授权模式：授权码模式，密码模式，隐含模式。这里与Android App项目对接使用的授权码模式
        AuthorizationServerTokenServices tokenServices = tokenServices();
        List<TokenGranter> tokenGranters = Arrays.asList(
                new AuthorizationCodeTokenGranter(tokenServices, codeServices, clientDetailsService(), requestFactory),
                new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService(), requestFactory),
                new ImplicitTokenGranter(tokenServices, clientDetailsService(), requestFactory));

        return new CompositeTokenGranter(tokenGranters);
    }

    // 客户端的信息操作类，我们是将客户端的注册信息保存在数据库，所以使用JdbcClientDetailService子类
    @Bean
    public ClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    // 授权服务器的令牌操作类，传入令牌的管理类，和身份验证管理类
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setAuthenticationManager(authenticationManager);
        return tokenServices;
    }

    //类似JwtTokenStore，这里使用JdbcTokenStore来校验令牌，并在数据库中管理
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    // 授权码的操作类，存储在内存中管理
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

}
