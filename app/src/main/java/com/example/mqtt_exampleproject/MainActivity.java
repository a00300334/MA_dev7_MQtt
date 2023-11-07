package com.example.mqtt_exampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private MqttAsyncClient  mqttAsyncClient;
    private String broker = "tcp://192.168.0.101:1883"; // laptop
    // Desktop private String broker = "tcp://192.168.23.123:1883";

    private static final String TOPIC = "inclass/example";

    private TextView textView;
    private EditText editText;
    private String clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientId = MqttAsyncClient.generateClientId();
        context = this;
        
        //TODO create views
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mqttAsyncClient = new MqttAsyncClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            mqttAsyncClient.connect(options, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT","We are connected!" );
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("MQTT","We are not connected :(" );
                }
            });

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}