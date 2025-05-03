package ru.nstu.labbi.Ants;

import javafx.scene.image.Image;
import ru.nstu.labbi.Habitat;

import java.util.Objects;
import java.util.Random;

public class AntWarrior extends Ant {

    private static final Image image = new Image(Objects.requireNonNull(Habitat.class.getResource("Images/red ant.png")).toExternalForm());

    private double i = 0;

    public AntWarrior(double x, double y, long birthTime, long lifeTime, int id) {
        super(image, x, y, birthTime, lifeTime, id);
    }

    public AntWarrior() {
        this(0, 0,0,0, 0);
    }

    public double getI() {
        return i++;
    }
}
