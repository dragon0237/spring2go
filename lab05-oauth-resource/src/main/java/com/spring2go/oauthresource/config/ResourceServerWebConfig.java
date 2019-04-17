package com.spring2go.oauthresource.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//Web配置
@Configuration
@EnableWebMvc
@ComponentScan({ "com.spring2go.oauthresource.web.controller" })
public class ResourceServerWebConfig extends WebMvcConfigurerAdapter {
}
