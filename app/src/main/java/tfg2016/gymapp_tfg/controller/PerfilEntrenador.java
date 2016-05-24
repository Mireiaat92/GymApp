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

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 18/05/2016.
 */
public class PerfilEntrenador extends AppCompatActivity {       //TODO Pendent d'implementar

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }


    private static Intent intent;

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_client_perfil.xml
        setContentView(R.layout.activity_perfil_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        setMyEntrenador((Entrenador) i.getSerializableExtra("myEntrenador"));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_entrenador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.editPerfil){
            Intent i = new Intent(this, EditPerfilEntrenador.class);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeEntrenadorData() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("valueFieldTable", myEntrenador.getObjectId());
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
        Intent i = new Intent(getApplicationContext(), EntrenadorDashboard.class);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }

}
