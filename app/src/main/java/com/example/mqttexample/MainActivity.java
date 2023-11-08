package com.example.mqttexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private MqttAsyncClient mqttAsyncClient;
    //private String broker = "tcp://192.168.23.123:1883"; // desktop
   private String broker = "tcp://192.168.0.101:1883"; // laptop

    private String clientId;
    private static final String TOPIC = "inclass/example";
    private TextView textView;
    private EditText editText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientId = MqttClient.generateClientId();
        context = this;

        //TODO create views
        textView = (TextView) findViewById(R.id.message_tv);
        editText = (EditText) findViewById(R.id.message_et);

    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            mqttAsyncClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mqttAsyncClient = new MqttAsyncClient(broker,clientId,new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            mqttAsyncClient.connect(options, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("MQTT","Connected to MQTT");
                    try {
                        mqttAsyncClient.subscribe(TOPIC, 0);
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("MQTT","NOT Connected to MQTT");
                }

                private MqttCallback mqttCallback = new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        if(topic.equals(TOPIC)){
                            String strMessage = new String(message.getPayload(), StandardCharsets.UTF_8);

                            runOnUiThread(()-> textView.setText(strMessage));
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                };

            });



        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(View view){
        String message = editText.getText().toString();
        if(message.length() > 0) {
            // convert String to the MqttMessage
            MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));

            //send the mqttMessage to a topic
            try {
                mqttAsyncClient.publish(TOPIC, mqttMessage);
                Log.d("MQTT","Message sent.. published");

                textView.setText(message);
                editText.setText("");

            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

