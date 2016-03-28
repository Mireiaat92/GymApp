package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.os.Bundle;

import tfg2016.gymapp_tfg.R;

/**
 * Created by Mireia on 27/03/2016.
 */
public class ClientDashboard extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to activity_user_dashboard.xml
        setContentView(R.layout.activity_user_dashboard);
        this.initializeButtons();
    }

    private void initializeButtons() {

    }
}
