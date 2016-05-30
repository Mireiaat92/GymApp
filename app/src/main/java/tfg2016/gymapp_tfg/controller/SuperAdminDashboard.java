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

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 21/04/2016.
 */
public class SuperAdminDashboard extends AppCompatActivity {

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
        setContentView(R.layout.activity_superadmin_dashboard);

        intent = this.getIntent();
        setSelectedEntrenador((Entrenador) intent.getSerializableExtra("superadmin")); //serialització de l'objecte

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_superadmin_dashboard);
        toolbar.setTitle(R.string.SuperAdminDashboard);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_superadmin_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_entrenador) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), AddEntrenador.class);
            startActivity(i);
            finish();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(SuperAdminDashboard.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Accedint a la llista d'entrenadors");
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
                    "ENTRENADORS");
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
            adapter = new ArrayAdapter<String>(SuperAdminDashboard.this,
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

            //===============================
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(SuperAdminDashboard.this, ClientsFromEntrenadorSuperAdmin.class);

                    try {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("mail", ob.get(position).getString("Mail"));
                        List<ParseObject> nameResponse = null;

                        nameResponse = ParseCloud.callFunction("checkUserSignInEntrenadors", params);
                        ParseObject userParse = nameResponse.iterator().next();
                        selectedEntrenador = new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"),
                                userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("selectedEntrenador", selectedEntrenador);
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
