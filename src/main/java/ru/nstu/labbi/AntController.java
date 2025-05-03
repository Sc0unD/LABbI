package ru.nstu.labbi;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.nstu.labbi.DialogWindows.CurrentObjectsDialogWindow;
import ru.nstu.labbi.DialogWindows.ErrorDialogWindow;
import ru.nstu.labbi.DialogWindows.InfoDialogWindow;

import java.io.IOException;

public class AntController {

    @FXML
    private Label time;

    @FXML
    private Label timerLabel;

    @FXML
    private Pane rootScene;

    @FXML
    private Pane workspacePane;

    @FXML
    private Label statTimerLabel;

    @FXML
    private Label statOverallCountLabel;

    @FXML
    private Label statWorkerCountLabel;

    @FXML
    private Label statWarriorCountLabel;

    @FXML
    private VBox statContainer;

    @FXML
    private CheckBox infoCheckBox;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private RadioButton showTimerRadioButton;

    @FXML
    private RadioButton hideTimerRadioButton;

    @FXML
    private MenuItem startMenuItem;

    @FXML
    private MenuItem stopMenuItem;

    @FXML
    private CheckMenuItem infoMenuItem;

    @FXML
    private RadioMenuItem showTimerMenuItem;

    @FXML
    private RadioMenuItem hideTimerMenuItem;

    @FXML
    private TextField N1TextField;

    @FXML
    private TextField N2TextField;

    @FXML
    private ComboBox<Integer> P1ComboBox;

    @FXML
    private ComboBox<Integer> P2ComboBox;

    @FXML
    private TextField workerLifetimeTextField;

    @FXML
    private TextField warriorLifetimeTextField;

    @FXML
    private Button currentObjectsButton;

    @FXML
    private Button workersAIButton;

    @FXML
    private Button warriorsAIButton;

    private Habitat habitat;
    private AnimationTimer timer;

    private boolean simulationStarted = false;
    private boolean showInfo = true;

    private long startTime;
    private long pauseTime;
    private long timePassed;
    private long lastGeneratedFrame;
    private long min;
    private long sec;

    @FXML
    public void initElements() {
        initHabitat();
        initTimer();
        initButtonsAndKeys();
        initSettingsButtons();
        initAntsSettings();
    }

    private void initHabitat() {
        habitat = new Habitat(workspacePane);

        (workspacePane.getScene().getWindow()).setOnCloseRequest(windowEvent -> stop());
    }

    //TODO Сделать нормально организованный таймер
    private void initTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentFrame = l / 1_000_000_000;
                if (currentFrame - lastGeneratedFrame < 1) return;

