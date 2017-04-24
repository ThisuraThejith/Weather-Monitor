/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherclient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import weatherserver.WeatherRMI;

/**
 *
 * @author THISURA THEJITH
 */
public class MonitoringStation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {

        WeatherRMI temperature = (WeatherRMI) Naming.lookup("rmi://localhost:5000/temperature");
        System.out.println("Temperature Measurement :" + temperature.getMeasurement());

        WeatherRMI rainfall = (WeatherRMI) Naming.lookup("rmi://localhost:5000/rainfall");
        System.out.println("Rainfall Measurement :" + rainfall.getMeasurement());

        WeatherRMI airpressure = (WeatherRMI) Naming.lookup("rmi://localhost:5000/airpressure");
        System.out.println("Airpressure Measurement :" + airpressure.getMeasurement());

        WeatherRMI humidity = (WeatherRMI) Naming.lookup("rmi://localhost:5000/humidity");
        System.out.println("Humidity Measurement :" + humidity.getMeasurement());
    }

}
