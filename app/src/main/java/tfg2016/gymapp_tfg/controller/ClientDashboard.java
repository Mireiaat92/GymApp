package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.User;

/**
 * Created by Mireia on 27/03/2016.
 */
public class ClientDashboard extends Activity {

    private User myUser;
    public User getMyUser() {
        return myUser;
    }
    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    private static Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to activity_client_dashboard.xml
        setContentView(R.layout.activity_client_dashboard);

        intent = this.getIntent();
        setMyUser((User) intent.getSerializableExtra("myUser")); //serialització de l'objecte

        //this.initializeButtons();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CLIENTS");
        query.whereEqualTo("ObjectID", myUser.getObjectId());
    }

    private void initializeButtons() {

    }


}
