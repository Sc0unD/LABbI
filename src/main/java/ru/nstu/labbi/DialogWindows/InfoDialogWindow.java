package ru.nstu.labbi.DialogWindows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nstu.labbi.Controllers.InfoDialogController;

import java.io.IOException;

public class InfoDialogWindow extends DialogWindow{

    InfoDialogController controller;
    public static final int OK = 1;
    public static final int CANCEL = 2;

    public InfoDialogWindow(Stage owner, String text) {
        super(owner, "Статистика симуляции");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/nstu/labbi/DialogWindows/InfoDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        controller.init(text);
    }

    public int get() {
        return controller.getState();
    }

}
