package ru.nstu.labbi.DialogWindows;

import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class DialogWindow {
    Stage stage;

    public DialogWindow(Stage owner, String title) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle(title);
    }

    public void show() {
        stage.showAndWait();
    }
}
