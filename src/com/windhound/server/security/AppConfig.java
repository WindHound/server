package com.windhound.server.security;

import com.windhound.server.database.DBManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SecurityConfig {

    @EnableWebMvc
    @Configuration
    @ComponentScan({ "com.mkyong.web.*" })
    @Import({ SecurityConfig.class })
    public class AppConfig {

        @Bean(name = "dataSource")
        public DriverManagerDataSource dataSource() {

            Properties prop = new Properties();
            InputStream inputStream = DBManager.class.getClassLoader().
                    getResourceAsStream("config.properties");

            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test");
            driverManagerDataSource.setUsername("root");
            driverManagerDataSource.setPassword("password");
            return driverManagerDataSource;
        }

        @Bean
        public InternalResourceViewResolver viewResolver() {
            InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
            viewResolver.setViewClass(JstlView.class);
            viewResolver.setPrefix("/WEB-INF/pages/");
            viewResolver.setSuffix(".jsp");
            return viewResolver;
        }

    }
}
