package org.example.Model;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        if(!servers.isEmpty()) {
            Server min = getMinimumProccesingTimeServer(servers);
            min.addTask(t);
        }
    }
    public Server getMinimumProccesingTimeServer(List<Server> servers) {
        Server min = null;
        int minimumTime = 999999;
        for(Server server : servers) {
            if (minimumTime > server.getServerProcessingTime())
            {
                minimumTime = server.getServerProcessingTime();
                min = server;
            }
        }
        return min;
    }
}
