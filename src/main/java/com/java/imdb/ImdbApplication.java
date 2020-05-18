package com.java.imdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 * Main Class for Spring Boot Application
 * 
 * @author Ganapathy_N
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
public class ImdbApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ImdbApplication.class, args);
    }
}
