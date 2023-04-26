package org.example.Simulation;

import org.example.Model.SelectionPolicy;
import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SimulationView extends JFrame {

    private JTextField timeLimitField;

    private JTextField[] serverTextFields;
    private JTextField tasksField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeField;
    private JLabel minServiceTimeLabel;
    private JLabel maxServiceTimeLabel;
    private JTextField minProcessingField;
    private JTextField maxProcessingField;
    private JTextField serversField;
    private JTextField clientsField;
    private  JComboBox operatorComboBox = new JComboBox(OPERATORS);

    private JLabel tasksLabel;
    private JPanel resultPanel;

    private static final String[] OPERATORS ={"Shortest Queue", "Shortest Time"};

    public SimulationView() {

        setTitle("Simulation Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        timeLimitField = new JTextField(10);
        minProcessingField = new JTextField(10);
        maxProcessingField = new JTextField(10);
        serversField = new JTextField(10);
        clientsField = new JTextField(10);
        minServiceTimeField = new JTextField(10);
        maxServiceTimeField = new JTextField(10);

        JLabel timeLimitLabel = new JLabel("Time Limit:");
        JLabel minProcessingLabel = new JLabel("Minimum Arrival Time:");
        JLabel maxProcessingLabel = new JLabel("Maximum Arrival Time:");
        JLabel operatorLabel = new JLabel("Policy :");
        JLabel serversLabel = new JLabel("Number of Servers:");
        JLabel clientsLabel = new JLabel("Number of Clients:");
        JLabel minSerciveTimeLabel = new JLabel("Minium Processing Time");
        JLabel maxServiceTimeLabel = new JLabel("Maximum Processing Time");

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(timeLimitLabel);
        inputPanel.add(timeLimitField);
        inputPanel.add(minProcessingLabel);
        inputPanel.add(minProcessingField);
        inputPanel.add(maxProcessingLabel);
        inputPanel.add(maxProcessingField);

        inputPanel.add(minSerciveTimeLabel);
        inputPanel.add(minServiceTimeField);
        inputPanel.add(maxServiceTimeLabel);
        inputPanel.add(maxServiceTimeField);
        inputPanel.add(serversLabel);
        inputPanel.add(serversField);
        inputPanel.add(clientsLabel);
        inputPanel.add(clientsField);
        inputPanel.add(operatorLabel);
        inputPanel.add(operatorComboBox);

        JButton startButton = new JButton("Start Simulation");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);

        resultPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.setVisible(false);

        tasksLabel = new JLabel("Tasks: 0");
        resultPanel.add(tasksLabel);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(resultPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            int timeLimit = Integer.parseInt(timeLimitField.getText());
            int minArrival = Integer.parseInt(minProcessingField.getText());
            int maxArrival = Integer.parseInt(maxProcessingField.getText());
            int numServers = Integer.parseInt(serversField.getText());
            int numClients = Integer.parseInt(clientsField.getText());
            int minService = Integer.parseInt(minProcessingField.getText());
            int maxService = Integer.parseInt(maxProcessingField.getText());
            SelectionPolicy policy;
            switch ((String) Objects.requireNonNull(operatorComboBox.getSelectedItem())) {
                case "Shortest Queue" -> {
                    policy = SelectionPolicy.SHORTEST_QUEUE;
                }
                case "Shortest Time" -> {
                    policy = SelectionPolicy.SHORTEST_TIME;
                }
                default -> policy = SelectionPolicy.SHORTEST_TIME;
            }

            JPanel newResultPanel = new JPanel(new GridLayout(numServers + 1, 2, 10, 10));
            newResultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


            JLabel[] serverLabels = new JLabel[numServers];
            serverTextFields = new JTextField[numServers];
            for (int i = 0; i < numServers; i++) {
                serverLabels[i] = new JLabel("Server " + (i + 1));
                serverTextFields[i] = new JTextField();
                serverTextFields[i].setEditable(false);
                newResultPanel.add(serverLabels[i]);
                newResultPanel.add(serverTextFields[i]);
            }

            JFrame resultFrame = new JFrame("Simulation Results");
            resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            resultFrame.setSize(400, 250);
            resultFrame.setLocationRelativeTo(null);
            resultFrame.setContentPane(newResultPanel);
            resultFrame.setVisible(true);

            JLabel tasksLabel = new JLabel("Tasks");
            tasksField = new JTextField();
            tasksField.setEditable(false);
            newResultPanel.add(tasksLabel);
            newResultPanel.add(tasksField);

            SimulationManager gen = new SimulationManager(timeLimit, minArrival, maxArrival, numServers, numClients, SimulationView.this, policy, minService, maxService );
            Thread t = new Thread(gen);
            t.start();


        });
    }
    public void updateServers(List<Server> servers) {
        for (int i = 0; i < servers.size(); i++) {
            if(servers.get(i).getTask() != null)
                serverTextFields[i].setText(servers.get(i).getTask().toString());
        }
    }
    public void updateTasks(ArrayList<Task> tasks) {
        tasksField.setText(tasks.toString());
    }


}

