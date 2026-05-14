package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class PomodoroView {
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/home.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Focus Dashboard - Productivity Tool");

        stage.setScene(scene);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
