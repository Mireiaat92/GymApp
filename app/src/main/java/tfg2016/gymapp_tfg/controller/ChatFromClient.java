package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

public class ChatFromClient extends Activity {
    public static final String USER_NAME_KEY = "userName";

    private static final String TAG = ChatFromClient.class.getName();

    // TODO: fix the layout to be able to put 100 here
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 15;

    private static String idClient;
    private static String idEntrenador;
    private static String username;

    private EditText txtMessage;
    private Button btnSend;
    private ListView chatListView;
    private ArrayAdapter<String> adapter;

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    private BroadcastReceiver pushReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Just received a push. Let's update the messages");
            receiveMessage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupUI();

        //PushService.subscribe(this, "Prueba", Chat.class);
        //PushService.setDefaultPushCallback(this, Chat.class);

        receiveMessage();
        registerReceiver(pushReceiver, new IntentFilter("MyAction"));

        Intent intent = getIntent();

        myEntrenador = (Entrenador) intent.getSerializableExtra("myEntrenador");
        myClient = (Client) intent.getSerializableExtra("myClient");
        username = myClient.getName();

        idEntrenador = myEntrenador.getObjectId();
        idClient = myClient.getObjectId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (pushReceiver != null) {
            unregisterReceiver(pushReceiver);
        }
    }

    public void setupUI() {
        txtMessage = (EditText) findViewById(R.id.etMensaje);
        btnSend = (Button) findViewById(R.id.btnSend);
        chatListView = (ListView) findViewById(R.id.chatList);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        chatListView.setAdapter(adapter);
        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = txtMessage.getText().toString();
                ParseObject message = new ParseObject("Messages");
                message.put("ID_client", idClient);
                message.put("ID_entrenador", idEntrenador);
                message.put(USER_NAME_KEY, username);
                message.put("message", data);
                message.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        receiveMessage();
                    }
                });
                createPushNotifications(data);
                txtMessage.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parse_chat, menu);
        return true;
    }

    public void createPushNotifications(String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("alert", message);
            object.put("title", "Chat");
            object.put("action", "MyAction");

            ParseQuery query = ParseInstallation.getQuery();
            query.whereNotEqualTo("ID_client", idClient);

            ParsePush pushNotification = new ParsePush();
            pushNotification.setQuery(query);
            pushNotification.setData(object);
            pushNotification.sendInBackground();
        } catch (JSONException e) {
            Log.e(TAG, "Could not parse the push notification", e);
        }
    }

    private void receiveMessage() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("ID_client", idClient);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messages, ParseException e) {
                if (e == null) {
                    adapter.clear();

                    StringBuilder builder = new StringBuilder();
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        builder.append(messages.get(i).getString(USER_NAME_KEY)
                                + ": " + messages.get(i).getString("message") + "\n");
                    }
                    addItemstoListView(builder.toString());
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addItemstoListView(String message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        chatListView.invalidate();
    }
}