package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 10/04/2016.
 */
public class PerfilClient extends AppCompatActivity {
    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_client_perfil.xml
        setContentView(R.layout.activity_perfil_client);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        setClient((Client) i.getSerializableExtra("client"));

        this.initializeClientData();
        initToolBar();

    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_client_perfil);
        toolbar.setTitle(client.getName() + " " + client.getSurname());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.editPerfil){
            Intent i = new Intent(this, EditPerfilClient.class);
            i.putExtra("client", client);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeClientData() {

        TextView txtname = (TextView) findViewById(R.id.clientName);
        txtname.setText(client.getName() + " " + client.getSurname());

        TextView txtmail = (TextView) findViewById(R.id.clientMail);
        txtmail.setText(client.getMail());

        String weightValue = null;
        TextView weight = (TextView)findViewById(R.id.weight);
        if (client.getWeight().toString() == null || client.getWeight().toString() == "0.0"){
            weightValue = "not assigned";
        }
        else{
            weightValue = client.getWeight().toString() + " kg";
        }
        weight.setText(weightValue);

        String heightValue = null;
        TextView height = (TextView)findViewById(R.id.height);
        if (client.getHeight().toString() == null || client.getHeight().toString() == "0.0"){
            heightValue = "not assigned";
        }
        else{
            heightValue = client.getHeight().toString() + " cm";
        }
        height.setText(heightValue);

        TextView objectiu = (TextView)findViewById(R.id.clientObjectiu);
        objectiu.setText(client.getObjectiu());

        try {
            ParseFile image;
            image = Complements.getDataFromBBDD(client.getObjectId(), "CLIENTS", "objectId").get(0).getParseFile("Foto");
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Complements.setImageViewWithParseFile(imageView, image, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientDashboard.class);
        i.putExtra("myClient", client);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
