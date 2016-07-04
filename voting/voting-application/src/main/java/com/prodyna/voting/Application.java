package com.prodyna.voting;

import com.prodyna.voting.auth.filter.SecurityFilter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application {

    @Value("${voting.app.secret.key}")
    @Getter
    private String secretKey;

    /**
     * Application entry point.
     * Just like in the good old days of University Java programming. :)
     * See SpringBoot docs for more details.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Creates and associates the filter instance to intercept all incoming
     * requests under {@link SecurityFilter#SECURED_API_PATH}.
     */
    @Bean
    public FilterRegistrationBean securityFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SecurityFilter(secretKey));
        registrationBean.addUrlPatterns(SecurityFilter.SECURED_API_PATH);

        return registrationBean;
    }

    /**
     * Adds CORS allowed origin for local client-side development server which we can trust.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://localhost").allowedHeaders("*").allowedMethods("*");
            }
        };
    }
}