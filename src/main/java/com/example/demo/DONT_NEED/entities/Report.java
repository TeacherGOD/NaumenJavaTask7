package com.example.demo.DONT_NEED.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
