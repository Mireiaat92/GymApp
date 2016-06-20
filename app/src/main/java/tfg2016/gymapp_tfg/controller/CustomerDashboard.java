package tfg2016.gymapp_tfg.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 18/06/2016.
 */
public class CustomerDashboard extends AppCompatActivity {

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    private static Intent intent;

    private Entrenador myEntrenador;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
               intent = this.getIntent();
        myClient = (Client) intent.getSerializableExtra("myClient");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        initToolBar();
        this.initializeNomEntrenador();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Pending Tasks"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed Tasks"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ClientPagerAdapter adapter = new ClientPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), myClient);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_customer_dashboard);
        ((TextView) findViewById(R.id.main_toolbar_title)).setText(myClient.getName() + " " + myClient.getSurname());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_perm_identity);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), PerfilClient.class);
                        i.putExtra("client", myClient);
                        startActivity(i);
                        finish();
                    }
                }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_message){
            Intent i = new Intent(getApplicationContext(), ChatFromClient.class);
            i.putExtra("myClient", myClient);
            i.putExtra("myEntrenador", myEntrenador);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void initializeNomEntrenador() {
        try {
            //String mail = myClient.getMail();
            String entrenadorName= null;

            myEntrenador = getEntrenadorName();
            if (myEntrenador==null){
                entrenadorName = getResources().getString(R.string.noTrainer);
            }
            else{
                entrenadorName = myEntrenador.getName() + " " + myEntrenador.getSurname();
            }
            TextView txtentrenador = (TextView) findViewById(R.id.entrenador);
            txtentrenador.setText(entrenadorName);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.infoEntrenador);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (myEntrenador==null){
                    Intent i = new Intent(CustomerDashboard.this, NoEntrenador.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(CustomerDashboard.this, PerfilEntrenadorFromClient.class);
                    i.putExtra("myEntrenadorId", myEntrenador.getObjectId());
                    i.putExtra("myClient", myClient);
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    /**
     * GetEntrenadorName
     * A partir del mail del client obtenim el nom del seu entrenador
     * @return myUser - Dades de l'usuaa
     * @throws ParseException
     */
    public Entrenador getEntrenadorName() throws java.text.ParseException {

        Entrenador myEntrenador = null;

        String idEntrenador = myClient.getID_Entrenador();
        if (!idEntrenador.isEmpty()) {
            HashMap<String, Object> paramsEntrenador = new HashMap<String, Object>();
            paramsEntrenador.put("objectid", myClient.getID_Entrenador());

            List<ParseObject> entrenadorResponse = null;

            try {
                entrenadorResponse = ParseCloud.callFunction("checkEntrenadorData", paramsEntrenador);

                if (!entrenadorResponse.isEmpty()) {
                    ParseObject userParse = entrenadorResponse.iterator().next();
                    myEntrenador = new Entrenador(userParse.getString("Nom"), userParse.getString("Cognom"), userParse.getString("Mail"), userParse.getString("Especialitats"), userParse.getObjectId());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else myEntrenador = null;

        return myEntrenador;
    }
}