package ru.nstu.labbi;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.nstu.labbi.DialogWindows.CurrentObjectsDialogWindow;
import ru.nstu.labbi.DialogWindows.ErrorDialogWindow;
import ru.nstu.labbi.DialogWindows.InfoDialogWindow;

import java.io.*;
import java.util.Properties;

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

    @FXML
    private ComboBox<String> comboBoxPriorityWorkersAI;

    @FXML
    private ComboBox<String> comboBoxPriorityWarriorsAI;

    @FXML
    private MenuItem loadMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    private Habitat habitat;
    private AnimationTimer timer;

    private boolean simulationStarted = false;
    private boolean showInfo = true;
    private boolean isTimerShown = true;
    private boolean isWorkersAIEnabled = true;
    private boolean isWarriorsAIEnabled = true;
    private boolean isSimulationLoadedFromFile = false;

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
        readConfiguration();
    }

    private void initHabitat() {
        habitat = new Habitat(workspacePane);

        (workspacePane.getScene().getWindow()).setOnCloseRequest(windowEvent -> {
            stop();
            writeConfiguration();
        });
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

        workersAIButton.setOnAction(actionEvent -> toggleWorkersAI());
        warriorsAIButton.setOnAction(actionEvent -> toggleWarriorsAI());

        saveMenuItem.setOnAction(actionEvent -> saveToFile());
        loadMenuItem.setOnAction(actionEvent -> loadFromFile());
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

        ObservableList<String> list = FXCollections.observableArrayList("MIN_PRIORITY", "NORM_PRIORITY", "MAX_PRIORITY");

        comboBoxPriorityWorkersAI.setItems(list);
        comboBoxPriorityWarriorsAI.setItems(list);

        comboBoxPriorityWorkersAI.setValue(list.get(1));
        comboBoxPriorityWarriorsAI.setValue(list.get(1));

        comboBoxPriorityWorkersAI.setOnAction(actionEvent -> {
            String selected = comboBoxPriorityWorkersAI.getValue();
            int priority;

            switch (selected) {
                case "MIN_PRIORITY" -> priority = Thread.MIN_PRIORITY;
                case "MAX_PRIORITY" -> priority = Thread.MAX_PRIORITY;
                default -> priority = Thread.NORM_PRIORITY;
            }

            habitat.setWorkersAIPriority(priority);
        });

        comboBoxPriorityWarriorsAI.setOnAction(actionEvent -> {
            String selected = comboBoxPriorityWarriorsAI.getValue();
            int priority;

            switch (selected) {
                case "MIN_PRIORITY" -> priority = Thread.MIN_PRIORITY;
                case "MAX_PRIORITY" -> priority = Thread.MAX_PRIORITY;
                default -> priority = Thread.NORM_PRIORITY;
            }

            habitat.setWarriorsAIPriority(priority);
        });
    }

    private void initAntsSettings() {
        ObservableList<Integer> list = FXCollections.observableArrayList(0,10,20,30,40,50,60,70,80,90,100);

        P1ComboBox.setItems(list);
        P2ComboBox.setItems(list);

        P1ComboBox.setValue(60);
        P2ComboBox.setValue(40);

        P1ComboBox.setOnAction(actionEvent -> updateP1(P1ComboBox.getValue()));
        P2ComboBox.setOnAction(actionEvent -> updateP2(P2ComboBox.getValue()));

        N1TextField.setOnAction(actionEvent -> updateN1(N1TextField.getText()));
        N1TextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateN1(N1TextField.getText());
        });

        N2TextField.setOnAction(actionEvent -> updateN2(N2TextField.getText()));
        N2TextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateN2(N2TextField.getText());
        });

        workerLifetimeTextField.setOnAction(actionEvent -> updateWorkerLifetime(workerLifetimeTextField.getText()));
        workerLifetimeTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateWorkerLifetime(workerLifetimeTextField.getText());
        });

        warriorLifetimeTextField.setOnAction(actionEvent -> updateWarriorLifetime(warriorLifetimeTextField.getText()));
        warriorLifetimeTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) updateWarriorLifetime(warriorLifetimeTextField.getText());
        });
    }

    private void updateN1(String value) {
        if (checkIfTextIsPositiveInteger(value)) {
            Habitat.setN1(Integer.parseInt(value));
            N1TextField.setText(value);
        }
        else {
            Habitat.setN1(2);
            N1TextField.setText("2");
            showErrorDialog();
        }
    }

    private void updateN2(String value) {
        if (checkIfTextIsPositiveInteger(value)) {
            Habitat.setN2(Integer.parseInt(value));
            N2TextField.setText(value);
        }
        else {
            Habitat.setN2(3);
            N2TextField.setText("3");
            showErrorDialog();
        }
    }

    private void updateP1(int value) {
        Habitat.setP1(value);
        P1ComboBox.setValue(value);
    }

    private void updateP2(int value) {
        Habitat.setP2(value);
        P2ComboBox.setValue(value);
    }

    private void updateWorkerLifetime(String value) {
        if (checkIfTextIsPositiveInteger(value)) {
            Habitat.setWorkersLifetime(Integer.parseInt(value));
            workerLifetimeTextField.setText(value);
        }
        else {
            Habitat.setWorkersLifetime(5);
            workerLifetimeTextField.setText("5");
            showErrorDialog();
        }
    }

    private void updateWarriorLifetime(String value) {
        if (checkIfTextIsPositiveInteger(value)) {
            Habitat.setWarriorLifetime(Integer.parseInt(value));
            warriorLifetimeTextField.setText(value);
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
        if (!isSimulationLoadedFromFile) habitat.clear();

        isSimulationLoadedFromFile = false;
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

        simulationStarted = false;
        timer.stop();

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
        pauseWorkersAI();
        pauseWarriorsAI();
    }

    private void resumeTimer() {
        if (!simulationStarted) return;
        startTime += System.nanoTime() - pauseTime;
        timer.start();
        resumeWorkersAI();
        resumeWarriorsAI();
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
        isTimerShown = true;
        time.setVisible(true);
        timerLabel.setVisible(true);

        showTimerRadioButton.setSelected(true);
        showTimerMenuItem.setSelected(true);
    }

    private void hideTimer() {
        isTimerShown = false;
        time.setVisible(false);
        timerLabel.setVisible(false);

        hideTimerRadioButton.setSelected(true);
        hideTimerMenuItem.setSelected(true);
    }

    private void toggleInfo() {
        showInfo = !showInfo;
        if (showInfo) setShowInfoState();
        else setHideInfoState();
    }

    private void setShowInfoState() {
        infoCheckBox.setSelected(true);
        infoMenuItem.setSelected(true);
    }

    private void setHideInfoState() {
        infoCheckBox.setSelected(false);
        infoMenuItem.setSelected(false);
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

    private boolean checkIfTextIsPositiveInteger(String string) {
        try {
            int num = Integer.parseInt(string);
            return num > 0;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    private void toggleWorkersAI() {
        if (isWorkersAIEnabled) pauseWorkersAI();
        else resumeWorkersAI();
    }

    private void toggleWarriorsAI() {
        if (isWarriorsAIEnabled) pauseWarriorsAI();
        else resumeWarriorsAI();
    }

    private void pauseWorkersAI() {
        habitat.stopWorkersAI();
        isWorkersAIEnabled = false;
    }

    private void pauseWarriorsAI() {
        habitat.stopWarriorsAI();
        isWarriorsAIEnabled = false;
    }

    private void resumeWorkersAI() {
        habitat.resumeWorkersAI();
        isWorkersAIEnabled = true;
    }

    private void resumeWarriorsAI() {
        habitat.resumeWarriorsAI();
        isWarriorsAIEnabled = true;
    }

    private void loadFromFile() {
        stop();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save ants");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ants Files", "*.ants"));
        File file = fileChooser.showOpenDialog(workspacePane.getScene().getWindow());

        if (file != null) habitat.loadAnts(file);
        isSimulationLoadedFromFile = true;
    }

    private void saveToFile() {
        if (simulationStarted) pauseTimer();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load ants");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Ants Files", "*.ants"));
        File file = fileChooser.showSaveDialog(workspacePane.getScene().getWindow());

        if (file != null) habitat.saveAnts(file);
        if (simulationStarted) resumeTimer();
    }

    private void writeConfiguration() {
        Properties properties = new Properties();
        properties.setProperty("show_info", Boolean.toString(showInfo));
        properties.setProperty("show_timer", Boolean.toString(isTimerShown));
        properties.setProperty("N1", N1TextField.getText());
        properties.setProperty("N2", N2TextField.getText());
        properties.setProperty("P1", Integer.toString(P1ComboBox.getValue()));
        properties.setProperty("P2", Integer.toString(P2ComboBox.getValue()));
        properties.setProperty("Workers_Lifetime", workerLifetimeTextField.getText());
        properties.setProperty("Warriors_Lifetime", warriorLifetimeTextField.getText());

        try (FileOutputStream fileOutputStream = new FileOutputStream("config.properties")) {
            properties.store(fileOutputStream, "Ants Application Configuration");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readConfiguration() {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
            properties.load(fileInputStream);

            showInfo = Boolean.parseBoolean(properties.getProperty("show_info"));
            if (showInfo) setShowInfoState();
            else setHideInfoState();

            isTimerShown = Boolean.parseBoolean(properties.getProperty("show_timer"));
            if (isTimerShown) showTimer();
            else hideTimer();

            updateN1(properties.getProperty("N1"));
            updateN2(properties.getProperty("N2"));
            updateP1(Integer.parseInt(properties.getProperty("P1")));
            updateP2(Integer.parseInt(properties.getProperty("P2")));
            updateWorkerLifetime(properties.getProperty("Workers_Lifetime"));
            updateWarriorLifetime(properties.getProperty("Warriors_Lifetime"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
