
import iot.DateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.eclipse.paho.client.mqttv3.MqttException;
import udanalyser.MainDataAnalyser;
import udmanager.SpeedManager;
import udmanager.SteeringManager;
import udMqtt.UDmqtt;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author darwin
 */
public class UDDrive{
     
    
     public static void main(String[] args) throws InterruptedException{

        /**
         * data manipulator variables
         */
        int factor = 100;

        MainDataAnalyser analyse = new MainDataAnalyser(":", "=", 0, 1, 2, 3, true, 4);
        UDmqtt mqtt = new UDmqtt();
        
        mqtt.connect();
        /**
         * This BlockingQueue will be the storage point for all the data coming
         * from the server and to be taken by the appropriate Threads.
         */
        BlockingQueue speedQueue = new ArrayBlockingQueue(1);
        BlockingQueue steeringQueue = new ArrayBlockingQueue(1);
      
        DateTime time = new DateTime();

     
        SteeringManager steerManager = new SteeringManager(steeringQueue);
        SpeedManager speedManager = new SpeedManager(speedQueue);

        Thread l = new Thread(steerManager);
        Thread b = new Thread(speedManager);
        
        //Thread Started
        l.start();
        b.start();
        
        while(mqtt.isConnected()){
            analyse.setData(mqtt.getMsg());
            if(analyse.getCommand().equals("MC")){
                int steering = analyse.getSteeringInt() * factor;
                int speed = analyse.getSpeedInt();
                String ID = analyse.getID();
                String speedData = ID + ":" + speed;
                String steeringData = ID + ":" + steering;
               
                Thread.sleep(150);
                boolean isSpeedAccepted = speedQueue.offer(speedData);
                if(!isSpeedAccepted){
                    speedQueue.clear();
                    System.out.println("Clearing Queue Speed at " + time.getDateFormated());
                }
                
                boolean isSteeringAccepted = steeringQueue.offer(steeringData);
                if(!isSteeringAccepted){
                    steeringQueue.clear();
                    System.out.println("Clearing Queue Steering at " + time.getDateFormated());
                }
            }
        }
            System.err.println("Connection Dropped");
        }
       

    
}

