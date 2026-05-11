package org.example.controller;

import javafx.fxml.FXML;
import org.example.utils.ChangeFXML;
import javafx.event.Event;

public class HomeController {
    @FXML
    public void logout(Event event) {
        ChangeFXML.changeFXML(event, "/view/login.fxml");
    }

    @FXML
    public void showTodoList(Event event) {
        ChangeFXML.changeFXML(event, "/view/todo.fxml");
    }
}
