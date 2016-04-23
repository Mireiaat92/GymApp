package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.resources.Encrypt;

/**
 * Created by Mireia on 21/04/2016.
 */
public class AddEntrenador extends AppCompatActivity {

    private boolean emailcheck;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_add_entrenador);
        this.initializeButtons();
        initToolBar();
    }

    private void initializeButtons() {
        Button addEntrenador = (Button) findViewById(R.id.btnAddEntrenador);

        // Listening to register new account link
        addEntrenador.setOnClickListener(clickAddEntrenador);
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_entrenador);
        toolbar.setTitle(R.string.addEntrenador);

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

    /**
     * Al clickar el botó Registre es procedirà amb el procediment de registre
     */
    public Button.OnClickListener clickAddEntrenador = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                //Comprovació de que tots els camps estan omplerts sinó mostrarà popup solicitant omplir tots els camps
                if (AddEntrenador.this.getName().equalsIgnoreCase("")
                        || AddEntrenador.this.getSurname().equalsIgnoreCase("")
                        || AddEntrenador.this.getMail().equalsIgnoreCase("")
                        || AddEntrenador.this.getPassword().equalsIgnoreCase("")){
                    Toast.makeText(AddEntrenador.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
                }
                //Comprovació de que el camp mail té el format adequat - mitjaçant la funció emailcheck
                checkemail(AddEntrenador.this.getMail());
                if (emailcheck == true) {
                    //Comprovar que disposem d'internet
                        //Crida de la funció "addEntrenador" que realitzarà les comprovacions de que el mail no estigui registrat ja
                        boolean addEntrenador = AddEntrenador.this.addEntrenador(AddEntrenador.this.getName(),
                                AddEntrenador.this.getSurname(), AddEntrenador.this.getMail(), AddEntrenador.this.getPassword());
                        //si el registre es realitza correctament ho notificarà mitjançant un popup
                        if (addEntrenador) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.registrationOK), Toast.LENGTH_SHORT).show();

                            Intent tornarSuperAdmin = new Intent(AddEntrenador.this, SuperAdminDashboard.class);
                            startActivity(tornarSuperAdmin);
                            finish();
                            //si el registre falla ho notificarà mitjançant un popup
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorRegistration), Toast.LENGTH_SHORT).show();
                        }

                }
                else{
                    //Si el format de l'email no és correcte ho notificarà mitjançant un popup
                    Toast.makeText(AddEntrenador.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    };

    /**
     * Method register
     *
     * @param name
     * @param surname
     * @param mail
     * @param password
     * @return
     * @throws ParseException
     */
    public boolean addEntrenador(String name, String surname, String mail, String password) throws ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        boolean success = false;
        password = encrypt.encryptPassword(password);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("surname", surname);
        params.put("mail", mail);
        params.put("password", password);

        //Comprovem si el mail existeix a la BD
        ArrayList checkRegistro = null;
        try {
            checkRegistro = ParseCloud.callFunction("checkUserSignInClients", params);
            if (checkRegistro.size() == 1) {//mail existente
                success = false;
                return success;
            } else {
                checkRegistro = ParseCloud.callFunction("checkUserSignInEntrenadors", params);
                if (checkRegistro.size() == 1) {//mail existente
                    success = false;
                    return success;
                } else {
                    //Creem el nou usuari a la BD
                    String registerResponse = ParseCloud.callFunction("registerEntrenador", params);
                    if (registerResponse.isEmpty() == false) //S'ha creat l'usuari
                        success = true;
                    Log.i("Registration objectId:", registerResponse);
                }
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return success;
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
    /**
     * Method getName
     * @return nameText
     */
    public String getName() {
        EditText name = (EditText) findViewById(R.id.reg_name);
        String nameText = name.getText().toString();
        return nameText;
    }

    /**
     * Method getSurname
     * @return surnameText
     */
    public String getSurname() {
        EditText surname = (EditText) findViewById(R.id.reg_surname);
        String surnameText = surname.getText().toString();
        return surnameText;
    }

    /**
     * Method getMail
     * @return mailText
     */
    public String getMail() {
        EditText mail = (EditText)findViewById(R.id.reg_email);
        String mailText = mail.getText().toString();
        return mailText;
    }

    /**
     * Method getPassword
     * @return passwordText
     */
    public String getPassword() {
        EditText password = (EditText)findViewById(R.id.reg_password);
        String passwordText = password.getText().toString();
        return passwordText;
    }


    public void doBack(){
        Intent i = new Intent(getApplicationContext(), SuperAdminDashboard.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }

}