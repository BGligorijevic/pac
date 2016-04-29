package com.prodyna.voting;

import com.prodyna.voting.auth.filter.SecurityFilter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${voting.app.secret.key}")
    @Getter
    private String secretKey;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public FilterRegistrationBean securityFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SecurityFilter(secretKey));
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }
}
