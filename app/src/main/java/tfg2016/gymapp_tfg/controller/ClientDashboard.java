package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 27/03/2016.
 */
public class ClientDashboard extends AppCompatActivity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

    private Entrenador myEntrenador;

    private static Intent intent;


    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set View to activity_client_dashboard.xml
        setContentView(R.layout.activity_client_dashboard);

        intent = this.getIntent();
        myClient = (Client) intent.getSerializableExtra("myClient"); //serialització de l'objecte

        initToolBar();

        this.initializeNomEntrenador();

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

    }


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_client_dashboard);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText(myClient.getName() + " " + myClient.getSurname());

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_perm_identity);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PerfilClient.class);
                        i.putExtra("client", myClient);
                        startActivity(i);
                        finish();
                    }
                }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_message){
            Intent i = new Intent(getApplicationContext(), ChatFromClient.class);
            i.putExtra("myClient", myClient);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    private void initializeNomEntrenador() {
        try {
            //String mail = myClient.getMail();
            String entrenadorName= null;

            myEntrenador = getEntrenadorName();
            if (myEntrenador==null){
                entrenadorName = "Encara no tens un entrenador assignat";
            }
            else{
                entrenadorName = myEntrenador.getName() + " " + myEntrenador.getSurname();
            }
            TextView txtentrenador = (TextView) findViewById(R.id.entrenador);
            txtentrenador.setText(entrenadorName);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.infoEntrenador);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (myEntrenador==null){
                    Intent i = new Intent(ClientDashboard.this, NoEntrenador.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(ClientDashboard.this, PerfilEntrenadorFromClient.class);
                    i.putExtra("myEntrenadorId", myEntrenador.getObjectId());
                    i.putExtra("myClient", myClient);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    /**
     * GetEntrenadorName
     * A partir del mail del client obtenim el nom del seu entrenador
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Entrenador getEntrenadorName() throws java.text.ParseException {

        Entrenador myEntrenador = null;

        String idEntrenador = myClient.getID_Entrenador();
        if (!idEntrenador.isEmpty()) {
            HashMap<String, Object> paramsEntrenador = new HashMap<String, Object>();
            paramsEntrenador.put("objectid", myClient.getID_Entrenador());

            List<ParseObject> entrenadorResponse = null;

            try {
                entrenadorResponse = ParseCloud.callFunction("checkEntrenadorData", paramsEntrenador);

                if (!entrenadorResponse.isEmpty()) {
                    ParseObject userParse = entrenadorResponse.iterator().next();
                    myEntrenador = new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else myEntrenador = null;

        return myEntrenador;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ClientDashboard.this);
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

            query.whereEqualTo("ID_Client", myClient.getObjectId());
            query.orderByAscending("Due_Date");
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
            adapter = new ArrayAdapter<String>(ClientDashboard.this,
                    R.layout.item_task);
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
                    Intent i = new Intent(ClientDashboard.this, TaskViewFromClient.class);

                    try {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("objectid", ob.get(position).getObjectId());
                        List<ParseObject> nameResponse = null;

                        nameResponse = ParseCloud.callFunction("checkTascaData", params);
                        ParseObject userParse = nameResponse.iterator().next();
                        selectedTasca = new Tasca(userParse.getString("idClient"), userParse.getString("Titol"), userParse.getString("Descripcio"), userParse.getDate("Due_Date"), userParse.getBoolean("Completada"), userParse.getString("Comentari"), userParse.getObjectId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("selectedTasca", selectedTasca);
                    i.putExtra("myClient", myClient);

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

    @Override
    public void onBackPressed(){

    }


}