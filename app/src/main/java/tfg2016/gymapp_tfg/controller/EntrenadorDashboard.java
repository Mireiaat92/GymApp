package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
        ((TextView) findViewById(R.id.main_toolbar_title)).setText(myEntrenador.getName() + " " + myEntrenador.getSurname());

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

        if (id == R.id.action_profile){
            Intent i = new Intent(getApplicationContext(), PerfilEntrenador.class);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EntrenadorDashboard.this);
            // Set progressdialog title
            mProgressDialog.setTitle(getResources().getString(R.string.accessingClients));
            // Set progressdialog message
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
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
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(EntrenadorDashboard.this, ClientViewFromEntrenadors.class);

                    try {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("mail", ob.get(position).getString("Mail"));
                        List<ParseObject> nameResponse = null;

                        nameResponse = ParseCloud.callFunction("checkUserSignInClients", params);
                        ParseObject userParse = nameResponse.iterator().next();
                        selectedClient = new Client(userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getString("Mail"),  userParse.getDouble("Pes"), (Double) userParse.getDouble("Alcada"), userParse.getString("Objectiu"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));
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
            listview.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EntrenadorDashboard.this);
                    // set title
                    String alert_title = (getResources().getString(R.string.confirmation));
                    String alert_description = (getResources().getString(R.string.areYouSureDeleteClient));
                    alertDialogBuilder.setTitle(alert_title);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage(alert_description)
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.yes),new DialogInterface.OnClickListener() {
                                // Lo que sucede si se pulsa yes
                                public void onClick(DialogInterface dialog,int id) {
                                    //ELIMINAR CLIENTE
                                    HashMap<String, Object> paramsQuery = new HashMap<String, Object>();
                                    paramsQuery.put("clientId", selectedClient.getObjectId());

                                    try {
                                        ParseCloud.callFunction("deleteClient", paramsQuery);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Intent i = new Intent(getApplicationContext(), EntrenadorDashboard.class);
                                    i.putExtra("myEntrenador", myEntrenador);
                                    startActivity(i);
                                }


                            })
                            .setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // Si se pulsa no no hace nada
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                    return false;

                }

            });

                }
    }

    @Override
    public void onBackPressed(){

    }
}