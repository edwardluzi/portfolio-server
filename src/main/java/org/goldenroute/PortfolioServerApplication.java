package org.goldenroute;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.goldenroute.portfolio.alpha.AlphaEngine;
import org.goldenroute.portfolio.service.DatabaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortfolioServerApplication
{
    private static final Logger logger = Logger.getLogger(PortfolioServerApplication.class);

    @Autowired
    private AlphaEngine alphaEngine;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    public static void main(String[] args)
    {
        SpringApplication.run(PortfolioServerApplication.class, args);
    }

    @Bean
    CommandLineRunner init()
    {
        return (evt) -> {
            configureLogger();
            databaseInitializer.config();
            alphaEngine.start();
        };
    }

    private void configureLogger()
    {
        System.setOut(new PrintStream(System.out)
        {
            @Override
            public void print(String string)
            {
                super.print(string);
                logger.info(string);
            }
        });

        System.setErr(new PrintStream(System.err)
        {
            @Override
            public void print(String string)
            {
                super.print(string);
                logger.error(string);
            }
        });
    }
}
