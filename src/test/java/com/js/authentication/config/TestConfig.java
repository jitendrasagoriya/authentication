package com.js.authentication.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.js.authentication.dao",
        "com.js.authentication.repository"
})
public class TestConfig {
}
