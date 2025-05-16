package ru.nstu.labbi.AIs;

import javafx.application.Platform;
import ru.nstu.labbi.Ants.Ant;
import ru.nstu.labbi.Ants.AntWarrior;

public class WarriorAI extends BaseAI {

    public static final int R = 80;
    private static final double V = R * Math.PI / FPS;
    private static final double omega = V / R;

    public WarriorAI(String name) {
        super(name);
    }

    @Override
    protected void moveAnts() {
        //System.out.println(Thread.currentThread().getName() +  " пошло поехало");
        for (Ant ant : containers.getAnts()) {
            if (!(ant instanceof AntWarrior)) continue;

            double alpha = ((AntWarrior)ant).getAngle(omega);

            double x = R * Math.cos(alpha);
            double y = R * Math.sin(alpha);

            Platform.runLater(() -> {
                ant.setX(ant.getStartX() + x);
                ant.setY(ant.getStartY() + y);
            });
        }
        //System.out.println(Thread.currentThread().getName() +  " приехало");

    }
}
