package ru.nstu.labbi.AIs;

import ru.nstu.labbi.Ants.Ant;
import ru.nstu.labbi.Ants.AntWorker;

public class WorkerAI extends BaseAI {

    private static final double V = 150.0 / FPS;

    public WorkerAI(String name) {
        super(name);
    }

    @Override
    protected void moveAnts() {
        //System.out.println(Thread.currentThread().getName() +  "пошло поехало");
        for (Ant ant : containers.getAnts()) {
            if (!(ant instanceof AntWorker)) continue;

            if (!((AntWorker) ant).isConfigure()) configureAnt((AntWorker) ant);

            if (ant.getX() < 0 && ant.getY() < 0 ||
                    ant.getX() > ant.getStartX() && ant.getY() > ant.getStartY()) {
                ant.changeDirectionX();
                ant.changeDirectionY();
            }

            ant.move();
        }
        //System.out.println(Thread.currentThread().getName() +  "приехало");

    }

    private void configureAnt(AntWorker ant) {

        double dx = AntWorker.getCornerX() - ant.getStartX();
        double dy = AntWorker.getCornerY() - ant.getStartY();

        double distance = Math.sqrt(dx*dx + dy*dy);

        double directionX = dx / distance;
        double directionY = dy / distance;

        double moveX = directionX * V;
        double moveY = directionY * V;

        ant.setSpeedX(moveX);
        ant.setSpeedY(moveY);

        ant.setConfigureTrue();
    }
}
