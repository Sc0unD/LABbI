package ru.nstu.labbi.AIs;

import ru.nstu.labbi.Ants.Ant;
import ru.nstu.labbi.Ants.AntWarrior;

public class WarriorAI extends BaseAI {

    private static final int R = 80;
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

            double alpha = omega * ((AntWarrior) ant).getI();

             int x = (int)(R * Math.cos(alpha));
             int y = (int)(R* Math.sin(alpha));

            ant.setX((int)ant.getStartX() + x);
            ant.setY((int)ant.getStartY() + y);
        }
        //System.out.println(Thread.currentThread().getName() +  " приехало");

    }
}
