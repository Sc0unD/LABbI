package ru.nstu.labbi.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Objects;


public class CurrentObjectsController {
    @FXML
    private Button okBtn;

    @FXML
    private ListView<String> listView;

    @FXML
    public void init(ObservableList<String> list) {

        listView.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/ru/nstu/labbi/CSS/listview.css")).toExternalForm());

        listView.setItems(list);

        okBtn.setOnAction(actionEvent ->( (Stage)okBtn.getScene().getWindow()).close() );
    }
}
