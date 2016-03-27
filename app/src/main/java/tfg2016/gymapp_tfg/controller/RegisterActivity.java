package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.resources.Complements;
import tfg2016.gymapp_tfg.resources.Encrypt;

public class RegisterActivity extends Activity {

    private boolean emailcheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
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

    public View.OnClickListener clickLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to Register screen
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }
    };

    public Button.OnClickListener clickRegister = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
            if (RegisterActivity.this.getName().equalsIgnoreCase("")
                    || RegisterActivity.this.getSurname().equalsIgnoreCase("")
                    || RegisterActivity.this.getMail().equalsIgnoreCase("")
                    || RegisterActivity.this.getPassword().equalsIgnoreCase("")){
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
            }
            checkemail(RegisterActivity.this.getMail());
            if (emailcheck == true) {
                if (!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                    Intent noInternet = new Intent(RegisterActivity.this, NoInternetConnection.class);
                    startActivity(noInternet);
                }
                else{
                        boolean register = RegisterActivity.this.register(RegisterActivity.this.getName(),
                                RegisterActivity.this.getSurname(), RegisterActivity.this.getMail(),
                                RegisterActivity.this.getPassword());
                    if (register) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registrationOK), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorRegistration), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                Toast.makeText(RegisterActivity.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
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
        /*ArrayList checkRegistro = null;
        try {
            checkRegistro = ParseCloud.callFunction("checkUserSignIn", params);
            if (checkRegistro.size() == 1) {//mail existente
                return success;
            } else {
                //Creem el nou usuari a la BD
                String registerResponse = ParseCloud.callFunction("register", params);
                if (registerResponse.isEmpty() == false) //S'ha creat l'usuari
                    success = true;
                Log.i("Registration objectId:", registerResponse);

            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }*/
        ParseObject testObject = new ParseObject("CLIENTS");
        testObject.put("Nom", name);
		testObject.put("Cognom", surname);
		testObject.put("Mail", mail);
		testObject.put("Contrasenya", password);
        testObject.saveInBackground();
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