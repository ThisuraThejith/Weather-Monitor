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

/**
 *
 * @author THISURA THEJITH
 */
public class WeatherServer extends UnicastRemoteObject implements Runnable,WeatherRMI {

    private Map<String, Socket> sensorClients;
    private List<MonitoringStation> stations;
    private ServerSocket serverSocket;

    public WeatherServer(int port) throws IOException {
        sensorClients = new HashMap<String, Socket>();
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(100000);
    }

    public void run() {
        while (true) {
            try {
                Socket server = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                try {
                    ArrayList<String> valueList = ((ArrayList<String>) in.readObject());
                    String location = valueList.get(0);
                    String type = valueList.get(1);
                    double measurement = Double.parseDouble(valueList.get(2));
                    if ("TEMP".equals(type)) {
                        if (measurement > 30) {
                           sendAlertToStations("Temperature above 30 at" + location);
                        } else if (measurement < 20) {
                           sendAlertToStations("Temperature below 20 at" + location);
                        }
                    }
                    System.out.println("Measurement is " + location+"_"+type + " " + measurement);
                    sensorClients.put(location+"_"+type, server);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                server.close();

            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void bindRMIServer() {
        try {
            Naming.rebind("rmi://localhost:5000/server", this);
            System.out.println("Server is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }
    }

    @Override
    public String getTemp(String location, String clientLocation) throws RemoteException {
       Socket sensor = sensorClients.get(location+"_TEMP");
       
       return "";
    }
    
    @Override
    public String getRainfall(String location, String clientLocation) throws RemoteException {
       Socket sensor = sensorClients.get(location+"_RAIN");
       return "";
    }
    
    @Override
    public String getAirpressure(String location, String clientLocation) throws RemoteException {
       Socket sensor = sensorClients.get(location+"_AIRPRESS");
       return "";
    }
    
    @Override
    public String getHumidity(String location, String clientLocation) throws RemoteException {
       Socket sensor = sensorClients.get(location+"_HUMIDITY");
       return "";
    }
    
    private void sendAlertToStations(String message) {
        String[] names;
        try {
            names = Naming.list("rmi://localhost:5000/station/");
            for (String name : names) {
                StationRMI station = (StationRMI) Naming.lookup(name);
                station.alert(message);
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
