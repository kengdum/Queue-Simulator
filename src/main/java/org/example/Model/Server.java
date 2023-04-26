package org.example.Model;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class Server implements Runnable {
    private static int countId = 0;
    private final int id;
    private final LinkedBlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public AtomicInteger  getWaitingPeriod() {
        return  waitingPeriod;
    }

    public Server() {
        countId ++;
        this.id = countId;
        this.tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger();
    }
    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }
    public void run() {
        while (true) {
            try {
                Task newTask = tasks.peek();
                if (newTask != null) {
                    synchronized (newTask) {
                        while (newTask.getServiceTime() > 0) {
                            newTask.setServiceTime(newTask.getServiceTime() - 1);
                            Thread.sleep(1000);
                            waitingPeriod.decrementAndGet();
                        }
                    }
                    tasks.take();
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public int getServerProcessingTime() {
        int sum = 0;
        for(Task task : tasks) {
            sum = sum + (task.getServiceTime());
        }
        return sum;
    }
    public int getId() {
        return  this.id;
    }
    public ArrayList<Task> getTask() {
        return new ArrayList<>(this.tasks);
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", tasks=" + tasks +
                '}';
    }
}
