package tfg2016.gymapp_tfg.controller;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;


/**
 * Created by Mireia on 01/04/2016.
 */
public class ClientViewFromEntrenador extends AppCompatActivity {
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
        setContentView(R.layout.activity_client_from_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        selectedClient = (Client) i.getSerializableExtra("selectedClient");
        myEntrenador = (Entrenador) i.getSerializableExtra("myEntrenador");

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        initToolBar();
        this.initFloatingButton();
    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_client_from_entrenador);
        toolbar.setTitle(selectedClient.getName() + " " + selectedClient.getSurname());

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

    public void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Pendent d'implementar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                // Switching to addTask screen
                Intent i = new Intent(getApplicationContext(), AddTask.class);
                i.putExtra("selectedClient", selectedClient);
                i.putExtra("myEntrenador", myEntrenador);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_view_from_entrenador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_profile) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), PerfilClientFromEntrenador.class);
            i.putExtra("selectedClient", selectedClient);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    //===============================================0
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ClientViewFromEntrenador.this);
            // Set progressdialog title
            mProgressDialog.setTitle(getResources().getString(R.string.accessingTasks));
            // Set progressdialog message
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "TASQUES" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TASQUES");
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
            adapter = new ArrayAdapter<String>(ClientViewFromEntrenador.this,
                    R.layout.item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject tasques : ob) {

                String date = convertStringToDate(tasques.getDate("Due_Date"));

                adapter.add(date + " - "+ tasques.get("Titol"));

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
                    Intent i = new Intent(ClientViewFromEntrenador.this, TaskViewFromEntrenador.class);

                    String selectedTascaId = ob.get(position).getObjectId();


                    i.putExtra("selectedTascaId", selectedTascaId);
                    i.putExtra("selectedClient", selectedClient);
                    i.putExtra("myEntrenador", myEntrenador);

                    startActivity(i);
                    finish();
                }
            });
        }
    }

    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd MMM");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), EntrenadorDashboard.class);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}