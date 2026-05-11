package org.example.entity;

import jakarta.persistence.*;
import org.example.model.User;

import java.time.LocalDate;

public class ToDoList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private boolean completed;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ToDoList() {
        this.createdAt = LocalDate.now();
    }

    public ToDoList(String title, User user) {
        this.title = title;
        this.user = user;
        this.completed = false;
        this.createdAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override

    public String toString(){
        return title;
    }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
