package tfg2016.gymapp_tfg.controller;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 26/03/2016.
 */
public class NoInternetConnection extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        this.initializeButtons();
        //initToolBar();
    }

    private void initializeButtons() {
        Button errorConnection = (Button)findViewById(R.id.tryAgain);
        errorConnection.setOnClickListener(clickRetry);

    }

    public Button.OnClickListener clickRetry = new Button.OnClickListener() {
        public void onClick(View v) {
            if(!Complements.isNetworkStatusAvialable(getApplicationContext())) {
                Toast.makeText(NoInternetConnection.this, getResources().getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
            }
            else{
                Intent login = new Intent(NoInternetConnection.this, Login.class);
                startActivity(login);
            }
        }
    };

}