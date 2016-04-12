package tfg2016.gymapp_tfg.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 26/03/2016.
 */
public class NoInternetConnection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        this.initializeButtons();
    }

    private void initializeButtons() {
        Button errorConnection = (Button)findViewById(R.id.tryAgain);
        errorConnection.setOnClickListener(clickRetry);

    }

    public Button.OnClickListener clickRetry = new Button.OnClickListener() {
        public void onClick(View v) {
            if(Complements.isNetworkStatusAvialable(getApplicationContext())) {
                Intent login = new Intent(NoInternetConnection.this, Login.class);
                startActivity(login);
            }
            else Toast.makeText(getApplicationContext(), getResources().getString(R.string.NotInternet), Toast.LENGTH_SHORT).show();
        }
    };

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_error_connection, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}