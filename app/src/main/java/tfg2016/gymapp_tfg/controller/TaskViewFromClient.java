package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class TaskViewFromClient extends Activity {

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

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
        setContentView(R.layout.activity_task_from_client);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        selectedTasca = (Tasca) i.getSerializableExtra("selectedTasca");
        //Client myClient = (Client) i.getSerializableExtra("selectedClient");

        this.initializeTascaData();

    }

    public void initializeTascaData() {

        String titol = selectedTasca.getTitol();
        TextView txttitiol = (TextView) findViewById(R.id.titol);
        txttitiol.setText(titol);

        String descripcio = selectedTasca.getTitol();
        TextView txtdescripcio = (TextView) findViewById(R.id.descripcio);
        txtdescripcio.setText(descripcio);

        String dueDate = String.valueOf(selectedTasca.getDueDate());
        TextView txtdueDate = (TextView) findViewById(R.id.duedate);
        txtdueDate.setText(dueDate);
    }
}
