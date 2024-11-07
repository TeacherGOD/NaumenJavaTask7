package com.example.demo.entities;

public enum ReportStatus {

    CREATED("Создан"),
    COMPLETED("Завершен"),
    ERROR("Ошибка");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
