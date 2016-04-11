package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;

/**
 * Created by Mireia on 10/04/2016.
 */
public class PerfilClient extends Activity {
    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_client_perfil.xml
        setContentView(R.layout.activity_client_perfil);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        client = (Client) i.getSerializableExtra("client");
        //Toast.makeText(PerfilClient.this, client.getName(), Toast.LENGTH_SHORT).show();

        this.initializeUserData();

    }

    public void initializeUserData() {

        String name = client.getName() + " " + client.getSurname();
        TextView txtname = (TextView) findViewById(R.id.name);
        txtname.setText(name);

        String mail = client.getMail();
        TextView txtmail = (TextView) findViewById(R.id.mail);
        txtmail.setText(mail);


    }
}
