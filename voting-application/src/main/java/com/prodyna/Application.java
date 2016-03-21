package com.prodyna;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.prodyna.auth.JwtFilter;

import lombok.Getter;

@SpringBootApplication
public class Application {

    @Value("${secretKey}")
    @Getter
    private String secretKey;

    public static void main(final String[] args) {
	SpringApplication.run(Application.class, args);
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
	final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	registrationBean.setFilter(new JwtFilter(secretKey));
	registrationBean.addUrlPatterns("/api/*");

	return registrationBean;
    }
}
