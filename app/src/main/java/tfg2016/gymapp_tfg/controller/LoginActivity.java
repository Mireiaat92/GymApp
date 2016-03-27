package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tfg2016.gymapp_tfg.R;

public class LoginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
        this.initializeButtons();
    }

    private void initializeButtons() {
        //Button login = (Button)findViewById(R.id.btnLogin);
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(clickRegister);

        // Listening to login link
        //login.setOnClickListener(clickLogin);
    }

    public View.OnClickListener clickRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Switching to Register screen
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
        }

    };

    public Button.OnClickListener clickLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* if(!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                Intent noInternet = new Intent(LoginActivity.this, NoInternetConnection.class);
                startActivity(noInternet);
            }*/
        }

    };
}
