package ru.nstu.labbi.DialogWindows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nstu.labbi.Controllers.CurrentObjectsController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class CurrentObjectsDialogWindow extends DialogWindow {

    CurrentObjectsController controller;

    public CurrentObjectsDialogWindow(Stage owner, TreeMap<Integer,Long> treeMap) {
        super(owner, "Текущие объекты");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ru/nstu/labbi/DialogWindows/CurrentObjects.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        controller = fxmlLoader.getController();
        controller.init(parseTreeMapToObservableList(treeMap));
    }

    private static ObservableList<String> parseTreeMapToObservableList(TreeMap<Integer,Long> treeMap) {
        Stream<Map.Entry<Integer,Long>> stream = treeMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue));

        List<Map.Entry<Integer,Long>> entryList = stream.toList();
        int maxValueLength;
        int maxKeyLength;
        int maxNumberLength;

        ObservableList<String> list = FXCollections.observableArrayList();

        try {
            maxValueLength = entryList.getLast().getValue().toString().length();
            maxKeyLength = entryList.getLast().getKey().toString().length();
            maxNumberLength = ((Integer)(entryList.size() + 1)).toString().length();
        }
        catch (NoSuchElementException e) {
            return list;
        }

        int i = 1;

        for (Map.Entry<Integer,Long> entry : entryList) {
            String value = String.format("%" + maxNumberLength + "d)   %-" + maxValueLength + "d", i++, entry.getValue());
            String key =   String.format("%" + maxKeyLength + "d",entry.getKey());
            list.add(value + " -- " + key);
        }
        return list;
    }
}
