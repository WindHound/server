package com.windhound.server.security;

import com.windhound.server.database.DBManager;
import oracle.jdbc.pool.OracleDataSource;
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
import java.sql.SQLException;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan({ "com.mkyong.web.*" })
//@Import({ com.windhound.server.security.AppConfig.class })
public class AppConfig {

        @Bean(name = "dataSource")
        public DriverManagerDataSource dataSource() {

            String hostname = null;
            String sid      = null;
            String user     = null;
            String password = null;
            String port     = null;

            Properties prop = new Properties();
            InputStream inputStream = DBManager.class.getClassLoader().
                    getResourceAsStream("config.properties");

            try {
                prop.load(inputStream);

                hostname = prop.getProperty("hostname");
                port = prop.getProperty("port");
                sid = prop.getProperty("sid");
                user = prop.getProperty("user");
                password = prop.getProperty("password");
            } catch (IOException e) {
                e.printStackTrace();
            }

            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setUrl("jdbc:oracle:thin:@" + hostname + ":" + port + ":" + sid);
            driverManagerDataSource.setUsername(user);
            driverManagerDataSource.setPassword(password);
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

