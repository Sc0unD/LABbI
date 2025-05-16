package ru.nstu.labbi.AIs;

import javafx.application.Platform;
import ru.nstu.labbi.Ants.Ant;
import ru.nstu.labbi.Ants.AntWorker;

public class WorkerAI extends BaseAI {

    private static final double V = 150.0 / FPS;
    int i = 0;

    public WorkerAI(String name) {
        super(name);
    }

//    @Override
//    protected void moveAnts() {
//        for (Ant ant : containers.getAnts()) {
//            if (!(ant instanceof AntWorker)) continue;
//
//            if (!((AntWorker) ant).isConfigure()) configureAnt((AntWorker) ant);
//
//            if (ant.getX() <= 0 && ant.getY() <= 0 ||
//                    ant.getX() >= ant.getStartX() && ant.getY() >= ant.getStartY()) {
//                ant.changeDirectionX();
//                ant.changeDirectionY();
//            }
//
//            StringBuilder sb = new StringBuilder();
//
//            sb.append("Проходка номер: ")
//                    .append(i)
//                    .append("\n Муравей с id: ")
//                    .append(ant.getId())
//                    .append("\n Стартовые коориднаты: (")
//                    .append(ant.getStartX())
//                    .append(", ")
//                    .append(ant.getStartY())
//                    .append(") \n Скорость: x -> ")
//                    .append(ant.getSpeedX())
//                    .append(" y -> ")
//                    .append(ant.getSpeedX())
//                    .append("В угол: ").append(AntWorker.getCornerX()).append(", ").append(AntWorker.getCornerY())
//                    .append("\n");
//
//            //System.out.println(sb.toString());
//
//            Platform.runLater(() -> {
//                synchronized (containers) {
//                    ant.move();
//                }
//            });
//        }
//        i++;
//    }

    @Override
    protected void moveAnts() {
        for (Ant ant : containers.getAnts()) {
            if (!(ant instanceof AntWorker)) continue;

            double dx, dy;

            if (((AntWorker)ant).isMovingToCorner()) {
                dx = AntWorker.getCornerX() - ant.getX();
                dy = AntWorker.getCornerY() - ant.getY();
            }
            else {
                dx = ant.getStartX() - ant.getX();
                dy = ant.getStartY() - ant.getY();
            }

            double distance = Math.sqrt(dx*dx + dy*dy);

            if (distance < 1) {
                ((AntWorker) ant).toggleMovingToCorner();
            }
            else {
                double x = ant.getX() + dx * V / distance;
                double y = ant.getY() + dy * V / distance;
                Platform.runLater(() -> {
                    ant.setX(x);
                    ant.setY(y);
                });
            }
        }
    }

//    private void configureAnt(AntWorker ant) {
//        double dx = AntWorker.getCornerX() - ant.getStartX();
//        double dy = AntWorker.getCornerY() - ant.getStartY();
//
//        double distance = Math.sqrt(dx*dx + dy*dy);
//
//        double directionX = dx / distance;
//        double directionY = dy / distance;
//
//        double moveX = directionX * V;
//        double moveY = directionY * V;
//
//        if (moveX < 1) moveX = 1;
//        if (moveY < 1) moveY = 1;
//
//        ant.setSpeedX(moveX);
//        ant.setSpeedY(moveY);
//
//        String s = String.format("""
//                Вектор: (%f, %f)
//                Расстояние: %f
//                Направление: (%f, %f)
//                Скорость: (%f, %f)\n\n
//                """, dx,dy,distance,directionX,directionY,moveX,moveY);
//
//        System.out.println("Муравей с id: " + ant.getId() + "настроен на \n" + s);
//
//        ant.setConfigureTrue();
//    }
}
