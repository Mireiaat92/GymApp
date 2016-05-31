package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class TaskViewFromEntrenador extends AppCompatActivity {

    String selectedTascaId;

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
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_task_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedTascaId = (String) i.getSerializableExtra("selectedTascaId");
        selectedClient = (Client) i.getSerializableExtra("selectedClient");
        myEntrenador = (Entrenador) i.getSerializableExtra("myEntrenador");

        this.initializeTascaData();
        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_task_from_entrenador);
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
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("objectid", selectedTascaId);

            List<ParseObject> taskresponse = null;
            try {
                taskresponse = ParseCloud.callFunction("checkTascaData", params);

                ParseObject userParse = taskresponse.iterator().next();
                setSelectedTasca(new Tasca(userParse.getString("idClient"), userParse.getString("Titol"), userParse.getString("Descripcio"), userParse.getDate("Due_Date"), userParse.getBoolean("Completada"), userParse.getString("Comentari"), userParse.getObjectId()));

            } catch (ParseException e) {
                e.printStackTrace();
            }


        String descripcio = selectedTasca.getDescripcio();
        TextView txtdescripcio = (TextView) findViewById(R.id.descripcio);
        txtdescripcio.setText(descripcio);


        Boolean completada = selectedTasca.getCompletada();
        Date dueDate = selectedTasca.getDueDate();
        String status =  null;
            if (completada == true){
            status="Completed";
        }
        else if (completada == false){
            status = "Not done";
        }
        else if (completada == null){
                //al fer addTask ha de posar completada en False
                //si esta en false pero la dueDate és del futur o 7 dies del pasat es posarà en pending
            status="Pending";
        }
        TextView txtStatus = (TextView) findViewById(R.id.status);
        txtStatus.setText(status);

        String comentarii = selectedTasca.getComentari();
        TextView txtcomentari = (TextView) findViewById(R.id.comentari);
        txtcomentari.setText(comentarii);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_view_from_entrenador, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.editTask){
            if(selectedTasca.getCompletada() == true){
                Toast.makeText(TaskViewFromEntrenador.this, "No pots editar una tasca completada", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent i = new Intent(this, EditTask.class);
                i.putExtra("selectedTasca", selectedTasca);
                i.putExtra("selectedClient", selectedClient);
                i.putExtra("myEntrenador", myEntrenador);
                startActivity(i);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientViewFromEntrenador.class);
        i.putExtra("selectedClient", selectedClient);
        i.putExtra("myEntrenador", myEntrenador);
        i.putExtra("taskId", selectedTasca.getObjectId());
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
