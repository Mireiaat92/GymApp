package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.resources.Complements;
import tfg2016.gymapp_tfg.resources.Encrypt;

public class Login extends Activity {

    private boolean emailcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
        this.initializeButtons();
    }

    /**
     * Inicializació dels botons de l'activitat Login. El botó de login i el de registre.
     */
    private void initializeButtons() {
        Button login = (Button)findViewById(R.id.btnLogin);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(clickRegister);

        // Listening to login link
        login.setOnClickListener(clickLogin);
    }

    /**
     * Al clickar el botó Registre ens portarà a l'activitat del Registre
     */
    public View.OnClickListener clickRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to Register screen
            Intent i = new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
    };

    /**
     * Al clickar el botó Login es procedirà amb les comprovacions necessaries per el login.
     */
    public Button.OnClickListener clickLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Login.this.getMail().equalsIgnoreCase("") ||Login.this.getPassword().equalsIgnoreCase("")){
                Toast.makeText(Login.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
            }
            checkemail(Login.this.getMail());
            if (emailcheck == true) {
                //Comprovar que disposem d'internet
                if(!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                    Intent noInternet = new Intent(Login.this, NoInternetConnection.class);
                    startActivity(noInternet);
                }
                else {
                    //POPUP DE LOGIN
                    LayoutInflater layoutInflater
                            = (LayoutInflater) getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View popupView = layoutInflater.inflate(R.layout.popup_login, null);
                    final PopupWindow popupWindow = new PopupWindow(popupView);
                    popupWindow.setFocusable(true);
                    popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    popupWindow.showAsDropDown(popupView);
                    boolean login = false;
                    try {
                        //Crida de les funcions login
                        Client myUserClient = Login.this.loginClient(Login.this.getMail(),
                                Login.this.getPassword());
                        if (myUserClient != null) {
                            Intent userDashboard = new Intent(Login.this, CustomerDashboard.class);
                            userDashboard.putExtra("myClient", myUserClient);

                            startActivity(userDashboard);
                        } else {
                            Entrenador myUserEntrenador = Login.this.loginEntrenador(Login.this.getMail(),
                                    Login.this.getPassword());
                            if (myUserEntrenador != null) {
                                Intent entrenadorsDashboard = new Intent(Login.this, EntrenadorDashboard.class);
                                entrenadorsDashboard.putExtra("myEntrenador", myUserEntrenador);
                                startActivity(entrenadorsDashboard);
                            } else {
                                Entrenador superadmin = Login.this.loginSuperAdmin(Login.this.getMail(),
                                        Login.this.getPassword());
                                if (superadmin != null){
                                    Intent entrenadorsDashboard = new Intent(Login.this, SuperAdminDashboard.class);
                                    entrenadorsDashboard.putExtra("superadmin", superadmin);

                                    startActivity(entrenadorsDashboard);
                                    finish();
                                }
                                else {

                                    Complements.showInfoAlert(getResources().getString(R.string.loginErr), Login.this);
                                    popupWindow.dismiss();
                                }
                            }
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                //Si el format de l'email no és correcte ho notificarà mitjançant un popup
                Toast.makeText(Login.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Login.
     * Enviem el mail i el password encriptat al CloudCode "login" que s'encarregarà de l'autentcació
     * @param mail
     * @param password
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Client loginClient(String mail, String password) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        Client myUserClient = null;

        password = encrypt.encryptPassword(password);
        HashMap<String, Object> loginParams = new HashMap<String, Object>();
        loginParams.put("mail", mail);
        loginParams.put("password", password);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("loginClient", loginParams);

            if (!loginResponse.isEmpty()) {
                ParseObject userParse = loginResponse.iterator().next();
                myUserClient = new Client(userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getString("Mail"),  userParse.getDouble("Pes"), (Double) userParse.getDouble("Alcada"), userParse.getString("Objectiu"), userParse.getObjectId(), userParse.getString("ID_Entrenador"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myUserClient;
    }

    /**
     * Login.
     * Enviem el mail i el password encriptat al CloudCode "login" que s'encarregarà de l'autentcació
     * @param mail
     * @param password
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Entrenador loginEntrenador(String mail, String password) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        Entrenador myUserEntrenador = null;

        password = encrypt.encryptPassword(password);
        HashMap<String, Object> loginParams = new HashMap<String, Object>();
        loginParams.put("mail", mail);
        loginParams.put("password", password);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("loginEntrenador", loginParams);

            if(!loginResponse.isEmpty()) {
                ParseObject userParse = loginResponse.iterator().next();
                myUserEntrenador = new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"),
                        userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myUserEntrenador;
    }


    public Entrenador loginSuperAdmin(String mail, String password) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        Entrenador adminEntrenador = null;

        password = encrypt.encryptPassword(password);
        HashMap<String, Object> loginParams = new HashMap<String, Object>();
        loginParams.put("mail", mail);
        loginParams.put("password", password);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("loginSuperAdmin", loginParams);

            if(!loginResponse.isEmpty()) {
                ParseObject userParse = loginResponse.iterator().next();
                adminEntrenador = new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"),
                        userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return adminEntrenador;
    }

    /**
     * Method getMail
     * @return userText
     */
    public String getMail() {
        EditText user = (EditText) findViewById(R.id.email);
        String userText = user.getText().toString();
        return userText;
    }

    /**
     * Method getPassw
     * @return passwText
     */
    public String getPassword() {
        EditText passw = (EditText) findViewById(R.id.password);
        String passwText = passw.getText().toString();
        return passwText;
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

    @Override
    public void onBackPressed(){
    }
}