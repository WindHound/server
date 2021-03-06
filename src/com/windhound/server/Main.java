package com.windhound.server;

import java.util.Optional;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import test.MoveDataControllerTest;
import test.StructureControllerTest;

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

        //StructureControllerTest.addGetRace();
        //MoveDataControllerTest.addGetMoveData();
    }

    public static void Close()
    {
        appContext.close();
    }
}