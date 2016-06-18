package tfg2016.gymapp_tfg.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    EditText comentari;

    String resultText;

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
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("objectid", selectedTasca.getObjectId());

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

        String duedate = convertStringToDate(selectedTasca.getDueDate());
        TextView txtduedate = (TextView) findViewById(R.id.duedate);
        txtduedate.setText(duedate);

        String comentarii = selectedTasca.getComentari();
        TextView txtcomentari = (TextView) findViewById(R.id.comentari);
        txtcomentari.setText(comentarii);

        Boolean status = selectedTasca.getCompletada();
        textboto = (Button) findViewById(R.id.tascaCompletada);
        if (status == true){
            textboto.setText(getResources().getString(R.string.taskCompleted));
            textboto.setBackgroundColor(000000);

        }
        else if (status == false){
            textboto.setText(getResources().getString(R.string.markCompleted));
        }

        //BOTÓ TASCA COMPLETADA
        tascaCompletada = (Button) findViewById(R.id.tascaCompletada);


        tascaCompletada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTasca.getCompletada() == true){
                    Toast.makeText(TaskViewFromClient.this, getResources().getString(R.string.alreadyCompleted), Toast.LENGTH_SHORT).show();
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

                    textboto.setText(getResources().getString(R.string.taskCompleted));
                    textboto.setBackgroundColor(000000);
                }

            }
        });

        if(selectedTasca.getComentari()!= null){
            TextView comment = (TextView)findViewById(R.id.deixarComentari);
            comment.setText(getResources().getString(R.string.editComment));
        }

        deixarComentari = (TextView) findViewById(R.id.deixarComentari);

        deixarComentari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(TaskViewFromClient.this);
                View promptView = layoutInflater.inflate(R.layout.dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskViewFromClient.this);
                alertDialogBuilder.setView(promptView);

                comentari = (EditText) promptView.findViewById(R.id.comentariText);
                comentari.setText(selectedTasca.getComentari());

                // setup a dialog window
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resultText= String.valueOf((comentari.getText()));
                                try {
                                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject >("TASQUES");
                                    ParseObject objFromServer = query.get(selectedTasca.getObjectId());
                                    objFromServer.put("Comentari", resultText);
                                    objFromServer.save();
                                    initializeTascaData();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }

        });

    }

    public static String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd MMM yyyy");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }


    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientDashboard.class);
        i.putExtra("myClient", myClient);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
