package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(length = 5000)
    private String content;

    public Report() {
        this.status = ReportStatus.CREATED;
    }


/*
    """
    Количество пользователей: {userCount}
    <br>
    Время на расчёт пользователей: {userCountTime}
    <br>
    Заметки: {notes} //скорее всего надо будет через tf их выводит +- адекватно, или просто по title?
    <br>
    Время на расчёт заметок: {notesTime}
    <br>
    Общее время: {allTime}
    """

*/

}
