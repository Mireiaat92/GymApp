package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.User;

/**
 * Created by Mireia on 03/04/2016.
 */
public class ActivityAddClient extends Activity {

    private User myUser;
    private boolean emailcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        Intent intent = getIntent();
        myUser = (User) intent.getSerializableExtra("myUser");

        this.initializeButtons();
    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons(){
        Button addClient = (Button) findViewById(R.id.addClient);
        addClient.setOnClickListener(clickAddClient);
    }
    public Button.OnClickListener clickAddClient = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                if (ActivityAddClient.this.getMail().equalsIgnoreCase("")) {
                    Toast.makeText(ActivityAddClient.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
                } else {
                //Comprovem que el camp mail tingui el format adequat
                checkemail(ActivityAddClient.this.getMail());
                if (emailcheck == true) {
                    addClient(ActivityAddClient.this.getMail());
                } else {
                    Toast.makeText(ActivityAddClient.this, "Adreça de correu incorrecte", Toast.LENGTH_SHORT).show();
                }
            }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Method addClient
     * @param mail
     * @return
     * @throws ParseException
     */
    public void addClient(String mail) throws ParseException {
        HashMap<String, Object> paramsCheckMail = new HashMap<String, Object>();
        paramsCheckMail.put("mail", mail);
        //Comprovem si el mail existeix a la BD
        List<ParseObject> checkRegistro = ParseCloud.callFunction("checkUserSignInClients", paramsCheckMail);

        if (checkRegistro.size() == 1) {//mail e
            HashMap<String, Object> paramsAddEntrenador = new HashMap<String, Object>();
            paramsAddEntrenador.put("mail", mail);
            paramsAddEntrenador.put("identrenador", myUser.getObjectId());
            ParseCloud.callFunction("addEntrenador", paramsAddEntrenador);

            Toast.makeText(ActivityAddClient.this, "Client afegit correctament", Toast.LENGTH_SHORT).show();

            Intent userDashboard = new Intent(ActivityAddClient.this, EntrenadorDashboard.class);
            userDashboard.putExtra("myUser", myUser);
            startActivity(userDashboard);
        }
        else{
            Toast.makeText(ActivityAddClient.this, getResources().getString(R.string.userNoExist), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Method getUser
     * @return userText
     */
    public String getMail() {
        EditText mail = (EditText) findViewById(R.id.mail_client);
        String mailText = mail.getText().toString();
        return mailText;
    }
    /**
     * Method checkemail
     * @param mail
     */
    public void checkemail(String mail) {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(mail);
        emailcheck = matcher.matches();
    }
}
