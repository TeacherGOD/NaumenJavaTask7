package com.example.demo.exceptions;

/**
 * Исключение, выбрасываемое, когда запрашиваемый ресурс не найден.
 * <p>
 * Это пользовательское исключение наследуется от {@link RuntimeException} и
 * используется для обработки ошибок, связанных с отсутствием ресурсов,
 * таких как пользователи, заметки или категории в базе данных.
 * </p>
 *
 * @author VladimirBoss
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}