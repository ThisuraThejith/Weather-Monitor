/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;
import java.io.*;

/**
 *
 * @author THISURA THEJITH
 */
public class WeatherServer extends Thread {

    private ServerSocket serverSocket;

    public WeatherServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port "
                        + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                        + "\nGoodbye!");
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Naming.rebind("rmi://localhost:5000/temperature", new TemperatureSensorSocket());
            Naming.rebind("rmi://localhost:5000/rainfall", new RainfallSensorSocket());
            Naming.rebind("rmi://localhost:5000/airpressure", new AirpressureSensorSocket());
            Naming.rebind("rmi://localhost:5000/humidity", new HumiditySensorSocket());
            System.out.println("Server is connected and ready for operation.");
        } catch (Exception e) {
            System.out.println("Server not connected: " + e);
        }

        int port = Integer.parseInt(args[0]);
        try {
            Thread t = new WeatherServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
