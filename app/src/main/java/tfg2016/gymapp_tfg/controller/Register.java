package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.resources.Complements;
import tfg2016.gymapp_tfg.resources.Encrypt;

public class Register extends Activity {

    private boolean emailcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.activity_register);
        this.initializeButtons();
    }

    private void initializeButtons() {
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        Button register = (Button) findViewById(R.id.btnRegister);

        // Listening to login link
        loginScreen.setOnClickListener(clickLogin);

        // Listening to register new account link
        register.setOnClickListener(clickRegister);

    }

    /**
     * Al clickar el botó Login ens portarà a l'activitat de login.
     */
    public View.OnClickListener clickLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to Register screen
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
    };

    /**
     * Al clickar el botó Registre es procedirà amb el procediment de registre
     */
    public Button.OnClickListener clickRegister = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
            //Comprovació de que tots els camps estan omplerts sinó mostrarà popup solicitant omplir tots els camps
            if (Register.this.getName().equalsIgnoreCase("")
                    || Register.this.getSurname().equalsIgnoreCase("")
                    || Register.this.getMail().equalsIgnoreCase("")
                    || Register.this.getPassword().equalsIgnoreCase("")){
                Toast.makeText(Register.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
            }
            //Comprovació de que el camp mail té el format adequat - mitjaçant la funció emailcheck
            checkemail(Register.this.getMail());
            if (emailcheck == true) {
                //Comprovar que disposem d'internet
                if (!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                    Intent noInternet = new Intent(Register.this, NoInternetConnection.class);
                    startActivity(noInternet);
                }
                else{
                    //Crida de la funció "registre" que realitzarà les comprovacions de que el mail no estigui registrat ja
                    boolean register = Register.this.register(Register.this.getName(),
                            Register.this.getSurname(), Register.this.getMail(), Register.this.getPassword());
                    //si el registre es realitza correctament ho notificarà mitjançant un popup
                    if (register) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registrationOK), Toast.LENGTH_SHORT).show();
                        finish();
                    //si el registre falla ho notificarà mitjançant un popup
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorRegistration), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
            //Si el format de l'email no és correcte ho notificarà mitjançant un popup
                Toast.makeText(Register.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
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
    public boolean register(String name, String surname, String mail, String password) throws ParseException {
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
                    String registerResponse = ParseCloud.callFunction("register", params);
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


}