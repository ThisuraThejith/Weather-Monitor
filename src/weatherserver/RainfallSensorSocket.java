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
public class RainfallSensorSocket extends UnicastRemoteObject implements WeatherRMI {

    public RainfallSensorSocket() throws RemoteException {
        super();
    }

    @Override
    public String getMeasurement() throws RemoteException {
        String rainfall = "5000";
        return rainfall;
    }

}
