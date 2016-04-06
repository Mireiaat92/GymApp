package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
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

    private static Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            // Set View to activity_client_dashboard.xml
            setContentView(R.layout.activity_client_dashboard);

            intent = this.getIntent();
            setMyUser((User) intent.getSerializableExtra("myUser")); //serialització de l'objecte

            //this.initializeButtons();

            String userId = myUser.getObjectId();


            getEntrenadorName(userId);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private void initializeButtons() {

    }
    /**
     * Login.
     * Enviem el mail i el password encriptat al CloudCode "login" que s'encarregarà de l'autentcació
     * @param objectId
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Client getEntrenadorName(String objectId) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        Client myClient = null;



        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("mail", getMyUser().getMail());

        List<ParseObject> nameResponse = null;
        try {
            nameResponse = ParseCloud.callFunction("checkUserSignInClients", params);

            if (!nameResponse.isEmpty()) {
                ParseObject userParse = nameResponse.iterator().next();
                myClient = new Client(userParse.getString("Mail"), userParse.getString("Password"),
                        userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));

                Toast.makeText(ClientDashboard.this, userParse.getString("ID_Entrenador"), Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return myClient;
    }

}
