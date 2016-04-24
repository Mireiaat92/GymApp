package tfg2016.gymapp_tfg.controller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

    private TextView pickDateIni;
    private DatePickerDialog pickDateDialogIni;
    private TextView pickDateFin;
    private DatePickerDialog pickDateDialogFin;
    private SimpleDateFormat dateFormatter;

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
        this.setDateTimeFieldIni();
        this.setDateTimeFieldFin();

        //setCurrentDateOnaIniciView();

    }

    /**
     * Method initializeButtons. Interfície
     */
    public void initializeButtons() {
        /*ImageButton btnChangeDate = (ImageButton) findViewById(R.id.btnChangeDate);
        btnChangeDate.setOnClickListener(clickChangeDate);

        final TextView viewIIniciDate = (TextView) findViewById(R.id.iniciDate_task);
        viewIIniciDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewIIniciDate.setOnClickListener(clickChangeIniciDate);
            }

        });

        final TextView viewFinalDate = (TextView) findViewById(R.id.finalDate_task);
        viewFinalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewFinalDate.setOnClickListener(clickChangeFinalDate);
            }

        });*/

        pickDateIni = (TextView) findViewById(R.id.iniciDate_task);
        pickDateIni.setOnClickListener(clickPickDateIni);
        pickDateIni.setInputType(InputType.TYPE_NULL);
        pickDateIni.setOnFocusChangeListener(focusPickDateIni);
        pickDateFin = (TextView) findViewById(R.id.finalDate_task);
        pickDateFin.setOnClickListener(clickPickDateFin);
        pickDateFin.setInputType(InputType.TYPE_NULL);
        pickDateFin.setOnFocusChangeListener(focusPickDateFin);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
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
                if (AddTask.this.getDescripcio().equalsIgnoreCase("")/* || AddTask.this.getDueDate().compareTo(null)*/){
                    Toast.makeText(AddTask.this, "Els camps descripció i data son obligatoris", Toast.LENGTH_SHORT).show();
                } else {
                    addTask(selectedClient.getObjectId(), AddTask.this.getTitol(), AddTask.this.getDescripcio(), AddTask.this.getInitDate(), AddTask.this.getFinalDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public ImageButton.OnClickListener clickChangeIniciDate = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

    public ImageButton.OnClickListener clickChangeFinalDate = new Button.OnClickListener() {
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
    public void addTask(String objectId, String titol, String descripcio, Date initDate, Date finalDate) throws ParseException {

        HashMap<String, Object> paramsAddTasca = new HashMap<String, Object>();
        paramsAddTasca.put("idclient", objectId);
        paramsAddTasca.put("titol", titol);
        paramsAddTasca.put("descripcio", descripcio);
        paramsAddTasca.put("initdate", initDate);
        paramsAddTasca.put("finaldate", finalDate);
        ParseCloud.callFunction("addTask", paramsAddTasca);

        Toast.makeText(AddTask.this, "Tasca afegida correctament", Toast.LENGTH_SHORT).show();

        Intent clientActivityFromEntrenador = new Intent(AddTask.this, ClientViewFromEntrenador.class);
        clientActivityFromEntrenador.putExtra("selectedClient", selectedClient);
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
    public Date getInitDate() {
        TextView initDateText = (TextView) findViewById(R.id.iniciDate_task);
        String initDateString = initDateText.getText().toString();
        Date initDate = ConvertStringToDate(initDateString);
        return initDate;
    }

    /**
     * Method getFinalDate
     *
     * @return finalDateText
     */
    public Date getFinalDate() {
        TextView finalDateText = (TextView) findViewById(R.id.finalDate_task);
        String finalDateString = finalDateText.getText().toString();
        Date finalDate = ConvertStringToDate(finalDateString);
        return finalDate;
    }


    //==========================================
    // display current date
    public void setCurrentDateOnaIniciView() {

        tvDisplayDate = (TextView) findViewById(R.id.iniciDate_task);

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
        Intent i = new Intent(getApplicationContext(), ClientViewFromEntrenador.class);
        i.putExtra("selectedClient", selectedClient);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }

    //=========================================================================================



    private void setDateTimeFieldIni() {
        Calendar newCalendar = Calendar.getInstance();
        pickDateDialogIni = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pickDateIni.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    public EditText.OnClickListener clickPickDateIni = new EditText.OnClickListener() {
        @Override
        public void onClick(View view) {
            pickDateDialogIni.show();
        }
    };

    public EditText.OnFocusChangeListener focusPickDateIni = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                pickDateDialogIni.show();
            v.clearFocus();
        }
    };

    private void setDateTimeFieldFin() {
        Calendar newCalendar = Calendar.getInstance();
        pickDateDialogFin = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pickDateFin.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public EditText.OnClickListener clickPickDateFin = new EditText.OnClickListener() {
        @Override
        public void onClick(View view) {
            pickDateDialogFin.show();
        }
    };

    public EditText.OnFocusChangeListener focusPickDateFin = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                pickDateDialogFin.show();
            v.clearFocus();
        }
    };
}
