package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTaskPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTaskPerServer) {
        this.servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
        this.maxTaskPerServer = maxTaskPerServer;
        for (int i = 0; i < maxNoServers; i ++) {
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
    }
    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }
    public List<Server> getServers() {
        return  servers;
    }
    public synchronized void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Server server : servers)
            stringBuilder.append(server.toString());
        return stringBuilder.toString();
    }
}
