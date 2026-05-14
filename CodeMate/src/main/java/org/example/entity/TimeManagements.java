package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_managements")
@Getter
@Setter
public class TimeManagements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "task_name")
    private String task_name;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start_time;

    @Column(name = "end_time")
    private LocalDateTime end_time;

    @Column(name = "duration_seconds")
    private long duration_seconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "pomodoro_state", nullable = false)

    private PomodoroState state;

    public TimeManagements() {
    }

    public TimeManagements(User user, String task_name, LocalDateTime start_time, LocalDateTime end_time, PomodoroState state) {
        this.user = user;
        this.task_name = task_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.state = state;

        if(start_time != null && end_time != null){
            this.duration_seconds = java.time.Duration.between(start_time,end_time).getSeconds();
        }
        else {
            this.duration_seconds = 0;
        }
    }
}
