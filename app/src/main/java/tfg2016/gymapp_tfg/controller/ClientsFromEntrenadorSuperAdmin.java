package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 30/05/2016.
 */
public class ClientsFromEntrenadorSuperAdmin extends AppCompatActivity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private Entrenador selectedEntrenador;
    public Entrenador getSelectedEntrenador() {
        return selectedEntrenador;
    }
    public void setSelectedEntrenador(Entrenador selectedEntrenador) {
        this.selectedEntrenador = selectedEntrenador;
    }


    private static Intent intent;

    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_entrenador_dashboard.xml
        setContentView(R.layout.activity_clients_from_entrenador);

        intent = this.getIntent();
        setSelectedEntrenador((Entrenador) intent.getSerializableExtra("selectedEntrenador")); //serialització de l'objecte

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        this.initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_clients_from_entrenador);
        toolbar.setTitle(selectedEntrenador.getName() + " " + selectedEntrenador.getSurname());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clients_from_entrenador_superadmin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.deleteEntrenador) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set title
           //String alert_title = getResources().getString("Confirmación");
            String alert_title = ("Confirmación");
            String alert_description = ("¿Estas seguro que deseas eliminar este entrenador?");
            alertDialogBuilder.setTitle(alert_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(alert_description)
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        // Lo que sucede si se pulsa yes
                        public void onClick(DialogInterface dialog,int id) {

                            try {
                                Client myUserClient = null;
                                do {
                                    myUserClient = ClientsFromEntrenadorSuperAdmin.this.searchEntrenador(selectedEntrenador.getObjectId());
                                    if (myUserClient != null) {
                                        HashMap<String, Object> params = new HashMap<String, Object>();
                                        params.put("entrenadorId", selectedEntrenador.getObjectId());
                                        ParseCloud.callFunction("deleteEntrenadorDependencies", params);
                                    }
                                }while(myUserClient != null);

                                HashMap<String, Object> params = new HashMap<String, Object>();
                                params.put("entrenadorId", selectedEntrenador.getObjectId());
                                ParseCloud.callFunction("deleteEntrenador", params);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent(getApplicationContext(), SuperAdminDashboard.class);
                            startActivity(i);
                            finish();
                        }


                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // Si se pulsa no no hace nada
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }


    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ClientsFromEntrenadorSuperAdmin.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Accedint a la llista de clients");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "CLIENTS" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "CLIENTS");

            query.whereEqualTo("ID_Entrenador", selectedEntrenador.getObjectId());
            query.orderByDescending("_created_at");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(ClientsFromEntrenadorSuperAdmin.this,
                    R.layout.item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject clients : ob) {
                adapter.add((String) clients.get("Nom") + " " + clients.get("Cognom"));
            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
        }
    }


    public Client searchEntrenador(String idEntrenador) throws java.text.ParseException {
        Client myUserClient = null;

        HashMap<String, Object> paramIdEntrenador = new HashMap<String, Object>();
        paramIdEntrenador.put("identrenador", idEntrenador);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("searchEntrenador", paramIdEntrenador);

            if (!loginResponse.isEmpty()) {
                ParseObject userParse = loginResponse.iterator().next();
                myUserClient = new Client(userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getString("Mail"),  userParse.getDouble("Pes"), (Double) userParse.getDouble("Alcada"), userParse.getString("Objectiu"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myUserClient;
    }

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), SuperAdminDashboard.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}