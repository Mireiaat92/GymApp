package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 08/04/2016.
 */
public class PerfilClientFromEntrenador extends AppCompatActivity {
    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_perfil_client_from_entrenador.xml
        setContentView(R.layout.activity_perfil_client_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        selectedClient = (Client) i.getSerializableExtra("selectedClient");
        myEntrenador = (Entrenador) i.getSerializableExtra("myEntrenador");

        this.initializeClientData();
        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_perfil_client_from_entrenador);
        toolbar.setTitle(selectedClient.getName() + " " + selectedClient.getSurname());

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

    public void initializeClientData() {

        TextView txtname = (TextView) findViewById(R.id.name);
        txtname.setText(selectedClient.getName() + " " + selectedClient.getSurname());

        TextView txtmail = (TextView) findViewById(R.id.mail);
        txtmail.setText(selectedClient.getMail());

        String weightValue = null;
        TextView weight = (TextView)findViewById(R.id.weight);
        if (selectedClient.getWeight().toString() == null || selectedClient.getWeight().toString() == "0.0"){
            weightValue = getResources().getString(R.string.notAssigned);
        }
        else{
            weightValue = selectedClient.getWeight().toString() + " kg";
        }
        weight.setText(weightValue);

        String heightValue = null;
        TextView height = (TextView)findViewById(R.id.height);
        if (selectedClient.getHeight().toString() == null || selectedClient.getHeight().toString() == "0.0"){
            heightValue = getResources().getString(R.string.notAssigned);
        }
        else{
            heightValue = selectedClient.getHeight().toString() + " cm";
        }
        height.setText(heightValue);

        TextView objectiu = (TextView)findViewById(R.id.objectiu);
        objectiu.setText(selectedClient.getObjectiu());


    }

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientViewFromEntrenador.class);
        i.putExtra("selectedClient", selectedClient);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
