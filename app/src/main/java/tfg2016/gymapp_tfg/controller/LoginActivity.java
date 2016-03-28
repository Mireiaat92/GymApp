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

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.User;
import tfg2016.gymapp_tfg.resources.Complements;
import tfg2016.gymapp_tfg.resources.Encrypt;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
        this.initializeButtons();
        Parse.initialize(this, "1SDM9Lv7AxwkSgfmgR2kXWnhTBHhsBSYGiMfGkLW", "zq8RaTSdErsyFNhb0LbCQbwkkbpl3UCm1M9ly4Hk");

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/
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
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
        }
    };

    /**
     * Al clickar el botó Login es procedirà amb les comprovacions necessaries per el login.
     */
    public Button.OnClickListener clickLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Comrpovar que disposem d'internet
           if(!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                Intent noInternet = new Intent(LoginActivity.this, NoInternetConnection.class);
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
                   //Crida de la funció login
                   User myUserClient = LoginActivity.this.loginClient(LoginActivity.this.getUser(),
                           LoginActivity.this.getPassword());
                   if (myUserClient != null) {
                       Intent userDashboard = new Intent(LoginActivity.this, ClientDashboard.class);
                       userDashboard.putExtra("myUser", myUserClient);
                       startActivity(userDashboard);
                   } else {
                       User myUserEntrenador = LoginActivity.this.loginEntrenador(LoginActivity.this.getUser(),
                               LoginActivity.this.getPassword());
                       if (myUserEntrenador != null) {
                           Intent userDashboard = new Intent(LoginActivity.this, EntrenadorDashboard.class);
                           userDashboard.putExtra("myUser", myUserEntrenador);
                           startActivity(userDashboard);
                       } else {
                           Complements.showInfoAlert(getResources().getString(R.string.loginErr), LoginActivity.this);
                           popupWindow.dismiss();
                       }
                   }
               } catch (java.text.ParseException e) {
                   e.printStackTrace();
               }
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
    public User loginClient(String mail, String password) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        User myUser = null;
        password = encrypt.encryptPassword(password);
        HashMap<String, Object> loginParams = new HashMap<String, Object>();
        loginParams.put("mail", mail);
        loginParams.put("password", password);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("loginClient", loginParams);

        if(!loginResponse.isEmpty()) {
            ParseObject userParse = loginResponse.iterator().next();
            myUser = new User(userParse.getString("Mail"), userParse.getString("Password"),
                    userParse.getString("Nom"), userParse.getString("Cognom"));
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myUser;
    }

    /**
     * Login.
     * Enviem el mail i el password encriptat al CloudCode "login" que s'encarregarà de l'autentcació
     * @param mail
     * @param password
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public User loginEntrenador(String mail, String password) throws java.text.ParseException {
        Encrypt encrypt = new Encrypt(getApplicationContext());
        User myUser = null;
        password = encrypt.encryptPassword(password);
        HashMap<String, Object> loginParams = new HashMap<String, Object>();
        loginParams.put("mail", mail);
        loginParams.put("password", password);

        List<ParseObject> loginResponse = null;
        try {
            loginResponse = ParseCloud.callFunction("loginEntrenador", loginParams);

            if(!loginResponse.isEmpty()) {
                ParseObject userParse = loginResponse.iterator().next();
                myUser = new User(userParse.getString("Mail"), userParse.getString("Password"),
                        userParse.getString("Nom"), userParse.getString("Cognom"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myUser;
    }

    /**
     * Method getUser
     * @return userText
     */
    public String getUser() {
        EditText user = (EditText) findViewById(R.id.user);
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
}
