/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weathersensors;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author THISURA THEJITH
 */
public class TemperatureSensor extends Thread {
 
    public static final String TYPE = "TEMP";
    private final long fiveMinsInMillis=1000;//300000; 
    private String location;
    private String serverName;
    
    private int port;
    private double currentTemperature;

    public TemperatureSensor(String serverName, int port, String location) {
        this.serverName = serverName;
        this.port = port;
        this.location = location;
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            try {
                if (counter == 12) {
                    List<String> source = new ArrayList<String>();
                    source.add(location);
                    source.add(TYPE);
                    currentTemperature = Math.random() * 40;
                    source.add(Double.toString(currentTemperature));
                    Socket client = new Socket(serverName, port);
                    OutputStream outToServer = client.getOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(outToServer);
                    out.writeObject(source);
                    counter = 1;
                } else {
                    currentTemperature = Math.random() * 30;
                    counter++;
                }
                try {
                    Thread.sleep(fiveMinsInMillis);
                    System.out.print("location :  " + location + " ");
                    System.out.println("Time waited : " + counter * 5 + " mins");
                } catch (InterruptedException ex) {
                    System.out.println("Hourly server update task interrupted");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    
    public String getLocation() {
        return location;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }
    
    
}
