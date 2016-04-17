package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class TaskViewFromClient extends AppCompatActivity {

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_perfil_client_from_entrenador.xml
        setContentView(R.layout.activity_task_from_client);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        selectedTasca = (Tasca) i.getSerializableExtra("selectedTasca");
        myClient = (Client) i.getSerializableExtra("myClient");


        this.initializeTascaData();
        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_task_from_client);
        toolbar.setTitle(selectedTasca.getTitol());

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_toolbar_arrow);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doBack();
                    }
                }

        );
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

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientDashboard.class);
        i.putExtra("myClient", myClient);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
