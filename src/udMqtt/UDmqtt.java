/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udMqtt;



import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 *
 * @author darwin
 */
public class UDmqtt implements MqttCallback { 

    MqttAsyncClient udClient;
    String msg = "ID=0:MC:0:0";
    
    public UDmqtt() {
    }
    
    public String getMsg(){
        return msg;
    }

    public boolean isConnected(){
        return udClient.isConnected();
    }
    
    public void reconnect() throws MqttException{
        udClient.reconnect();
    }
    
    public void connect(){
    
        /**********************************************************************
         * Subscribe Mqtt required variables.
         */
        String topic = "/control/speedSteering/value";
        int qos = 2;
        String broker = "tcp://192.168.1.103:1883";
        String clientId = "Test1";
        MemoryPersistence persistance = new MemoryPersistence();
        /**********************************************************************/
        
        /**********************************************************************
        * Creating mqtt subscribe
        */
         try 
         {
             udClient = new MqttAsyncClient(broker, clientId, persistance);
             MqttConnectOptions connOpts = new MqttConnectOptions();
             connOpts.setCleanSession(true);
             udClient.setCallback(this);
             System.out.println("Connecting to broker: " + broker);
             udClient.connect(connOpts);
             if(!udClient.isConnected()){
                 System.out.println("Connected");
                 Thread.sleep(1000);
                 udClient.subscribe(topic, qos);
                 System.out.println("Subsribed");
             }
             
             
         } catch (MqttException | InterruptedException e) {
             if(e instanceof MqttException){
                 System.out.println("reason" + ((MqttException)e).getReasonCode());
                 
             }
             System.out.println("msg " + e.getMessage());
             System.out.println("loc "+ e.getLocalizedMessage());
             System.out.println("cause "+ e.getCause());
             System.out.println("excep "+ e);
         }
}

    @Override
    public void connectionLost(Throwable cause) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String messageRecieved = new String(message.getPayload());
        msg = messageRecieved;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
