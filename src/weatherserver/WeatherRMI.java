/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author THISURA THEJITH
 */
public interface WeatherRMI extends Remote{
        public String getTemp(String location, String clientLocation) throws RemoteException;
        public String getRainfall(String location, String clientLocation) throws RemoteException;
        public String getAirpressure(String location, String clientLocation) throws RemoteException;
        public String getHumidity(String location, String clientLocation) throws RemoteException;
}
