package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.User;

/**
 * Created by Mireia on 03/04/2016.
 */
public class AddClient extends AppCompatActivity {

    private User myUser;
    private boolean emailcheck;
    Toolbar toolbar;

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        Intent intent = getIntent();
        myEntrenador = (Entrenador) intent.getSerializableExtra("myEntrenador");

        this.initializeButtons();

        initToolBar();
    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons(){
        Button addClient = (Button) findViewById(R.id.addClient);
        addClient.setOnClickListener(clickAddClient);
    }
    public Button.OnClickListener clickAddClient = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                if (AddClient.this.getMail().equalsIgnoreCase("")) {
                    Toast.makeText(AddClient.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
                } else {
                    //Comprovem que el camp mail tingui el format adequat
                    checkemail(AddClient.this.getMail());
                    if (emailcheck == true) {
                        addClient(AddClient.this.getMail());
                    } else {
                        Toast.makeText(AddClient.this, "Adreça de correu incorrecte", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_client);
        toolbar.setTitle(R.string.addClient);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_client) {
            // Switching to addClient screen
            Intent i = new Intent(getApplicationContext(), AddClient.class);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method addClient
     * @param mail
     * @return
     * @throws ParseException
     */
    public void addClient(String mail) throws ParseException {
        HashMap<String, Object> paramsCheckMail = new HashMap<String, Object>();
        paramsCheckMail.put("mail", mail);
        //Comprovem si el mail existeix a la BD
        List<ParseObject> checkRegistro = ParseCloud.callFunction("checkUserSignInClients", paramsCheckMail);

        if (checkRegistro.size() == 1) {//mail e
            HashMap<String, Object> paramsAddEntrenador = new HashMap<String, Object>();
            paramsAddEntrenador.put("mail", mail);
            paramsAddEntrenador.put("identrenador", myEntrenador.getObjectId());
            ParseCloud.callFunction("addEntrenador", paramsAddEntrenador);

            Toast.makeText(AddClient.this, "Client afegit correctament", Toast.LENGTH_SHORT).show();

            Intent userDashboard = new Intent(AddClient.this, EntrenadorDashboard.class);
            userDashboard.putExtra("myEntrenador", myEntrenador);
            startActivity(userDashboard);
        }
        else{
            Toast.makeText(AddClient.this, getResources().getString(R.string.userNoExist), Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Method getUser
     * @return userText
     */
    public String getMail() {
        EditText mail = (EditText) findViewById(R.id.mail_client);
        String mailText = mail.getText().toString();
        return mailText;
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


    public void doBack(){
        Intent i = new Intent(getApplicationContext(), EntrenadorDashboard.class);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}