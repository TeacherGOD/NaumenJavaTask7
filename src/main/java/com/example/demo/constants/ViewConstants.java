package com.example.demo.constants;

/**
 * Класс для хранения констант представлений.
 * <p>
 * Содержит статические финальные переменные для путей к HTML-шаблонам,
 * используемым в приложении.
 * </p>
 *
 * @author VladimirBoss
 */
public class ViewConstants {
    /** Путь к главной странице заметок. */
    public static final String HOME_PAGE = "/notes/view/all";

    /** Название шаблона для отображения всех заметок. */
    public static final String ALL_NOTES_VIEW = "all-notes";

    /** Название шаблона для создания новой заметки. */
    public static final String CREATE_NOTE_VIEW = "create-note";

    /** Название шаблона для редактирования заметки. */
    public static final String EDIT_NOTE_VIEW = "edit-note";

    /** Название шаблона для отображения категорий. */
    public static final String CATEGORY_VIEW = "categories";

    /** Название шаблона для добавления напоминания. */
    public static final String ADD_REMINDER_VIEW = "addReminder";

    /** Название шаблона для страницы регистрации. */
    public static final String REGISTRATION_VIEW = "registration";

    /** Название шаблона для страницы входа. */
    public static final String LOGIN_VIEW = "login";

    /** Название шаблона для страницы ошибки 404. */
    public static final String ERROR_VIEW = "404";

    /** Название шаблона для удаления напоминания. */
    public static final String REMOVE_REMINDER_VIEW = "removeReminder";

    /** Название шаблона для отображения тегов. */
    public static final String TAGS_VIEW = "tags";

    /** Название шаблона для фильтрации тегов. */
    public static final String TAGS_FILTER_VIEW = "tags-filter";


    // Приватный конструктор для предотвращения создания экземпляров этого класса
    private ViewConstants() {
        throw new AssertionError("This class is not meant to be instantiated");
    }


}