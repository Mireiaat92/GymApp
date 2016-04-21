package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    Button tascaCompletada;
    TextView deixarComentari;
    Button textboto;

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

        String descripcio = selectedTasca.getDescripcio();
        TextView txtdescripcio = (TextView) findViewById(R.id.descripcio);
        txtdescripcio.setText(descripcio);

        Boolean status = selectedTasca.getCompletada();
        textboto = (Button) findViewById(R.id.tascaCompletada);
        if (status == true){
            textboto.setText("TASCA COMPLETA");
            textboto.setBackgroundColor(000000);

        }
        else if (status == false){
            textboto.setText("MARCAR COM A COMPLETA");
        }

        //BOTÓ TASCA COMPLETADA
        tascaCompletada = (Button) findViewById(R.id.tascaCompletada);


        tascaCompletada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTasca.getCompletada() == true){
                    Toast.makeText(TaskViewFromClient.this, "La tasca ja està completa", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject >("TASQUES");
                        ParseObject objFromServer = query.get(selectedTasca.getObjectId());
                        objFromServer.put("Completada", true);

                        objFromServer.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    textboto.setText("TASCA COMPLETA");
                    textboto.setBackgroundColor(000000);
                }

            }
        });

        deixarComentari = (TextView) findViewById(R.id.deixarComentari);

        deixarComentari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskViewFromClient.this, "DEIXAR COMENTARI", Toast.LENGTH_SHORT).show();
            }
        });

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
