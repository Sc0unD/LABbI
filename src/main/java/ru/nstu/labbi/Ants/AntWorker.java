package ru.nstu.labbi.Ants;

import javafx.scene.image.Image;
import ru.nstu.labbi.Habitat;

import java.util.Objects;

public class AntWorker extends Ant {

    private static final Image image = new Image(Objects.requireNonNull(Habitat.class.getResource("Images/black ant.png")).toExternalForm());

    private boolean isMovingToCorner = true;

    private static final double cornerX = 0;
    private static final double cornerY = 0;

    public AntWorker(double x, double y, long birthTime, long lifeTime, int id) {
        super(image, x, y, birthTime, lifeTime, id);
    }

    public AntWorker() {
        this(0, 0,0,0,0);
    }

    public static double getCornerX() {
        return cornerX;
    }

    public static double getCornerY() {
        return cornerY;
    }

    public boolean isMovingToCorner() {
        return isMovingToCorner;
    }

    public void toggleMovingToCorner() {
        isMovingToCorner = !isMovingToCorner;
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
