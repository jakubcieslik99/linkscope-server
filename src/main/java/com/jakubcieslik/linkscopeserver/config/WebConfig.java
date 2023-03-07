package com.jakubcieslik.linkscopeserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebConfig {

  @Value("${WEBAPP_URL}")
  private String webAppURL;
  @Value("${API_URL}")
  private String apiURL;

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfig = new CorsConfiguration();

    corsConfig.setAllowedOrigins(Arrays.asList(webAppURL, apiURL));
    corsConfig.setAllowedMethods(Arrays.asList(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.DELETE.name()
    ));
    corsConfig.setAllowCredentials(true);
    corsConfig.setMaxAge(3600L);

    corsSource.registerCorsConfiguration("/**", corsConfig);
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsSource));
    bean.setOrder(-100); //before Spring Security (100)
    return bean;
  }
}
