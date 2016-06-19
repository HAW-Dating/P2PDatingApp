package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import net.xenqtt.client.AsyncClientListener;
import net.xenqtt.client.AsyncMqttClient;
import net.xenqtt.client.MqttClient;
import net.xenqtt.client.PublishMessage;
import net.xenqtt.client.Subscription;
import net.xenqtt.message.ConnectReturnCode;
import net.xenqtt.message.QoS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import de.haw_landshut.haw_dating.p2pdatingapp.chat.ChatBubble;
import de.haw_landshut.haw_dating.p2pdatingapp.chat.ChatBubbleAdapter;
import de.haw_landshut.haw_dating.p2pdatingapp.data.ChatMessage;

public class ChatActivity extends AppCompatActivity implements ChatNameDialog.Communicator {
    private AsyncMqttClient mqttClient;
    private ListView chatListView;
    private String clientId;
    private static Context context;

    private ArrayList<ChatBubble> chatBubbleList = new ArrayList<>();
    private ChatBubbleAdapter chatBubbleAdapter;
    private String roomId;
    private String userName;
    private final String userUUID = UUID.randomUUID().toString();

    public static final String PREF_NAME = "chatActivityPrefs";

    public static Context getContext() {
        return context;
    }

    private Handler handler = new Handler();


    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final CountDownLatch connectLatch = new CountDownLatch(1);
        final AtomicReference<ConnectReturnCode> connectReturnCode = new AtomicReference<>();

        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        this.userName = settings.getString("userName", "anonymous");

        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        context = getApplicationContext();

        Bundle bundle = intent.getBundleExtra(FindYourLoveActivity.CHAT_MESSAGE);

        ArrayList<String> data = bundle.getStringArrayList("data");

        String ip = data.get(0);
        roomId = data.get(1);

        chatListView = (ListView) findViewById(R.id.listView);
        chatBubbleAdapter = new ChatBubbleAdapter(chatBubbleList);
        chatListView.setAdapter(chatBubbleAdapter);

        AsyncClientListener listener = new AsyncClientListener() {
            @Override
            public void connected(MqttClient client, ConnectReturnCode returnCode) {
                connectReturnCode.set(returnCode);
                connectLatch.countDown();
            }

            @Override
            public void publishReceived(MqttClient client, PublishMessage mqttMessage) {
                String payload = mqttMessage.getPayloadString();
                ChatMessage chatMessage = ChatMessage.chatMessageFromJson(payload);
                Log.d("Message recieved:", payload);
                if (!userUUID.equals(chatMessage.getSenderUUID())) {
                    addToListView(chatMessage, 0);
                }
                mqttMessage.ack();
            }

            @Override
            public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
                if (cause != null) {
                    System.err.println("Disconnected from the broker due to an exception." + cause);
                }
                addToListView(new ChatMessage("lost connection from broker", "", ""), 2);
                if (reconnecting) {
                    addToListView(new ChatMessage("the broker will try to reconnect...(not - bugged!)", "", ""), 2);
                }
            }

            @Override
            public void published(MqttClient client, PublishMessage message) {
            }

            @Override
            public void subscribed(MqttClient client, Subscription[] requestedSubscriptions, Subscription[] grantedSubscriptions, boolean requestsGranted) {
                if (!requestsGranted) {
                    System.err.println("Unable to subscribe to the following subscriptions: " + Arrays.toString(requestedSubscriptions));
                }
            }

            @Override
            public void unsubscribed(MqttClient client, String[] topics) {
            }
        };

        clientId = String.valueOf(System.currentTimeMillis());

        mqttClient = new AsyncMqttClient(ip, listener, 5);

        try {
            // Connect to the broker with a specific client ID
            mqttClient.connect(clientId, true);

            connectLatch.await();
            ConnectReturnCode returnCode = connectReturnCode.get();
            if (returnCode == null || returnCode != ConnectReturnCode.ACCEPTED) {
                System.out.println("Unable to connect to the MQTT broker. Reason: " + returnCode);
                addToListView(new ChatMessage("unable to connect to broker. please restart activity", "", ""), 2);
            } else {
                addToListView(new ChatMessage("Connected to " + roomId, "", ""), 2);
                List<Subscription> subscriptions = new ArrayList<>();
                subscriptions.add(new Subscription(roomId, QoS.AT_MOST_ONCE));
                mqttClient.subscribe(subscriptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // type 0 = rcv, 1 = snd, 2 = info
    public void addToListView(final ChatMessage message, final int type) {
        runOnUiThread(new Runnable() {
            public void run() {
                chatBubbleList.add(new ChatBubble(message.getSender(), message.getText(), type));
                chatBubbleAdapter.notifyDataSetChanged();
                chatListView.setSelection(chatBubbleAdapter.getCount() - 1);
            }
        });
    }

    public void sendMessage(View view) {
        EditText ETmessage = (EditText) findViewById(R.id.ETmessage);
        String messageString;
        if (!(messageString = ETmessage.getText().toString().trim()).equals("")) {
            ChatMessage message = new ChatMessage(messageString, userName, userUUID);

            addToListView(message, 1);

            mqttClient.publish(new PublishMessage(roomId, QoS.AT_MOST_ONCE, message.toJson()));
            Log.d("Message sent:", message.toJson());
            // reset textfield
            ETmessage.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
            FragmentManager manager = getFragmentManager();
            ChatNameDialog chatNameDialog = new ChatNameDialog();
            chatNameDialog.show(manager, "ChatNameDialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mqttClient.disconnect();
    }


    @Override
    public void onDialogMessage(String message) {

        this.userName = message;
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userName", this.userName);

        // Commit the edits!
        editor.commit();

    }


}
