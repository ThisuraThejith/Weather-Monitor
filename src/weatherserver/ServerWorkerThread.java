/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weathersensors.TemperatureSensor;

/**
 *
 * @author THISURA THEJITH
 */
public class ServerWorkerThread extends Thread {
    private Socket socket;
    private WeatherServer server;
    private InputStream inputStream = null;
    private DataOutputStream out = null;
    private String location;
    private String sensorType;
    
    public ServerWorkerThread(Socket socket, WeatherServer server){
        this.server = server;
        this.socket = socket;
        try {
            socket.setSoTimeout(3600000);
        } catch (SocketException ex) {
            Logger.getLogger(ServerWorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(true){
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                ObjectInputStream in = (ObjectInputStream) inputStream;
                ArrayList<String> valueList = ((ArrayList<String>) in.readObject());
                location = valueList.get(0);
                sensorType = valueList.get(1);
                synchronized(this.server){
                    this.server.addSensor(location, sensorType, socket);
                    this.server.addWorker(location, sensorType, this);
                }
                double measurement = Double.parseDouble(valueList.get(2));
                if (TemperatureSensor.TYPE.equals(sensorType)) {
                    if (measurement > 35) {
                        synchronized(this.server){
                            this.server.sendAlertToStations("Temperature above 35 at" + location);
                        }
                    } else if (measurement < 20) {
                        synchronized(this.server){
                            this.server.sendAlertToStations("Temperature below 20 at" + location);
                        }
                    }
                }
                System.out.println("Measurement is " + location + "_" + sensorType + " " + measurement);

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                return;
            } catch (SocketTimeoutException ex){
                synchronized(this.server){
                    this.server.sendAlertToStations("Didn't receive " + sensorType + "alerts from " + location + ".");
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerWorkerThread.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }
    
    
    public String getTemp(String location, String clientLocation) throws RemoteException {
        try {
            //Socket sensor = sensorClients.get(location+"_TEMP");
            //write to the output strem
            out.writeBytes("getTemp");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       return Double.toString(Math.random() * 40);
    }
    
   
    public String getRainfall(String location, String clientLocation) throws RemoteException {
       //Socket sensor = sensorClients.get(location+"_RAIN");
       try {
            //Socket sensor = sensorClients.get(location+"_TEMP");
            //write to the output strem
            out.writeBytes("getRainfall");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       return Double.toString(Math.random() * 30);
    }
    
    
    public String getAirpressure(String location, String clientLocation) throws RemoteException {
      // Socket sensor = sensorClients.get(location+"_AIRPRESS");
       return Double.toString(Math.random() * 100);
    }
    
   
    public String getHumidity(String location, String clientLocation) throws RemoteException {
      // Socket sensor = sensorClients.get(location+"_HUMIDITY");
       return Double.toString(Math.random() * 50);
    }
}
