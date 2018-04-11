package com.windhound.server;

import com.windhound.server.database.DBBoat;
import com.windhound.server.database.DBChampionship;
import com.windhound.server.database.DBManager;
//import com.windhound.server.database.DBMoveData;
import com.windhound.server.movedata.*;
import com.windhound.server.race.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.windhound.server.race.course.Buoy;
import com.windhound.server.race.course.Course;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main
{
    // Get PORT and HOST from Environment or set default
    public static final Optional host;
    public static final Optional port;
    public static final Properties myProps = new Properties();

    private static ConfigurableApplicationContext appContext;
    static
    {
        host = Optional.ofNullable(System.getenv("HOSTNAME"));
        port = Optional.ofNullable(System.getenv("PORT"));
    }

    public static void main(String[] args)
    {
        // Set properties
        // Allows both local testing and deploy with same code
        myProps.setProperty("server.address", (String)host.orElse("localhost"));
        myProps.setProperty("server.port", (String)port.orElse("8080"));

        SpringApplication app = new SpringApplication(Main.class);
        app.setDefaultProperties(myProps);

        appContext = app.run(args);

        System.out.println("\n\n\n\nMUST MAKE SURE ALL CONSTRUCTORS MAKE A DEEP COPY TO PARAMETERS(EXAMPLE 'NEW COURSE')\n\n\n\n");
    }

    public static void Close()
    {
        appContext.close();
    }
}