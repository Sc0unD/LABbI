package ru.nstu.labbi.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class InfoDialogController {

    @FXML
    private TextArea text;

    @FXML
    private Button okBtn;

    @FXML
    private Button cancelBtn;

    public static final int OK = 1;
    public static final int CANCEL = 2;
    private int state = 0;

    public void init(String infoText) {
        text.setText(infoText);

        okBtn.setOnAction(actionEvent -> {
            state = OK;
            ((Stage)okBtn.getScene().getWindow()).close();
        });

        cancelBtn.setOnAction(actionEvent -> {
            state = CANCEL;
            ((Stage)cancelBtn.getScene().getWindow()).close();
        });
    }

    public int getState() {
        return state;
    }
}
