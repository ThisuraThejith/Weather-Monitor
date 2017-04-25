/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherclient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author THISURA THEJITH
 */
public interface StationRMI extends Remote{
     public void alert(String message) throws RemoteException;
}
