package com.example.demo.DONT_NEED.services;

import com.example.demo.entities.Note;
import com.example.demo.DONT_NEED.entities.Report;
import com.example.demo.DONT_NEED.entities.ReportStatus;
import com.example.demo.repositories.NoteRepository;
import com.example.demo.DONT_NEED.repositories.ReportRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    public Long createReport() {
        Report report = new Report();
        reportRepository.save(report);
        return report.getId();
    }


    public CompletableFuture<Report> createReportAsync(Long reportId) {

        AtomicLong userCount = new AtomicLong();
        AtomicLong notesTime = new AtomicLong();
        AtomicLong userCountTime = new AtomicLong();
        AtomicReference<List<Note>> notes=new AtomicReference<List<Note>>();
        return CompletableFuture.supplyAsync(() -> {


            long startTime = System.currentTimeMillis();
            var report = findReport(reportId);


            Thread userCountThread = new Thread(() -> {
                long userCountStartTime = System.currentTimeMillis();
                userCount.set(userRepository.count());
                userCountTime.set(System.currentTimeMillis() - userCountStartTime);

            });

            Thread notesListThread = new Thread(() -> {
                long notesStartTime = System.currentTimeMillis();
                notes.set(StreamSupport.stream(noteRepository.findAll().spliterator(), false)
                        .toList());

                notesTime.set(System.currentTimeMillis() - notesStartTime);


            });


            userCountThread.start();
            notesListThread.start();

            try {
                userCountThread.join();
                notesListThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long allTime = System.currentTimeMillis() - startTime;
            report.setContent(formatReportText(userCount.get(), userCountTime.get(), notes.get(), notesTime.get(), allTime));
            report.setStatus(ReportStatus.COMPLETED);

            reportRepository.save(report);
            return report;
        });

    }



    public String formatReportText(Long userCount,Long userCountTime,List<Note> notes,Long notesTime, Long allTime)
    {
//        var res= "Количество пользователей: "+userCount+
//                "    <br>\n" +
//                "    Время на расчёт пользователей: "+userCountTime +
//                "    <br>\n" +
//                "    Заметки: "+notes+//скорее всего надо будет через th их выводить +- адекватно, или просто по title?
//                "    <br>\n" +
//                "    Время на расчёт заметок: "+notesTime+
//                "    <br>\n" +
//                "    Общее время: "+allTime;
//        return res;

        String noteTitles = notes.stream()
                .map(Note::toString)
                .reduce((a, b) -> a + "<br/> " + b)
                .orElse("Нет заметок");

        return String.format("Количество пользователей: %d<br/>" +
                        "Время на расчёт пользователей: %d мс<br/>" +
                        "Заметки: <br/>%s<br/>" +
                        "Время на расчёт заметок: %d мс<br/>" +
                        "Общее время: %d мс",
                userCount, userCountTime, noteTitles, notesTime, allTime);
    }


    public String getContent(Long id) {
        var report = findReport(id);
        return report.getContent();
    }

    public Report findReport(Long id) {
        return reportRepository.findById(id).orElseThrow(()-> new IllegalStateException("Report with id " + id + " is not found"));
    }
}