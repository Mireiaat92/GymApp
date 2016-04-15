package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import tfg2016.gymapp_tfg.model.Tasca;

/**
 * Created by Mireia on 11/04/2016.
 */
public class AddTask extends Activity {

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

    private TextView tvDisplayDate;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        selectedClient = (Client) intent.getSerializableExtra("selectedClient");

        this.initializeButtons();

        setCurrentDateOnView();
    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons() {
        Button addTask = (Button) findViewById(R.id.AddNewTask);
        addTask.setOnClickListener(clickAddTask);

        Button btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
        btnChangeDate.setOnClickListener(clickChangeDate);

    }

    public Button.OnClickListener clickAddTask = new Button.OnClickListener() {
        public void onClick(View v) {
            try {
                if (AddTask.this.getTitol().equalsIgnoreCase("") || AddTask.this.getDescripcio().equalsIgnoreCase("")) {
                    Toast.makeText(AddTask.this, "Els camps titol i descripcio son obligatoris", Toast.LENGTH_SHORT).show();
                } else {
                    addTask(selectedClient.getObjectId(), AddTask.this.getTitol(), AddTask.this.getDescripcio(), AddTask.this.getDueDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    public Button.OnClickListener clickChangeDate = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

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

        Toast.makeText(AddTask.this, "Tasca afegida correctament", Toast.LENGTH_SHORT).show();

        Intent clientActivityFromEntrenador = new Intent(AddTask.this, ClientViewFromEntrenador.class);
        clientActivityFromEntrenador.putExtra("selectedClient", selectedClient);
        startActivity(clientActivityFromEntrenador);

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
     * Method getDueDate
     *
     * @return dueDateText
     */
    public Date getDueDate() {
        EditText dueDateText = (EditText) findViewById(R.id.duedate_task);
        String dueDateString = dueDateText.getText().toString();
        Date dueDate = ConvertStringToDate(dueDateString);
        return dueDate;
    }


    //==========================================
    // display current date
    public void setCurrentDateOnView() {

        tvDisplayDate = (TextView) findViewById(R.id.duedate_task);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
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
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));
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

}
