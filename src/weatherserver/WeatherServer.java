/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weatherclient.MonitoringStation;
import weatherclient.StationRMI;
import weathersensors.TemperatureSensor;

/**
 *
 * @author THISURA THEJITH
 */
public class WeatherServer extends UnicastRemoteObject implements Runnable,WeatherRMI {

    private Map<String, Socket> sensorClients;
    private Map<String, ServerWorkerThread> workers;
    private Map<String,String> stations;
    private ServerSocket serverSocket;

    public WeatherServer(int port) throws IOException {
        sensorClients = new HashMap<String, Socket>();
        stations = new HashMap<String,String>();
        workers = new HashMap<String, ServerWorkerThread>();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(100000);
    }

    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerWorkerThread(socket,this).start();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    
    
    public void addWorker(String location, String type, ServerWorkerThread worker) {
        workers.put(location + "_" + type, worker);
    }
    
    public void addSensor(String location, String type, Socket socket) {
        sensorClients.put(location + "_" + type, socket);
    }

    public void removeSensor(String location, String type){
       sensorClients.remove(location + "_" + type);
    }
    
    public void addStation(String location){
        stations.put(location, location);
    }
    
    public void removeStation(String location){
        stations.remove(location);
    }
    
    public void bindRMIServer() {
        try {
            Naming.rebind("rmi://localhost:5000/server", this);
            System.out.println("Server is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }
    }

    
    /////RMI methods
    @Override
    public int getNumberofSensors(){
       return this.sensorClients.size();
    }
    
    @Override
    public int getNumberOfStations(){
       return this.stations.size();
    }
    
    @Override
    public String getTemp(String location, String clientLocation) throws RemoteException {
        addStation(clientLocation);
        return workers.get(location + "_" + TemperatureSensor.TYPE).getTemp(location, clientLocation);
    }
    
    @Override
    public String getRainfall(String location, String clientLocation) throws RemoteException {
       addStation(clientLocation);
       return "";
    }
    
    @Override
    public String getAirpressure(String location, String clientLocation) throws RemoteException {
       addStation(clientLocation);
       return "";
    }
    
    @Override
    public String getHumidity(String location, String clientLocation) throws RemoteException {
       addStation(clientLocation);
       return "";
    }
    
    public void sendAlertToStations(String message) {
        String[] names;
        try {
            names = Naming.list("rmi://localhost:5000/station/");
            for (String name : names) {
                System.out.println("Station Name :"+name);
                if (name.contains("station")) {
                    StationRMI station = (StationRMI) Naming.lookup(name);
                    station.alert(message);
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WeatherServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException nb) {
        }

    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 6066;
        WeatherServer server;
        try {
            server = new WeatherServer(port);
            server.bindRMIServer();
            Thread t = new Thread(server);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
