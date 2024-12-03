package com.example.demo.config;


import com.example.demo.entities.Note;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;

@Configuration
@EnableScheduling
public class Config {


    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public ArrayList<Note> notesContainer()
    {
        return new ArrayList<>();
    }






}
