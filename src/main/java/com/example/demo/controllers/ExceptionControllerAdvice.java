package com.example.demo.controllers;

import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionControllerAdvice {

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiException handleGeneralException(Exception e) {
//        return ApiException.create(e);
//    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleResourceNotFoundException(ResourceNotFoundException e) {
        return ApiException.create(e);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleNoHandlerFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView("404"); // Имя вашего шаблона
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}