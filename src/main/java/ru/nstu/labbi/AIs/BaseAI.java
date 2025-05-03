package ru.nstu.labbi.AIs;

import ru.nstu.labbi.Ants.Ant;
import ru.nstu.labbi.Containers;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public abstract class BaseAI {

    private Thread thread;
    protected final Containers containers = Containers.getInstance();

    private final Runnable runnable;

    private final String name;

    protected static final long FPS = 60;
    private static final long FRAME_TIME = 1_000_000_000 / FPS;

    public BaseAI(String name) {

        this.name = name;

        runnable = () -> {
            long startTime = System.nanoTime();
            long endTime = 0;
            int mills;
            int nanos;
            try {
                //System.out.println(Thread.currentThread().getName() +  " пробует зайти в цикл");
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (containers) {
                    //System.out.println(Thread.currentThread().getName() +  " в цикле и пробует пробится в кадр");

                        endTime = System.nanoTime();
                        long dt = endTime - startTime;

                         if (dt < FRAME_TIME) {
                            // System.out.println(Thread.currentThread().getName() +  " начал ждать " + FRAME_TIME + " ns");
                             nanos = (int)(FRAME_TIME - dt);
                             mills = nanos / 1_000_000;
                             nanos -= mills * 1_000_000;
                             //System.out.println(Thread.currentThread().getName() +  " ждет " + mills + "ms " + nanos + " ns");
                             Thread.sleep(mills, nanos);
                        }

                        //System.out.println(Thread.currentThread().getName() +  " в кадре щас будет двигать");
                        moveAnts();
                        startTime = endTime;
                    }
                }
            }
            catch (InterruptedException e) {
                System.out.println("Убило усыпленный поток");
            }
        };

        resetThread();
    }

    public void start() {
        if (!thread.isInterrupted()) resetThread();
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    private void resetThread() {
        thread = new Thread(runnable);
        thread.setName(name);
    }



    protected abstract void moveAnts();
}
