package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {

    @Autowired
    private JwtFilter jwtTokenFilter;

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
        var bean = new FilterRegistrationBean<JwtFilter>();
        bean.setFilter(jwtTokenFilter);
        bean.addUrlPatterns("/region/adm/*");
        bean.addUrlPatterns("/category/adm/*");
        bean.addUrlPatterns("/profile/adm/*");
        bean.addUrlPatterns("/profile/image/*");
        bean.addUrlPatterns("/article/adm/*");
        bean.addUrlPatterns("/attach/adm/*");
        bean.addUrlPatterns("/comment/adm/*");
        bean.addUrlPatterns("/tag/adm/*");
        bean.addUrlPatterns("/channel/*");
        bean.addUrlPatterns("/profile/*");
        bean.addUrlPatterns("/video/profile/*");
        bean.addUrlPatterns("/video/adm/*");
        bean.addUrlPatterns("/playlist/profile/*");
        bean.addUrlPatterns("/playlist/adm/*");
        return bean;
    }

}
