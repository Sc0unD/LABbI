package ru.nstu.labbi;

import javafx.scene.layout.Pane;
import ru.nstu.labbi.Ants.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

public class Containers {

    private static class Holder {
        private final static Containers instance = new Containers();
    }

    private final Vector<Ant> ants;
    private final HashSet<Integer> identifiers;
    private final TreeMap<Integer, Long> birthTimes;

    private Containers() {
        ants = new Vector<>();
        identifiers = new HashSet<>();
        birthTimes = new TreeMap<>();
    }

    public static Containers getInstance() {
        return Holder.instance;
    }

    public void clear() {
        ants.clear();
        identifiers.clear();
        birthTimes.clear();
    }

    public void addAnt(Ant ant) {
        ants.add(ant);
        identifiers.add(ant.getId());
        birthTimes.put(ant.getId(), ant.getBirthTime());
    }

    public boolean containsId(int id) {
        return identifiers.contains(id);
    }

    public void removeDeadAnts(long t, Pane pane) {
        Iterator<Ant> iterator = ants.iterator();
        while (iterator.hasNext()) {
            try {
                Ant ant = iterator.next();
                if (ant.isDead(t)) {
                    iterator.remove();
                    identifiers.remove(ant.getId());
                    birthTimes.remove(ant.getId());
                    pane.getChildren().remove(ant.getImageView());
                }
            } catch (Exception e) {
                System.out.println("Ми удалять муарей");
                e.printStackTrace();
            }
        }
    }

    public Vector<Ant> getAnts() {
        return ants;
    }

    public HashSet<Integer> getIdentifiers() {
        return identifiers;
    }

    public TreeMap<Integer, Long> getBirthTimes() {
        return birthTimes;
    }

    public long getAntsCount() {
        return ants.size();
    }

    public long getWorkersCount() {
        return ants.stream().filter(ant -> ant instanceof AntWorker).count();
    }

    public long getWarriorsCount() {
        return ants.stream().filter(ant -> ant instanceof AntWarrior).count();
    }

}
