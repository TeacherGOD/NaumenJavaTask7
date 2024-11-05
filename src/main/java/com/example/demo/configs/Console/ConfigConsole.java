package com.example.demo.configs.Console;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;

//@Configuration
public class ConfigConsole {

    // Console configuration
//    @Autowired
    private CommandProcessor commandProcessor;

    public ConfigConsole(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Bean
    public CommandLineRunner commandScanner() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            commandProcessor.processCommand("help");
            while (true) {
                System.out.print("Enter command: ");
                String command = scanner.nextLine();
                if (command.equals("exit")) {
                    System.out.println("Goodbye.");
                    break;
                }
                commandProcessor.processCommand(command);
            }
        };
    }
}