                lastGeneratedFrame = currentFrame;
                timePassed = (System.nanoTime() - startTime) / 1_000_000_000;
                habitat.update(timePassed);
                updateTimer();
            }
        };
    }

    private void initButtonsAndKeys() {

        startButton.setOnAction(actionEvent -> start());
        startMenuItem.setOnAction(actionEvent -> start());

        stopButton.setOnAction(actionEvent -> pauseSimulation());
        stopMenuItem.setOnAction(actionEvent -> pauseSimulation());

        currentObjectsButton.setOnAction(actionEvent -> showCurrentObjectsDialog());

        rootScene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case KeyCode.B: { // Запуск
                    start();
                    break;
                }
                case KeyCode.E: { // Остановка
                    pauseSimulation();
                    break;
                }
                case KeyCode.T: {
                    toggleTimerVisibility();
                    break;
                }
            }
        });
    }

    private void initSettingsButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        showTimerRadioButton.setToggleGroup(toggleGroup);
        hideTimerRadioButton.setToggleGroup(toggleGroup);

        ToggleGroup menuToggleGroup = new ToggleGroup();
        showTimerMenuItem.setToggleGroup(menuToggleGroup);
        hideTimerMenuItem.setToggleGroup(menuToggleGroup);

        infoCheckBox.setOnAction(actionEvent -> toggleInfo());
        infoMenuItem.setOnAction(actionEvent -> toggleInfo());

        showTimerRadioButton.setOnAction(actionEvent -> showTimer());
        hideTimerRadioButton.setOnAction(actionEvent -> hideTimer());

        showTimerMenuItem.setOnAction(actionEvent -> showTimer());
        hideTimerMenuItem.setOnAction(actionEvent -> hideTimer());
    }

    private void initAntsSettings() {
        ObservableList<Integer> list = FXCollections.observableArrayList(0,10,20,30,40,50,60,70,80,90,100);

        P1ComboBox.setItems(list);
        P2ComboBox.setItems(list);

        P1ComboBox.setValue(60);
        P2ComboBox.setValue(40);

        P1ComboBox.setOnAction(actionEvent -> Habitat.setP1(P1ComboBox.getValue()));
        P2ComboBox.setOnAction(actionEvent -> Habitat.setP2(P2ComboBox.getValue()));

        N1TextField.setOnAction(actionEvent -> updateN1());
        N1TextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateN1();
        });

        N2TextField.setOnAction(actionEvent -> updateN2());
        N2TextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateN2();
        });

        workerLifetimeTextField.setOnAction(actionEvent -> updateWorkerLifetime());
        workerLifetimeTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateWorkerLifetime();
        });

        warriorLifetimeTextField.setOnAction(actionEvent -> updateWarriorLifetime());
        warriorLifetimeTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateWarriorLifetime();
        });
    }

    private void updateN1() {
        if (checkIfTextIsPositiveInteger(N1TextField.getText())) {
            Habitat.setN1(Integer.parseInt(N1TextField.getText()));
        }
        else {
            Habitat.setN1(2);
            N1TextField.setText("2");
            showErrorDialog();
        }
    }

    private void updateN2() {
        if (checkIfTextIsPositiveInteger(N2TextField.getText())) {
            Habitat.setN2(Integer.parseInt(N2TextField.getText()));
        }
        else {
            Habitat.setN2(3);
            N2TextField.setText("3");
            showErrorDialog();
        }
    }

    private void updateWorkerLifetime() {
        if (checkIfTextIsPositiveInteger(workerLifetimeTextField.getText())) {
            Habitat.setWorkersLifetime(Integer.parseInt(workerLifetimeTextField.getText()));
        }
        else {
            Habitat.setWorkersLifetime(5);
            workerLifetimeTextField.setText("5");
            showErrorDialog();
        }
    }

    private void updateWarriorLifetime() {
        if (checkIfTextIsPositiveInteger(warriorLifetimeTextField.getText())) {
            Habitat.setWarriorLifetime(Integer.parseInt(warriorLifetimeTextField.getText()));
        }
        else {
            Habitat.setWarriorLifetime(3);
            warriorLifetimeTextField.setText("3");
            showErrorDialog();
        }
    }

    private void start() {
        if (simulationStarted) return;

        startButton.setDisable(true);
        startMenuItem.setDisable(true);
        stopButton.setDisable(false);
        stopMenuItem.setDisable(false);

        startTime = System.nanoTime();
        lastGeneratedFrame = startTime / 1_000_000_000;

        resetTimer();

        simulationStarted = true;
        timer.start();
        habitat.startAI();
    }

    private void stop() {
        if (!simulationStarted) return;

        startButton.setDisable(false);
        startMenuItem.setDisable(false);
        stopButton.setDisable(true);
        stopMenuItem.setDisable(true);
        resetTimer();

        simulationStarted = false;
        timer.stop();

        habitat.clear();
        habitat.stopAI();
    }

    private void pauseSimulation() {
        if (!simulationStarted) return;
        if (!showInfo) stop();
        else showInfoDialog();
    }

    private void pauseTimer() {
        if (!simulationStarted) return;
        pauseTime = System.nanoTime();
        timer.stop();

    }

    private void resumeTimer() {
        if (!simulationStarted) return;
        startTime += System.nanoTime() - pauseTime;
        timer.start();
    }

    private void pauseWorkersAI() {

    }

    private void pauseWarriorsAI() {

    }

    private void resumeWorkersAI() {

    }

    private void resumeWarriorsAI() {

    }

    private void showInfoDialog() {
        pauseTimer();
        updateStat();

        InfoDialogWindow window = new InfoDialogWindow((Stage)rootScene.getScene().getWindow(),
                statOverallCountLabel.getText() + "\n" +
                        statWorkerCountLabel.getText() + "\n" +
                        statWarriorCountLabel.getText());

        window.show();
        int result = window.get();

        if (result == InfoDialogWindow.OK) {
            stop();
        }
        else if (result == InfoDialogWindow.CANCEL) {
            resumeTimer();
        }

    }

    private void showErrorDialog() {
        pauseTimer();
        ErrorDialogWindow window = new ErrorDialogWindow((Stage)rootScene.getScene().getWindow(),
                """
                        Ошибка при введение числа!
                        Значение должно быть целым числом и быть больше нуля!
                        """);
        window.show();
        resumeTimer();
    }

    private void showCurrentObjectsDialog() {
        pauseTimer();
        CurrentObjectsDialogWindow window = new CurrentObjectsDialogWindow(
                (Stage) (currentObjectsButton.getScene().getWindow()),
                habitat.getBirthTimes()
        );
        window.show();
        resumeTimer();
    }

    private void toggleTimerVisibility() {
       if (time.isVisible()) hideTimer();
       else showTimer();
    }

    private void showTimer() {
        time.setVisible(true);
        timerLabel.setVisible(true);

        showTimerRadioButton.setSelected(true);
        showTimerMenuItem.setSelected(true);
    }

    private void hideTimer() {
        time.setVisible(false);
        timerLabel.setVisible(false);

        hideTimerRadioButton.setSelected(true);
        hideTimerMenuItem.setSelected(true);
    }

    private void toggleInfo() {
        showInfo = !showInfo;
        infoCheckBox.setSelected(showInfo);
        infoMenuItem.setSelected(showInfo);
    }

    private void showStat() {
        workspacePane.setVisible(false);
        statContainer.setVisible(true);
        statContainer.setLayoutX((workspacePane.getWidth() - statContainer.getWidth()) / 2);
        statContainer.setLayoutY((workspacePane.getHeight() - statContainer.getHeight()) / 2);
    }

    private void hideStat() {
        workspacePane.setVisible(true);
        statContainer.setVisible(false);
    }

    private void updateStat() {
        statTimerLabel.setText(String.format("С начала симуляции прошло %02d:%02d (%02d сек)", min, sec, timePassed));
        statOverallCountLabel.setText(String.format("Сейчас жив(ы) %d муравей(ьев), из них", habitat.getAntsCount()));
        statWorkerCountLabel.setText(String.format("Рабочих: %d", habitat.getWorkersAntsCount()));
        statWarriorCountLabel.setText(String.format("Воинов: %d", habitat.getWarriorsAntsCount()));
    }

    private void updateTimer() {
        min = timePassed / 60;
        sec = timePassed - min*60;
        time.setText(String.format("%02d:%02d", min, sec));
    }

    private void resetTimer() {
        time.setText("00:00");
    }

    private void showInfo() {
        updateStat();

        InfoDialogWindow window = new InfoDialogWindow((Stage)rootScene.getScene().getWindow(),
                statOverallCountLabel.getText() + "\n" +
                statWorkerCountLabel.getText() + "\n" +
                statWarriorCountLabel.getText());

        int result = window.get();

        if (result == InfoDialogWindow.OK) {
            stop();
        }
        else if (result == InfoDialogWindow.CANCEL) {
            startTime += System.nanoTime() - pauseTime;
            timer.start();
        }
    }

    private boolean checkIfTextIsPositiveInteger(String string) {
        try {
            int num = Integer.parseInt(string);
            return num > 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

}
