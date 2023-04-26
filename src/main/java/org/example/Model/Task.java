package org.example.Model;

public class Task implements Comparable<Task> {
    private int arrivalTime;
    private int serviceTime;

    public Task(int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    @Override
    public int compareTo(Task t) {
        return this.getArrivalTime() - t.getArrivalTime();
    }

    @Override
    public String toString() {
        return "{" + arrivalTime + " " + serviceTime + "}";
    }
}
