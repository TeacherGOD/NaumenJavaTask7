package com.example.demo.exceptions;

/**
 * Исключение, выбрасываемое, когда объект уже существует в системе.
 * <p>
 * Это пользовательское исключение наследуется от {@link RuntimeException} и
 * используется для обработки ошибок, связанных с попытками создания объектов,
 * которые уже существуют (например, пользователи с одинаковыми именами или
 * адресами электронной почты).
 * </p>
 *
 * @author VladimirBoss
 */
public class AlreadyExistException extends RuntimeException{

    /**
     * Конструктор для создания исключения с заданным сообщением.
     *
     * @param message Сообщение об ошибке.
     */
    public AlreadyExistException(String message) {
        super(message);
    }
}
