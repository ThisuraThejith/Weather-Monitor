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
import weatherserver.WeatherRMI;

/**
 *
 * @author THISURA THEJITH
 */
public class MonitoringStation extends UnicastRemoteObject implements StationRMI{

    private String location;
    public MonitoringStation() throws RemoteException {
        super();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public void bindStation(){
    try {
            Naming.rebind("rmi://localhost:5000/station/"+location, this);
            System.out.println("Station is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }
    }
    
    public void getTemp()throws NotBoundException, MalformedURLException, RemoteException{
        WeatherRMI temperature = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Temperature Measurement :" + temperature.getTemp("galle",location));
    }
    
    public void getRainfall()throws NotBoundException, MalformedURLException, RemoteException{
        WeatherRMI rainfall = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Rainfall Measurement :" + rainfall.getRainfall("galle",location));
    }
    
    public void getAirpressure()throws NotBoundException, MalformedURLException, RemoteException{
        WeatherRMI airpressure = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Airpressure Measurement :" + airpressure.getAirpressure("galle",location));
    }
    
    public void getHumidity()throws NotBoundException, MalformedURLException, RemoteException{
        WeatherRMI humidity = (WeatherRMI) Naming.lookup("rmi://localhost:5000/server");
        System.out.println("Humidity Measurement :" + humidity.getHumidity("galle",location));
    }
    
    @Override
    public void alert(String message) throws RemoteException{
        System.out.println("Alert received : " + message);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
             MonitoringStation station = new MonitoringStation();
             station.setLocation("kandy");
             station.bindStation();
    }

}
