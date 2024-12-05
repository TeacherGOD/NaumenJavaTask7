package com.example.demo.exceptions;

/**
 * Класс, представляющий API-исключение.
 * <p>
 * Этот класс используется для передачи сообщений об ошибках
 * в ответах API.
 * </p>
 *
 * @author VladimirBoss
 */
public class ApiException extends Exception {

    /** Сообщение об ошибке. */
    private String message;

    /**
     * Приватный конструктор для создания экземпляра ApiException.
     *
     * @param message Сообщение об ошибке.
     */
    private ApiException(String message) {
        this.message = message;
    }


    @Override()
    public String getMessage() {
        return message;
    }

    /**
     * Устанавливает новое сообщение об ошибке.
     *
     * @param message Новое сообщение об ошибке.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Создает экземпляр ApiException на основе Throwable.
     *
     * @param e Исключение, из которого будет извлечено сообщение.
     * @return Новый экземпляр ApiException с сообщением из Throwable.
     */
    public static ApiException create(Throwable e) {
        return new ApiException(e.getMessage());
    }

    /**
     * Создает экземпляр ApiException с заданным сообщением.
     *
     * @param message Сообщение об ошибке.
     * @return Новый экземпляр ApiException с указанным сообщением.
     */
    public static ApiException create(String message) {
        return new ApiException(message);
    }
}