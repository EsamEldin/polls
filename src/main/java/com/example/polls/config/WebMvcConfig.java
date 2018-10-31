package com.example.polls.config;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 *We’ll be accessing the APIs from the react client that will run on 
 *its own development server.(different server)
   create the following WebMvcConfig class inside com.example.polls.config package to enable cross origin requests globally -
 *
 *
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//to enable cross origin requests globally -
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    
    //enable cross origin requests globally
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
