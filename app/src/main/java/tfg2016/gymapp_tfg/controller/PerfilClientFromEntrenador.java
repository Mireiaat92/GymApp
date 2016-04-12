package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;

/**
 * Created by Mireia on 08/04/2016.
 */
public class PerfilClientFromEntrenador extends Activity {
    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_perfil_client_from_entrenador.xml
        setContentView(R.layout.activity_perfil_client_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        selectedClient = (Client) i.getSerializableExtra("selectedClient");

        this.initializeUserData();

    }

    public void initializeUserData() {

        String name = selectedClient.getName() + " " + selectedClient.getSurname();
        TextView txtname = (TextView) findViewById(R.id.name);
        txtname.setText(name);

        String mail = selectedClient.getMail();
        TextView txtmail = (TextView) findViewById(R.id.mail);
        txtmail.setText(mail);


    }
}
