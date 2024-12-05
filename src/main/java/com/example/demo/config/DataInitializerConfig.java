package com.example.demo.config;

import com.example.demo.services.DataInitializerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * Конфигурационный класс, реализующий {@link CommandLineRunner} для инициализации данных с использованием {@link DataInitializerService}.
 *
 * @author VladimirBoss
 * @see CommandLineRunner
 * @see DataInitializerService
 */
@Component
public class DataInitializerConfig implements CommandLineRunner {


    @Autowired
    private DataInitializerService dataInitializerService;
    private Logger logger = LoggerFactory.getLogger(DataInitializerConfig.class);


    /**
     * Инициализирует данные с использованием {@link DataInitializerService}.
     *
     * @param args Аргументы командной строки, переданные приложению.
     * @throws Exception Если возникает ошибка во время инициализации данных.
     */
    @Override
    public void run(String... args) throws Exception {
        dataInitializerService.initialize();
        logger.info("Initial data has been added");

    }
}