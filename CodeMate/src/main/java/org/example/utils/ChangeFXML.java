package org.example.utils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeFXML {
    public static void changeFXML(Event event, String pathFXML) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Scene.class.getResource(pathFXML));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Node resources = (Node) event.getSource();
            Stage stage = (Stage) resources.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
