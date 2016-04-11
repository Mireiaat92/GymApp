package tfg2016.gymapp_tfg.controller;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Tasca;


/**
 * Created by Mireia on 01/04/2016.
 */
public class ClientActivityFromEntrenador extends Activity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

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

    // Declare Variables
    TextView txtname;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_client_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedClient = (Client) i.getSerializableExtra("selectedClient");

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        this.initializeButtons();
    }

    /**
     * Inicializació dels botons de l'activitat ClientActivityFromEntrenador. Perfil
     */
    private void initializeButtons() {
        Button perfilClient = (Button) findViewById(R.id.btnPerfilClient);
        Button addTask = (Button) findViewById(R.id.btnAddTask);

        // Listening to login link
        perfilClient.setOnClickListener(clickPerfilClient);
        addTask.setOnClickListener(clickAddtask);
    }

    /**
     * Al clickar el botó PerfilClient ens portarà a l'activitat PerfilClientFromEntrenador
     */
    public Button.OnClickListener clickPerfilClient = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), ActivityPerfilClientFromEntrenador.class);
            i.putExtra("selectedClient", selectedClient);
            startActivity(i);
        }
    };

    /**
     * Al clickar el botó addTask ens portarà a l'activitat de addTask.
     */
    public Button.OnClickListener clickAddtask = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), AddTask.class);
            i.putExtra("selectedClient", selectedClient);
            startActivity(i);
        }
    };



    //===========================================================================   MODIFICAR  V   =================




    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ClientActivityFromEntrenador.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Accedint a la llista de tasques");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
                // Locate the class table named "TASQUES" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "TASQUES");

            query.whereEqualTo("ID_Client", selectedClient.getObjectId());
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
            listview = (ListView) findViewById(R.id.tascalistview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(ClientActivityFromEntrenador.this,
                    R.layout.item_entrenador_dashboard);
            // Retrieve object "name" from Parse.com database
            for (ParseObject tasques : ob) {
                adapter.add((String) tasques.get("Titol"));
            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(ClientActivityFromEntrenador.this, TaskActivityFromEntrenador.class);

                    try {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        //params.put("idclient", ob.get(position).getObjectId());
                        params.put("objectid", ob.get(position).getObjectId());
                        List<ParseObject> nameResponse = null;

                        nameResponse = ParseCloud.callFunction("checkTascaData", params);
                        ParseObject userParse = nameResponse.iterator().next();
                        selectedTasca = new Tasca(userParse.getString("idClient"), userParse.getString("Titol"), userParse.getString("Descripcio"), userParse.getDate("Due_Date"), userParse.getObjectId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("selectedTasca", selectedTasca);
                    i.putExtra("selectedClient", selectedClient);

                    startActivity(i);
                }
            });
        }
    }
}