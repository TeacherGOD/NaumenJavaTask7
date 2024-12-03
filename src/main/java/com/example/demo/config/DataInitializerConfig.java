package com.example.demo.config;

import com.example.demo.services.DataInitializerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerConfig implements CommandLineRunner {





    @Autowired
//    private DataInitializerService dataInitializerService;
    private DataInitializerService dataInitializerService;
    private Logger logger= LoggerFactory.getLogger(DataInitializerConfig.class);


    @Override
    public void run(String... args) throws Exception {
        dataInitializerService.initialize();
//        System.out.println("Initial data has been added.");
        logger.info("Initial data has been added");

    }
}