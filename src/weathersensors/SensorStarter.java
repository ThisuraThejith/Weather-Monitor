/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weathersensors;

/**
 *
 * @author THISURA THEJITH
 */

//Starting the sensors
public class SensorStarter {
    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 6066;
        
        Thread tempcolombo = new TemperatureSensor(serverName, port,"colombo");
        tempcolombo.start();
        Thread tempgalle = new TemperatureSensor(serverName, port,"galle");
        tempgalle.start();
        
        Thread raincolombo = new RainfallSensor(serverName, port,"colombo");
        raincolombo.start();
        Thread raingalle = new RainfallSensor(serverName, port,"galle");
        raingalle.start();
        
        Thread humidcolombo = new HumiditySensor(serverName, port,"colombo");
        humidcolombo.start();
        Thread humidgalle = new HumiditySensor(serverName, port,"galle");
        humidgalle.start();
        
        Thread aircolombo = new AirpressureSensor(serverName, port,"colombo");
        aircolombo.start();
        Thread airgalle = new AirpressureSensor(serverName, port,"galle");
        airgalle.start();
    }
}
