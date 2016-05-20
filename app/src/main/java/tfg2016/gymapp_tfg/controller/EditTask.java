package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/05/2016.
 */
public class EditTask extends AppCompatActivity {

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

    private String taskId;

    private SimpleDateFormat dateFormatter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedTasca = (Tasca) i.getSerializableExtra("selectedTasca");
        selectedClient = (Client) i.getSerializableExtra("selectedClient");
        myEntrenador = (Entrenador) i.getSerializableExtra("myEntrenador");
        taskId = (String) i.getSerializableExtra("taskId");

        this.initializeButtons();
        this.initializeTaskData();
        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_task);
        toolbar.setTitle(R.string.editTask);

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

    public void initializeButtons() {
        Button sendDeleteThisTask = (Button) findViewById(R.id.sendDeleteThisTask);
        sendDeleteThisTask.setOnClickListener(clickSendDeleteThisTask);
    }

    public void initializeTaskData() {
        EditText titol = (EditText)findViewById(R.id.EditTextTaskTitol);
        titol.setText(selectedTasca.getTitol());

        EditText descripcio = (EditText)findViewById(R.id.EditTextTaskDescription);
        descripcio.setText(selectedTasca.getDescripcio());

        EditText dueDate = (EditText)findViewById(R.id.EditTextTaskDueDate);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dueDate.setText(dateFormatter.format(selectedTasca.getDueDate()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.saveEditTask) {
            Tasca nuevaTasca = CargarTasca();

            if (nuevaTasca.getTitol().equalsIgnoreCase("") || nuevaTasca.getDescripcio().equalsIgnoreCase("")){
                Toast.makeText(this, ("titol i edscripcio obligatoris"), Toast.LENGTH_SHORT).show();
            }
            else {
                Intent i = new Intent(this, TaskViewFromEntrenador.class);
                i.putExtra("selectedTasca", selectedTasca);
                i.putExtra("selectedClient", selectedClient);
                i.putExtra("myEntrenador", myEntrenador);
                try {
                    EditarSitioBDD(nuevaTasca);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method CargarTasca. Recuperamos la información de los datos de la tarea seleccionada
     * @return Tasca
     */
    public Tasca CargarTasca(){
        EditText TextTitol =(EditText)findViewById(R.id.EditTextTaskTitol);
        String titol = TextTitol.getText().toString();

        EditText TextDescripcio =(EditText)findViewById(R.id.EditTextTaskDescription);
        String descripcio = TextDescripcio.getText().toString();

        EditText TextDueDate =(EditText)findViewById(R.id.EditTextTaskDueDate);
        String dueDateString = TextDueDate.getText().toString();
        Date dueDate = ConvertStringToDate(dueDateString);

        boolean completada = selectedTasca.getCompletada();

        String comentari = selectedTasca.getComentari();
        String idClient = selectedTasca.getIdClient();
        String objectId = selectedTasca.getObjectId();

        return new Tasca(idClient, titol, descripcio, dueDate, completada, comentari, objectId);
    }

    /**
     * Method EditarTascaBDD
     * @param nuevaTasca
     * @return success
     * @throws ParseException
     */
    public boolean EditarSitioBDD(Tasca nuevaTasca) throws ParseException{
        nuevaTasca.getObjectId();
        boolean success = false;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("idClient", selectedClient.getObjectId());
        params.put("titol", nuevaTasca.getTitol());
        params.put("descripcio", nuevaTasca.getDescripcio());
        params.put("dueDate", nuevaTasca.getDueDate());
        params.put("completada", nuevaTasca.getCompletada());
        params.put("comentari", nuevaTasca.getComentari());
        params.put("objectId", nuevaTasca.getObjectId());

        ParseCloud.callFunction("editTaskData", params);

        return success;
    }

    /**
     * ConvertStringToDate
     * @param fecha
     * @return data
     */
    public Date ConvertStringToDate(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formatter.parse(fecha);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Button.OnClickListener clickSendDeleteThisTask = new Button.OnClickListener() {

        public void onClick(View v) {

            //ELIMINAR UNA TASCA
            String alertString = getResources().getString(R.string.deleteTask); //missatge de alerta

            HashMap<String, Object> paramsQuery = new HashMap<String, Object>();
            paramsQuery.put("objectId", selectedTasca.getObjectId()); //ell cap a mi

            try {
                ParseCloud.callFunction("deleteTask", paramsQuery);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), ClientViewFromEntrenador.class);
            i.putExtra("selectedClient", selectedClient);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);

        }
    };

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), TaskViewFromEntrenador.class);
        i.putExtra("selectedTasca", selectedTasca);
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
