package tfg2016.gymapp_tfg.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;


/**
 * Created by Mireia on 01/04/2016.
 */
public class ClientActivityFromEntrenador extends Activity {

    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    // Declare Variables
    TextView txtname;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_client_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedClient = (Client) i.getSerializableExtra("selectedClient");

        Toast.makeText(ClientActivityFromEntrenador.this, selectedClient.getMail(), Toast.LENGTH_SHORT).show();


        this.initializeButtons();
    }

    /**
     * Inicializació dels botons de l'activitat ClientActivityFromEntrenador. Perfil
     */
    private void initializeButtons() {
        Button addClient = (Button) findViewById(R.id.btnPerfilClient);

        // Listening to login link
        addClient.setOnClickListener(clickPerfilClient);
    }

    /**
     * Al clickar el botó addClient ens portarà a l'activitat de addClient.
     */
    public Button.OnClickListener clickPerfilClient = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), ActivityPerfilClientFromEntrenador.class);
            i.putExtra("selectedClient", selectedClient);
            startActivity(i);
        }
    };
}