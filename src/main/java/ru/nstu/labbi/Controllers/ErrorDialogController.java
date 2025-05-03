package ru.nstu.labbi.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ErrorDialogController {

    @FXML
    private TextArea text;

    @FXML
    private Button okBtn;

    public void init(String areaText) {
        text.setText(areaText);

        okBtn.setOnAction(actionEvent -> {
            ((Stage)okBtn.getScene().getWindow()).close();
        });
    }

}
