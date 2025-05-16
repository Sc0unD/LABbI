package ru.nstu.labbi;

import java.util.*;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ru.nstu.labbi.AIs.WarriorAI;
import ru.nstu.labbi.AIs.WorkerAI;
import ru.nstu.labbi.Ants.*;

public class Habitat {

    private static final double WORKSPACE_WIDTH = 1280 - 26;
    private static final double WORKSPACE_HEIGHT = 720 - 14 - 57;

    private static final Image backgroundImage =
            new Image(Objects.requireNonNull(Habitat.class.getResource("Images/anthill.png")).toExternalForm());

    private static final Random random = new Random();

    private static int N1 = 2;
    private static int N2 = 3;
    private static int P1 = 60;
    private static int P2 = 40;

    private static long workersLifetime = 5;
    private static long warriorLifetime = 3;

    private final Pane mainPane;

    private final Containers containers;

    private final WorkerAI workerAI;
    private final WarriorAI warriorAI;



    public Habitat(Pane pane) {
        mainPane = pane;
        setBackGround();

        containers = Containers.getInstance();

        workerAI = new WorkerAI("Workers");
        warriorAI = new WarriorAI("Warriors");

        workerAI.setPriority(Thread.NORM_PRIORITY);
        warriorAI.setPriority(Thread.NORM_PRIORITY);
    }

    public void setBackGround() {
        ImageView imageView = new ImageView(backgroundImage);
        mainPane.getChildren().add(imageView);
        imageView.setFitWidth(mainPane.getPrefWidth());
        imageView.setFitHeight(mainPane.getPrefHeight());
        imageView.setX(0);
        imageView.setY(0);
    }

    public void update(long time) {
        synchronized (containers) {
            if (canWorkerBeBorn(time)) addWorker(time);
            if (canWarriorBeBorn(time)) addWarrior(time);
            containers.removeDeadAnts(time, mainPane);
        }
    }

    private boolean canWorkerBeBorn(long time) {
        return (time % N1 == 0) && (random.nextInt(1,101) <= P1);
    }

    private boolean canWarriorBeBorn(long time) {
        return (time % N2 == 0) && (random.nextInt(1,101) <= P2);
    }

    private void addWorker(long time) {
        AntWorker antWorker = new AntWorker();
        addAnt(antWorker, time);
    }

    private void addWarrior(long time) {
        AntWarrior antWarrior = new AntWarrior();
        addAnt(antWarrior, time);
    }

    private void addAnt(Ant ant , long time) {


        //System.out.println(ant.getStartX() + "  " + ant.getStartY());

        ant.setBirthTime(time);

        long lifetime = 0;
        int speedX = 0;
        int speedY = 0;

        double x = 0;
        double y = 0;

        if (ant instanceof AntWorker) {
            lifetime = workersLifetime;
            while (x < mainPane.getWidth()*0.3 && y < mainPane.getHeight()*0.3) {
                x = random.nextDouble(mainPane.getWidth()*0.3, mainPane.getWidth() - Ant.getImgWidth());
                y = random.nextDouble(mainPane.getHeight()*0.3, mainPane.getHeight() - Ant.getImgHeight());
            }
            ant.setStartY(y);

        }
        if (ant instanceof AntWarrior) {
            lifetime = warriorLifetime;
            x = random.nextDouble(WarriorAI.R * 1.05, mainPane.getWidth() - Ant.getImgWidth() - WarriorAI.R * 1.05);
            y = random.nextDouble(0, mainPane.getHeight() - Ant.getImgHeight() - WarriorAI.R * 1.05 * 2);
            ant.setStartY(y + WarriorAI.R);
        }

        ant.setStartX(x);
        ant.setX(x);
        ant.setY(y);

        ant.setLifeTime(lifetime);
        ant.setSpeedX(speedX);
        ant.setSpeedY(speedY);
        if (random.nextBoolean()) ant.changeDirectionX();
        if (random.nextBoolean()) ant.changeDirectionY();

        int id = random.nextInt(10000,100000);
        while (containers.containsId(id)) id = random.nextInt(10000,100000);
        ant.setId(id);

        containers.addAnt(ant);
        mainPane.getChildren().add(ant.getImageView());

    }

    private void moveAnts() {
        for (Ant ant : containers.getAnts()) {
            double x = ant.getX();
            double y = ant.getY();

            if (x <= 0 || x >= mainPane.getWidth() - Ant.getImgWidth())
                ant.changeDirectionX();

            if (y <= 0 || y >= mainPane.getHeight() - Ant.getImgHeight())
                ant.changeDirectionY();

            ant.move();
        }
    }

    public void clear() {
        mainPane.getChildren().clear();
        containers.clear();
        setBackGround();
    }

    public long getAntsCount() { return containers.getAntsCount(); }

    public long getWorkersAntsCount() { return containers.getWorkersCount(); }

    public long getWarriorsAntsCount() { return containers.getWarriorsCount(); }

    public Image getBackgroundImage() { return backgroundImage; }

    public static void setN1(int n1) { N1 = n1; }

    public static void setN2(int n2) { N2 = n2; }

    public static void setP1(int p1) { P1 = p1; }

    public static void setP2(int p2) { P2 = p2; }

    public static void setWorkersLifetime(long workersLifetime) { Habitat.workersLifetime = workersLifetime; }

    public static void setWarriorLifetime(long warriorLifetime) { Habitat.warriorLifetime = warriorLifetime; }

    public TreeMap<Integer, Long> getBirthTimes() { return containers.getBirthTimes(); }

    public void startAI() {
        workerAI.start();
        warriorAI.start();
    }

    public void stopAI() {
        resumeWorkersAI();
        resumeWarriorsAI();
        workerAI.stop();
        warriorAI.stop();
    }

    public void stopWorkersAI() {
        workerAI.pause();
    }

    public void stopWarriorsAI() {
        warriorAI.pause();
    }

    public void resumeWorkersAI() {
        synchronized (workerAI) {
            workerAI.resume();
            workerAI.notify();
        }
    }

    public void resumeWarriorsAI() {
        synchronized (warriorAI) {
            warriorAI.resume();
            warriorAI.notify();
        }
    }

    public void setWorkersAIPriority(int i) {
        workerAI.setPriority(i);
    }

    public void setWarriorsAIPriority(int i) {
        warriorAI.setPriority(i);
    }
}
