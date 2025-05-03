package ru.nstu.labbi;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class AntMain extends Application {

    AntController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AntMain.class.getResource("AntsLife.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ants forever!");
        stage.setScene(scene);

        scene.getRoot().requestFocus();
        controller = fxmlLoader.getController();
        controller.initElements();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
