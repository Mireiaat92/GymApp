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
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;
import tfg2016.gymapp_tfg.model.User;
import tfg2016.gymapp_tfg.resources.Encrypt;

/**
 * Created by Mireia on 27/03/2016.
 */
public class ClientDashboard extends Activity {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private User myUser;
    public User getMyUser() {
        return myUser;
    }
    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            // Set View to activity_client_dashboard.xml
            setContentView(R.layout.activity_client_dashboard);

            intent = this.getIntent();
            setMyUser((User) intent.getSerializableExtra("myUser")); //serialització de l'objecte

            this.initializeButtons();

            this.initializeNomEntrenador();

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

    }

    private void initializeButtons() {
        Button addClient = (Button) findViewById(R.id.btnPerfil);

        // Listening to login link
        addClient.setOnClickListener(clickPerfil);

    }

    private void initializeNomEntrenador() {
        try {
            String mail = myUser.getMail();
            String entrenadorName= null;

            myEntrenador = getEntrenadorName(mail);
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
    }



    /**
     * Al clickar el botó addClient ens portarà a l'activitat de addClient.
     */
    public Button.OnClickListener clickPerfil = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to addClient screen
           // Intent i = new Intent(getApplicationContext(), PerfilClient.class);
           // i.putExtra("myUser", myUser);
          //  startActivity(i);

            Intent i = new Intent(getApplicationContext(), PerfilClient.class);

            try {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("mail", myUser.getMail());
                List<ParseObject> nameResponse = null;

                nameResponse = ParseCloud.callFunction("checkUserSignInClients", params);
                ParseObject userParse = nameResponse.iterator().next();
                client = new Client(userParse.getString("Nom"), /*userParse.getString("Password"),*/userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            i.putExtra("client", client);
            // Open SingleItemView.java Activity
            startActivity(i);
        }
    };
    /**
     * GetEntrenadorName
     * A partir del mail del client obtenim el nom del seu entrenador
     * @param mail
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Entrenador getEntrenadorName(String mail) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        Client myClient = null;
        String nameEntrenador;
        Entrenador myEntrenador = null;


        HashMap<String, Object> paramsClient = new HashMap<String, Object>();
        paramsClient.put("mail", mail);

        List<ParseObject> nameResponse = null;
        try {
            nameResponse = ParseCloud.callFunction("checkUserSignInClients", paramsClient);

            if (!nameResponse.isEmpty()) {
                ParseObject userParse = nameResponse.iterator().next();
                myClient = new Client(userParse.getString("Nom"), /*userParse.getString("Password"),*/
                        userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String idEntrenador = myClient.getID_Entrenador();
        if (!idEntrenador.isEmpty()) {
            HashMap<String, Object> paramsEntrenador = new HashMap<String, Object>();
            paramsEntrenador.put("objectid", myClient.getID_Entrenador());

            List<ParseObject> entrenadorResponse = null;

            try {
                entrenadorResponse = ParseCloud.callFunction("checkEntrenadorData", paramsEntrenador);

                if (!entrenadorResponse.isEmpty()) {
                    ParseObject userParse = entrenadorResponse.iterator().next();
                    myEntrenador = new Entrenador(userParse.getString("Nom"), /*userParse.getString("Password"),*/
                            userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getObjectId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else myEntrenador = null;

        return myEntrenador;
    }


    //===========================================================================   MODIFICAR  V   =================




    // RemoteDataTask AsyncTask
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

            query.whereEqualTo("ID_Client", myUser.getObjectId());
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
            adapter = new ArrayAdapter<String>(ClientDashboard.this,
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
                    Intent i = new Intent(ClientDashboard.this, TaskViewFromClient.class);

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
                    i.putExtra("selectedClient", myUser);

                    startActivity(i);
                }
            });
        }
    }


}
