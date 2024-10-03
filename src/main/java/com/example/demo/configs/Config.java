package com.example.demo.configs;


import com.example.demo.note.Note;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;

@Configuration
public class Config {


    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public ArrayList<Note> notesContainer()
    {
        return new ArrayList<>();
    }


}
