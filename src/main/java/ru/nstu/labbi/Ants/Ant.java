package ru.nstu.labbi.Ants;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Ant implements IBehaviour {

    private final ImageView imageView;
    private static final double IMG_WIDTH = 60.0;
    private static final double IMG_HEIGHT = 60.0;

    private long birthTime;
    private long lifeTime;

    protected double speedX = 10;
    protected double speedY = 10;

    protected double startX;
    protected double startY;

    protected int id;

    public Ant(Image image, double x, double y, long birthTime, long lifeTime, int id) {
        imageView = new ImageView(image);
        imageView.setFitWidth(IMG_WIDTH);
        imageView.setFitHeight(IMG_HEIGHT);
        setX(x);
        setY(y);
        this.birthTime = birthTime;
        this.lifeTime = lifeTime;
        this.id = id;

        startX = x;
        startY = y;
    }

    public Ant(Image image) {
        this(image,0,0,0,0,0);
    }

    public boolean isDead(long time) {
        return birthTime + lifeTime <= time;
    }

    public double getStartY() {
        return startY;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public static double getImgWidth() {
        return IMG_WIDTH;
    }

    public static double getImgHeight() {
        return IMG_HEIGHT;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setSpeedX(double sx) {
        speedX = sx;
    }

    public void setSpeedY(double sy) {
        speedY = sy;
    }

    @Override
    public void changeDirectionX() {
        speedX = -speedX;
        imageView.setScaleX(-1 * imageView.getScaleX());
    }

    @Override
    public void changeDirectionY() {
        speedY = -speedY;
    }

    @Override
    public void move() {
        //System.out.println(this.hashCode() + " " + getX() + " " + getY());
        //System.out.println("sx: " + speedX + " sy: " + speedY);
        setX(getX() + speedX);
        setY(getY() + speedY);
        //System.out.println(this.hashCode() + " " + getX() + " " + getY() + "\n");
    }

    @Override
    public void setX(double x) {
        imageView.setX(x);
    }

    @Override
    public void setY(double y) {
        imageView.setY(y);
    }

    @Override
    public double getX() {
        return imageView.getX();
    }

    @Override
    public double getY() {
        return imageView.getY();
    }

    public void setBirthTime(long birthTime) {
        this.birthTime = birthTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }
}
