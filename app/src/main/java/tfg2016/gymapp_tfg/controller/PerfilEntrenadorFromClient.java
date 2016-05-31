package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 31/05/2016.
 */
public class PerfilEntrenadorFromClient extends AppCompatActivity {       //TODO Pendent d'implementar

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    private String entrenadorID;


    private static Intent intent;

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_client_perfil.xml
        setContentView(R.layout.activity_perfil_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        entrenadorID = ((String) i.getSerializableExtra("myEntrenadorId"));
        setMyClient((Client) i.getSerializableExtra("myClient"));

        this.initializeEntrenadorData();
        initToolBar();

    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_perfil_entrenador);
        toolbar.setTitle(myEntrenador.getName() + " " + myEntrenador.getSurname());

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


    public void initializeEntrenadorData() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("valueFieldTable", entrenadorID);
        params.put("table", "ENTRENADORS");
        params.put("field", "objectId");

        List<ParseObject> entrenadorResponse = null;
        try {
            entrenadorResponse = ParseCloud.callFunction("getData", params);

            ParseObject userParse = entrenadorResponse.iterator().next();
            setMyEntrenador(new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"),
                    userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String name = myEntrenador.getName() + " " + myEntrenador.getSurname();
        TextView txtname = (TextView) findViewById(R.id.name);
        txtname.setText(name);

        String mail = myEntrenador.getMail();
        TextView txtmail = (TextView) findViewById(R.id.mail);
        txtmail.setText(mail);

        String especialitats = myEntrenador.getEspecialitats();
        TextView txtespecialitats = (TextView) findViewById(R.id.especialitats);
        txtespecialitats.setText(especialitats);

        try {
            ParseFile image;
            image = Complements.getDataFromBBDD(myEntrenador.getObjectId(), "ENTRENADORS", "objectId").get(0).getParseFile("Foto");
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Complements.setImageViewWithParseFile(imageView, image, true);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientDashboard.class);
        i.putExtra("myClient", myClient);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }

}
