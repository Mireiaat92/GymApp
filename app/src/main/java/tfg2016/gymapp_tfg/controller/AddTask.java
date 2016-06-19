package tfg2016.gymapp_tfg.controller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class AddTask extends AppCompatActivity {

    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    private TextView tvDisplayDate;

    Toolbar toolbar;

    private int year;
    private int month;
    private int day;

    String titol;

    static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        selectedClient = (Client) intent.getSerializableExtra("selectedClient");
        myEntrenador = (Entrenador) intent.getSerializableExtra("myEntrenador");

        this.initializeButtons();
        initToolBar();

        setCurrentDateOnView();
    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons() {
        ImageButton btnChangeDate = (ImageButton) findViewById(R.id.btnChangeDate);
        btnChangeDate.setOnClickListener(clickChangeDate);

        final TextView view = (TextView) findViewById(R.id.dueDate_task);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                view.setOnClickListener(clickChangeDate);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done){
            try {
                if (AddTask.this.getDescripcio().equalsIgnoreCase("") || AddTask.this.getTitol().equalsIgnoreCase("")|| (AddTask.this.getDueDate() == null)){
                    Toast.makeText(AddTask.this, getResources().getString(R.string.allFieldsRequired), Toast.LENGTH_SHORT).show();
                } else {
                    addTask(selectedClient.getObjectId(), AddTask.this.getTitol(), AddTask.this.getDescripcio(), AddTask.this.getDueDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public ImageButton.OnClickListener clickChangeDate = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_task);
        toolbar.setTitle(R.string.addTask);

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

    /**
     * Method addTask
     *
     * @param titol, descripcio, dueDate
     * @return
     * @throws ParseException
     */
    public void addTask(String objectId, String titol, String descripcio, Date dueDate) throws ParseException {

        HashMap<String, Object> paramsAddTasca = new HashMap<String, Object>();
        paramsAddTasca.put("idclient", objectId);
        paramsAddTasca.put("titol", titol);
        paramsAddTasca.put("descripcio", descripcio);
        paramsAddTasca.put("duedate", dueDate);
        ParseCloud.callFunction("addTask", paramsAddTasca);

        Toast.makeText(AddTask.this, getResources().getString(R.string.taskAdded), Toast.LENGTH_SHORT).show();

        Intent clientActivityFromEntrenador = new Intent(AddTask.this, ClientViewFromEntrenadors.class);
        clientActivityFromEntrenador.putExtra("selectedClient", selectedClient);
        clientActivityFromEntrenador.putExtra("myEntrenador", myEntrenador);
        startActivity(clientActivityFromEntrenador);
        finish();
    }


    /**
     * Method getTitol
     *
     * @return titolText
     */
    public String getTitol() {
        EditText titol = (EditText) findViewById(R.id.titol_task);
        String titolText = titol.getText().toString();
        return titolText;
    }

    /**
     * Method getDescripcio
     *
     * @return descripcioText
     */
    public String getDescripcio() {
        EditText descripcio = (EditText) findViewById(R.id.descripcio_task);
        String descripcioText = descripcio.getText().toString();
        return descripcioText;
    }

    /**
     * Method getInitDate
     *
     * @return initDateText
     */
    public Date getDueDate() {
        TextView dueDateText = (TextView) findViewById(R.id.dueDate_task);
        String dueDateString = dueDateText.getText().toString();
        Date dueDate = ConvertStringToDate(dueDateString);
        return dueDate;
    }




    //==========================================
    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.dueDate_task);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year));
        }
    };

    /**
     * ConvertStringToDate
     * @param fecha
     * @return data
     */
    public Date ConvertStringToDate(String fecha) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date data = null;
        try {
            data = formatter.parse(fecha);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), ClientViewFromEntrenadors.class);
        i.putExtra("selectedClient", selectedClient);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }
}
