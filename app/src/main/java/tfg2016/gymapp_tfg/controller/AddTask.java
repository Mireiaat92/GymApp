package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.Date;
import java.util.HashMap;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class AddTask extends Activity {

    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        selectedClient = (Client) intent.getSerializableExtra("selectedClient");

        this.initializeButtons();
    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons(){
        Button addTask = (Button) findViewById(R.id.AddNewTask);
        addTask.setOnClickListener(clickAddTask);

    }

    public Button.OnClickListener clickAddTask = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                if (AddTask.this.getTitol().equalsIgnoreCase("") || AddTask.this.getDescripcio().equalsIgnoreCase("")) {
                    Toast.makeText(AddTask.this, "Els camps titol i descripcio son obligatoris", Toast.LENGTH_SHORT).show();
                } else {

                        addTask(AddTask.this.getTitol(), AddTask.this.getDescripcio(), AddTask.this.getDueDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };

    /**
     * Method addTask
     * @param titol, descripcio, dueDate
     * @return
     * @throws ParseException
     */
    public void addTask(String titol, String descripcio, Date dueDate) throws ParseException {
        HashMap<String, Object> paramsCheckMail = new HashMap<String, Object>();
        paramsCheckMail.put("idClient", selectedClient.getObjectId());
        paramsCheckMail.put("titol", titol);
        paramsCheckMail.put("descripcio", descripcio);
        paramsCheckMail.put("dueDate", dueDate);


            HashMap<String, Object> paramsAddTasca = new HashMap<String, Object>();
            paramsAddTasca.put("titol", titol);
            paramsAddTasca.put("descripcio", descripcio);
            paramsAddTasca.put("duedate", dueDate);
            paramsAddTasca.put("idlient", selectedClient.getObjectId());
            ParseCloud.callFunction("addTask", paramsAddTasca);

            Toast.makeText(AddTask.this, "Tasca afegida correctament", Toast.LENGTH_SHORT).show();

            Intent clientActivityFromEntrenador = new Intent(AddTask.this, ClientActivityFromEntrenador.class);
            clientActivityFromEntrenador.putExtra("selectedClient", selectedClient);
            startActivity(clientActivityFromEntrenador);

    }



    /**
     * Method getTitol
     * @return titolText
     */
    public String getTitol() {
        EditText titol = (EditText) findViewById(R.id.titol_task);
        String titolText = titol.getText().toString();
        return titolText;
    }

    /**
     * Method getDescripcio
     * @return descripcioText
     */
    public String getDescripcio() {
        EditText descripcio = (EditText) findViewById(R.id.descripcio_task);
        String descripcioText = descripcio.getText().toString();
        return descripcioText;
    }

    /**
     * Method getDueDate
     * @return dueDateText
     */
    public Date getDueDate() {
        EditText dueDate = (EditText) findViewById(R.id.duedate_task);
        Date dueDateText = (Date) dueDate.getText();
        return dueDateText;
    }
}
