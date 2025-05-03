package ru.nstu.labbi.DialogWindows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nstu.labbi.Controllers.ErrorDialogController;

import java.io.IOException;

public class ErrorDialogWindow extends DialogWindow {

    ErrorDialogController controller;

    public ErrorDialogWindow(Stage owner, String errorText) {
        super(owner, "Ошибка!");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/nstu/labbi/DialogWindows/ErrorDialog.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        controller.init(errorText);
    }
}
