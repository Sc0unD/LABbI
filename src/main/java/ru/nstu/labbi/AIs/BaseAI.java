package ru.nstu.labbi.AIs;

import ru.nstu.labbi.Containers;

public abstract class BaseAI {

    private Thread thread;
    protected final Containers containers = Containers.getInstance();

    private final Runnable runnable;

    private final String name;

    private boolean isRunning = true;

    protected static final long FPS = 60;
    private static final long FRAME_TIME = 1_000_000_000 / FPS;

    public BaseAI(String name) {

        this.name = name;

        runnable = () -> {
            long startTime = System.nanoTime();
            long endTime;
            int mills;
            int nanos;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (this) {
                        if (!isRunning) {
                            try {
                                wait();
                            }
                            catch (InterruptedException e) {
                                System.out.println("Поток " + name + " немного прерван зайдите позже");
                            }
                        }
                    }

                    endTime = System.nanoTime();
                    long dt = endTime - startTime;
                    if (dt < FRAME_TIME) {
                         nanos = (int)(FRAME_TIME - dt);
                         mills = nanos / 1_000_000;
                         nanos -= mills * 1_000_000;
                         Thread.sleep(mills, nanos);
                    }
                    synchronized (containers) {
                        moveAnts();
                        startTime = System.nanoTime();
                    }
                }
            }
            catch (InterruptedException e) {
                System.out.println("Поток " + name + " немного собрался помирать");
            }
        };

        resetThread();
    }

    public void start() {
        if (thread.isInterrupted()) resetThread();
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    private void resetThread() {
        thread = new Thread(runnable);
        thread.setName(name);
    }

    public void pause() {
        isRunning = false;
    }

    public void resume() {
        isRunning = true;
    }

    public void setPriority(int i) {
        thread.setPriority(i);
    }

    protected abstract void moveAnts();
}
