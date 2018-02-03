package com.windhound.server;

/**
 * Created by nathan on 02/02/2018.
 */
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greeting2Controller
{
    private static final String template = "Hello, %s! The year is %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting2")
    public Greeting greeting2(@RequestParam(value="name", defaultValue="World") String name,
                              @RequestParam(value="year", defaultValue="1970") String year)
    {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name, year));
    }
}