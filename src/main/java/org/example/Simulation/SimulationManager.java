package org.example.Simulation;

import org.example.Model.Scheduler;
import org.example.Model.SelectionPolicy;
import org.example.Model.Server;
import org.example.Model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int maxProcessingTime;
    private  int minProcessingTime;
    private int minServiceTime;
    private int maxServiceTime;
    private int numberOfServers;
    private int numberOfClients;
    public SelectionPolicy selectionPolicy;
    private Scheduler scheduler;
    private List<Task> generatedTasks;

    private static int currentTime = 0;
    private SimulationView view;

    public List<Task> getGeneratedTasks() {
        return generatedTasks;
    }

    public SimulationManager(int timeLimit, int minProcessingTime, int maxProcessingTime, int numberOfServers, int numberOfClients, SimulationView view, SelectionPolicy policy, int minServiceTime, int maxServiceTime) {
        this.timeLimit = timeLimit;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxProcessingTime;
        this.view = view;
        this.selectionPolicy = policy;

        scheduler = new Scheduler(numberOfServers, 5);
        scheduler.changeStrategy(selectionPolicy);

        for(int i = 0; i < numberOfServers; i++ ) {
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
        }
        generatedTasks = new ArrayList<>();
        generateNRandomTasks();
    }
    public void generateNRandomTasks() {
        int n = numberOfClients;
        for(int i = 0; i < n; i++) {
            int arrivalTime = (int)(Math.floor(Math.random()*(maxProcessingTime-minProcessingTime+1)));
            int serviceTime = (int) (Math.floor(Math.random() * (maxServiceTime-minServiceTime+1))) + 1;
            Task newTask = new Task(arrivalTime, serviceTime);
            generatedTasks.add(newTask);
        }
       Collections.sort(generatedTasks);
    }
    @Override
    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.txt")));
            int averageServiceTimeValue = this.averageServiceTime();
            int peakHour = 0;
            int time = 0;
            float averageWaitingPeriod = (float) 0;
            while (currentTime < timeLimit) {
                synchronized (generatedTasks) {
                    for (Task task : generatedTasks) {
                        if (task.getArrivalTime() == currentTime) {
                            scheduler.dispatchTask(task);
                        }
                    }
                }
                if(peakHour <= this.getNumberOfTasks())
                {
                    peakHour = this.getNumberOfTasks();
                    time = currentTime;
                }
                averageWaitingPeriod = averageWaitingPeriod + this.getAverageWaitingPeriod();
                System.out.println(this.getNumberOfTasks());
                writer.write(String.format("Time:%d\n", currentTime));
                for (Server server : scheduler.getServers()) {
                    writer.write(server.toString()+ "\n");
                    view.updateServers(scheduler.getServers());
                    view.updateTasks((ArrayList<Task>) generatedTasks);
                }
                writer.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                currentTime++;
            }
            writer.write(String.format("Average Service time %d\n", averageServiceTimeValue));
            writer.write(String.format("Peak hour %d\n", time));
            writer.write(String.format("Average waiting time %f", averageWaitingPeriod/numberOfClients));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getNumberOfTasks() {
        int sum = 0;
       for(Server server : scheduler.getServers()) {
           sum = sum + server.getTask().size();
       }
       return sum;
    }
    public int averageServiceTime() {
        int sum = 0;
        for(Task task : generatedTasks) {
            sum = sum + task.getServiceTime();
        }
        return sum/numberOfClients;
    }
    public float getAverageWaitingPeriod() {
        AtomicInteger value = new AtomicInteger();
        for(Server server : scheduler.getServers()) {
            value.addAndGet(server.getWaitingPeriod().get());
        }
        return (float)(value.get())/numberOfServers;
    }
}
