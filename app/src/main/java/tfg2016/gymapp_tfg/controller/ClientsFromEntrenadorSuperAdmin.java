package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import tfg2016.gymapp_tfg.R;
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