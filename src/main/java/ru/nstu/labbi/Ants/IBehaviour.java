package ru.nstu.labbi.Ants;

public interface IBehaviour {

    void move();
    void setX(double x);
    void setY(double y);
    double getX();
    double getY();
    void changeDirectionX();
    void changeDirectionY();
}
