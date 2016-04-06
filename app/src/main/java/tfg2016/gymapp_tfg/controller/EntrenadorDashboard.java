package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.User;


/**
 * Created by Mireia on 01/04/2016.
 */
public class EntrenadorDashboard extends Activity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;


    private User myUser;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    private static Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_entrenador_dashboard.xml
        setContentView(R.layout.activity_entrenador_dashboard);

        intent = this.getIntent();
        setMyUser((User) intent.getSerializableExtra("myUser")); //serialització de l'objecte

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        this.initializeButtons();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    /**
     * Inicializació dels botons de l'activitat EntrenadorDashboard. AddClient
     */
    private void initializeButtons() {
        Button addClient = (Button) findViewById(R.id.btnAddClient);

        // Listening to login link
        addClient.setOnClickListener(clickAddClient);
    }

    /**
     * Al clickar el botó addClient ens portarà a l'activitat de addClient.
     */
    public Button.OnClickListener clickAddClient = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {


            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), ActivityAddClient.class);
            i.putExtra("myUser", myUser);
            startActivity(i);
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EntrenadorDashboard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://tfg2016.gymapp_tfg.controller/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);------------------------------------------------------aki
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EntrenadorDashboard Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://tfg2016.gymapp_tfg.controller/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);---------------------------------------------------aki
        client.disconnect();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(EntrenadorDashboard.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Parse.com Simple ListView Tutorial");
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

            query.whereEqualTo("ID_Entrenador", myUser.getObjectId());
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
                adapter.add((String) clients.get("Nom"));
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
                    Intent i = new Intent(EntrenadorDashboard.this,
                            ClientActivityFromEntrenador.class);
                    // Pass data "name" followed by the position
                    i.putExtra("Nom", ob.get(position).getString("Nom")
                            .toString());
                    // Open SingleItemView.java Activity
                    startActivity(i);
                }
            });
        }
    }
}