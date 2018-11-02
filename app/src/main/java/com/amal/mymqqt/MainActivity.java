package com.amal.mymqqt;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {

    String topic        = "hello/username/amal";
    String content      = "Message from MqttPublishSample";
    int qos             = 2;
    String broker       = "tcp://test.mosquitto.org:1883";
    String clientId     = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();
    TextView testview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        testview = (TextView) findViewById(R.id.textView);

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            sampleClient.setCallback(new exampleCallBack());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
          //  sampleClient.publish(topic, message);
            sampleClient.subscribe(topic, qos);
          //  System.out.println("Message published");
          //  sampleClient.disconnect();
          //  System.out.println("Disconnected");
          //  System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class exampleCallBack implements MqttCallback
    {
        public void connectionLost(Throwable cause)
        {
           // Log.d(getClass().getCanonicalName(), "MQTT Server connection lost" + e.getReasonCode());
        }
        public void messageArrived(String topic, MqttMessage message)
        {
            testview.setText("Message arrived:" + topic + ":" + message.toString());
            Log.d(getClass().getCanonicalName(), "Message arrived:" + topic + ":" + message.toString());
        }
        public void deliveryComplete(IMqttDeliveryToken token)
        {
            Log.d(getClass().getCanonicalName(), "Delivery complete");
        }
    }
}
