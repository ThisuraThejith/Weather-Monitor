/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import weatherserver.WeatherRMI;

/**
 *
 * @author THISURA THEJITH
 */
public class MonitoringStation extends UnicastRemoteObject implements Runnable, StationRMI {

    private String stationLocation;

    public MonitoringStation() throws RemoteException {
        super();
    }

    public String getLocation() {
        return stationLocation;
    }

    public void setLocation(String location) {
        this.stationLocation = location;
    }

    public void bindStation() {
        try {
            Naming.rebind("rmi://localhost:5000/station/" + stationLocation, this);
            System.out.println("Station is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }
    }

    public void getTemp(String location) throws NotBoundException, MalformedURLException, RemoteException {
        WeatherRMI temperature = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Temperature Measurement :" + temperature.getTemp(location, stationLocation));
    }

    public void getRainfall(String location) throws NotBoundException, MalformedURLException, RemoteException {
        WeatherRMI rainfall = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Rainfall Measurement :" + rainfall.getRainfall(location, stationLocation));
    }

    public void getAirpressure(String location) throws NotBoundException, MalformedURLException, RemoteException {
        WeatherRMI airpressure = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Airpressure Measurement :" + airpressure.getAirpressure(location, stationLocation));
    }

    public void getHumidity(String location) throws NotBoundException, MalformedURLException, RemoteException {
        WeatherRMI humidity = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Humidity Measurement :" + humidity.getHumidity(location, stationLocation));
    }

    public void getNumberOfSensors() throws NotBoundException, MalformedURLException, RemoteException {
        WeatherRMI server = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Humidity Measurement :" + server.getNumberofSensors());
    }

    @Override
    public void alert(String message) throws RemoteException {
        System.out.println("Alert received : " + message);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Hi Monitoring Station,please input the no of the choice you want :");
            System.out.println("[1]View weather details");
            System.out.println("[2]View location of the sensors");
            System.out.println("[3]View no of sensors connected");
            //System.out.println("[4]View weather details");
            System.out.print("Enter your option :");
            Scanner reader = new Scanner(System.in);
            //System.out.println("Enter a number: ");
            int n = reader.nextInt();

            if (n == 1) {
                try {
                    System.out.print("Enter location to see report :");
                    Scanner scr = new Scanner(System.in);
                    String sensorlocation = scr.nextLine();
                    getTemp(sensorlocation);
                    getRainfall(sensorlocation);
                    getHumidity(sensorlocation);
                    getAirpressure(sensorlocation);
                } catch (NotBoundException ex) {
                    Logger.getLogger(MonitoringStation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MonitoringStation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(MonitoringStation.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (n == 2) {
                //location
            } else if (n == 3) {
                //no of sensors
            } else {
                System.out.println("Enter a valid option");
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
        MonitoringStation station = new MonitoringStation();
        station.setLocation("kandy");
        station.bindStation();
        new Thread(station).start();
    }

}
