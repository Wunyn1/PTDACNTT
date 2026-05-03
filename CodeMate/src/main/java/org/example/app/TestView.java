package org.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class TestView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try{
            URL fxml = Objects.requireNonNull(getClass().getResource("/view/login.fxml"));
            Parent root = FXMLLoader.load(fxml);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}