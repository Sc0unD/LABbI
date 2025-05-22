package ru.nstu.labbi.Ants;

import javafx.scene.image.Image;
import ru.nstu.labbi.Habitat;

import java.util.Objects;

public class AntWarrior extends Ant {

    private static final Image image = new Image(Objects.requireNonNull(Habitat.class.getResource("Images/red ant.png")).toExternalForm());

    private double angle = Math.PI * 3 / 2;

    public AntWarrior(double x, double y, long birthTime, long lifeTime, int id) {
        super(image, x, y, birthTime, lifeTime, id);
    }

    public AntWarrior() {
        this(0, 0,0,0, 0);
    }

    public double getAngle(double alpha) {
        double oldAngle = angle;
        angle += alpha;
        return oldAngle;
    }

    @Override
    public void resetImage() {
        setImageView(image);
        imageView.setX(posX);
        imageView.setY(posY);
        imageView.setFitWidth(IMG_WIDTH);
        imageView.setFitHeight(IMG_HEIGHT);
    }
}
