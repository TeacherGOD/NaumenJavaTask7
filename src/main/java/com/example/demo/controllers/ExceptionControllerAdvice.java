package com.example.demo.controllers;

import com.example.demo.constants.ViewConstants;
import com.example.demo.exceptions.ApiException;
import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Обработчик исключений для контроллеров приложения.
 * <p>
 * Перехватывает и обрабатывает исключения, возникающие в контроллерах,
 * возвращая соответствующие ответы и представления.
 *
 * @author VladimirBoss
 * @see ResourceNotFoundException
 * @see ApiException
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Обрабатывает исключения типа ResourceNotFoundException.
     *
     * @param e Исключение, которое необходимо обработать.
     * @return ApiException - объект с информацией об ошибке.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleResourceNotFoundException(ResourceNotFoundException e) {
        return ApiException.create(e);
    }

    /**
     * Обрабатывает общие исключения, не перехваченные другими обработчиками.
     *
     * @param e Исключение, которое необходимо обработать.
     * @return ModelAndView - объект для отображения страницы ошибки 404.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleNoHandlerFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView(ViewConstants.ERROR_VIEW);
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}