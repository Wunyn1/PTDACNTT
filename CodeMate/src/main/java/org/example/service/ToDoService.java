package org.example.service;

import org.example.dao.ToDoListDAO;
import org.example.entity.ToDoList;
import org.example.model.User;

import java.time.LocalDate;
import java.util.List;

public class ToDoService {
    public ToDoList addTodo(String title, User currentUser) {
        if (title == null || title.trim().isEmpty() || currentUser == null) return null;

        ToDoList todo = new ToDoList(title.trim(), currentUser);
        ToDoListDAO.save(todo);
        return todo;
    }

    public List<ToDoList> getToDosByUser(User currentUser) {
        if(currentUser == null){
            return null;
        }
        return ToDoListDAO.findByUserAndDate(currentUser, LocalDate.now());
    }

    public void update(ToDoList todo) {
        if (todo == null) return;
        ToDoListDAO.update(todo);
    }

    public void setCompleted(ToDoList todo, boolean completed) {
        if (todo == null) return;
        todo.setCompleted(completed);
        update(todo);
    }

    public void delete(ToDoList todo) {
        if (todo == null) return;
        ToDoListDAO.delete(todo);
    }
}
