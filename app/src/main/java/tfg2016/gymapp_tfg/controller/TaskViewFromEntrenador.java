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
public class TaskViewFromEntrenador extends Activity {
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
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_task_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedTasca = (Tasca) i.getSerializableExtra("selectedTasca");
        selectedClient = (Client) i.getSerializableExtra("selectedClient");

        this.initializeTascaData();

    }

    public void initializeTascaData() {

        String titol = selectedTasca.getTitol();
        TextView txttitol = (TextView) findViewById(R.id.titol);
        txttitol.setText(titol);

        String descripcio = selectedTasca.getDescripcio();
        TextView txtdescripcio = (TextView) findViewById(R.id.descripcio);
        txtdescripcio.setText(descripcio);

        String duedate = String.valueOf(selectedTasca.getDueDate());
        TextView txtduedate = (TextView) findViewById(R.id.duedate);
        txtduedate.setText(duedate);


    }


}
