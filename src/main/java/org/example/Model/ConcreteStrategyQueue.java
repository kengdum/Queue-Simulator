package org.example.Model;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        if(!servers.isEmpty()) {
            Server min = getMinimumSizeServer(servers);
            min.addTask(t);
        }
    }
    public Server getMinimumSizeServer(List<Server> servers) {
        Server min = null;
        int minimumValue = 999999;
        for(Server server : servers) {
            if(minimumValue > server.getTask().size())
            {
                minimumValue = server.getTask().size();
                min = server;
            }
        }
        return min;
    }
}
