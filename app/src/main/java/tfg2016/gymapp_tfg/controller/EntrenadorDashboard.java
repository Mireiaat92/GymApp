package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
 * Created by Mireia on 01/04/2016.
 */
public class EntrenadorDashboard extends AppCompatActivity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private Client selectedClient;
    public Client getSeectedClient() {
        return selectedClient;
    }
    public void setSelelctedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }


    private static Intent intent;

    Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_entrenador_dashboard.xml
        setContentView(R.layout.activity_entrenador_dashboard);

        intent = this.getIntent();
        setMyEntrenador((Entrenador) intent.getSerializableExtra("myEntrenador")); //serialització de l'objecte

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        this.initToolBar();
        this.initFloatingButton();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_entrenador_dashboard);
        toolbar.setTitle(R.string.EntrenadorDashboard);

        setSupportActionBar(toolbar);
    }

    public void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addClient);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Pendent d'implementar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), AddClient.class);
                i.putExtra("myEntrenador", myEntrenador);
                startActivity(i);
                finish();
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_entrenador_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    */

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EntrenadorDashboard.this);
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

            query.whereEqualTo("ID_Entrenador", myEntrenador.getObjectId());
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
            adapter = new ArrayAdapter<String>(EntrenadorDashboard.this,
                    R.layout.item_entrenador_dashboard);
            // Retrieve object "name" from Parse.com database
            for (ParseObject clients : ob) {
                adapter.add((String) clients.get("Nom") + " " + clients.get("Cognom"));
            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(EntrenadorDashboard.this, ClientViewFromEntrenador.class);

                    try {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("mail", ob.get(position).getString("Mail"));
                        List<ParseObject> nameResponse = null;

                        nameResponse = ParseCloud.callFunction("checkUserSignInClients", params);
                        ParseObject userParse = nameResponse.iterator().next();
                        selectedClient = new Client(userParse.getString("Nom"), /*userParse.getString("Password"),*/userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("selectedClient", selectedClient);
                    i.putExtra("myEntrenador", myEntrenador);
                    // Open SingleItemView.java Activity
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed(){

    }
}