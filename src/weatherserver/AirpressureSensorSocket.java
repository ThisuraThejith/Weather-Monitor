/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

/**
 *
 * @author THISURA THEJITH
 */
public class AirpressureSensorSocket extends UnicastRemoteObject implements WeatherRMI {

    public AirpressureSensorSocket() throws RemoteException {
        super();
    }

    @Override
    public String getMeasurement() throws RemoteException {
        String airpressure = "1011";
        return airpressure;
    }
}
