/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author THISURA THEJITH
 */
public class TemperatureSensorSocket extends UnicastRemoteObject implements WeatherRMI {

    public TemperatureSensorSocket() throws RemoteException {
        super();
    }

    @Override
    public String getMeasurement() throws RemoteException {
        String temperature = "27";
        return temperature;
    }
}
