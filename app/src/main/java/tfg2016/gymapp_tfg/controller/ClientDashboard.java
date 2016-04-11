package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.User;
import tfg2016.gymapp_tfg.resources.Encrypt;

/**
 * Created by Mireia on 27/03/2016.
 */
public class ClientDashboard extends Activity {

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

    private Entrenador myEntrenador;

    private static Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            // Set View to activity_client_dashboard.xml
            setContentView(R.layout.activity_client_dashboard);

            intent = this.getIntent();
            setMyUser((User) intent.getSerializableExtra("myUser")); //serialització de l'objecte

            this.initializeButtons();

            String mail = myUser.getMail();
            String entrenadorname= null;

            myEntrenador = getEntrenadorName(mail);
            if (myEntrenador==null){
                entrenadorname = "Encara no tens un entrenador assignat";
            }
            else{
                entrenadorname = myEntrenador.getName() + " " + myEntrenador.getSurname();
            }
            TextView txtentrenador = (TextView) findViewById(R.id.entrenador);
            txtentrenador.setText(entrenadorname);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private void initializeButtons() {
        Button addClient = (Button) findViewById(R.id.btnPerfil);

        // Listening to login link
        addClient.setOnClickListener(clickPerfil);

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



}
