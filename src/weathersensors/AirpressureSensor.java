/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weathersensors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THISURA THEJITH
 */
public class AirpressureSensor extends Thread{
     
    private final long fiveMinsInMillis=300000; 
    private String location;
    private String serverName;
    private String type;
    private int port;
    private double currentAirpressure;

    public AirpressureSensor(String serverName, int port, String location) {
        this.serverName = serverName;
        this.port = port;
        this.location = location;
        this.type = "AIRPRESS";
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            try {
                if (counter == 12) {
                    List<String> source = new ArrayList<String>();
                    source.add(location+"_"+type);
                    source.add(type);
                    currentAirpressure = Math.random() * 30;
                    source.add(Double.toString(currentAirpressure));
                    Socket client = new Socket(serverName, port);
                    OutputStream outToServer = client.getOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(outToServer);
                    out.writeObject(source);
                    counter = 1;
                } else {
                    currentAirpressure = Math.random() * 30;
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

    public double getCurrentAirpressure() {
        return currentAirpressure;
    }
    
    
}
