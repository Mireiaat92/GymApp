package tfg2016.gymapp_tfg.controller;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 18/05/2016.
 */
public class EditPerfilClient extends AppCompatActivity {
    private Client client;
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    private ParseFile file;
    public ParseFile getFile() {
        return file;
    }
    public void setFile(ParseFile file) {
        this.file = file;
    }

    Toolbar toolbar;

    private static final int LOAD_IMAGE = 1;

    private DatePickerDialog pickDateDialog;

    private SimpleDateFormat dateFormatter;

    private int year;
    private int month;
    private int day;

    static final int DATE_DIALOG_ID = 999;
    private TextView tvDisplayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil_client);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        setClient((Client) i.getSerializableExtra("client"));

        this.initializeButtons();
        this.initializeClientData();
        initToolBar();
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_perfil_client);
        toolbar.setTitle(R.string.editPerfilClient);

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

    public void initializeButtons() {
        Button gallery = (Button)findViewById(R.id.galleryButton);
        gallery.setOnClickListener(clickGallery);
    }

    /**
     * Method Button.OnClickListener clickGallery
     */
    public Button.OnClickListener clickGallery = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, LOAD_IMAGE);
        }
    };


    public void initializeClientData() {
        EditText name = (EditText)findViewById(R.id.EditTextNom);
        name.setText(client.getName());

        EditText surname = (EditText)findViewById(R.id.EditTextCognom);
        surname.setText(client.getSurname());

        EditText weight = (EditText)findViewById(R.id.EditTextPes);
        weight.setText(client.getWeight().toString());

        EditText height = (EditText)findViewById(R.id.EditTextAlçada);
        height.setText(client.getHeight().toString());

        EditText objectiu = (EditText)findViewById(R.id.EditTextObjectiu);
        objectiu.setText(client.getObjectiu());

       try
        {
            setFile(Complements.getDataFromBBDD(client.getObjectId(), "CLIENTS", "objectId").get(0).getParseFile("Foto"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (getFile() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageClient);
            Complements.setImageViewWithParseFile(imageView, file, true);
        }

        //TODO Afegir camps restants

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_perfil_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.saveEditPerfilClient) {
            Client nouPerfil = CargarPerfil();

            if (nouPerfil.getName().equalsIgnoreCase("") || nouPerfil.getSurname().equalsIgnoreCase("")){
                Toast.makeText(this, (getResources().getString(R.string.nameandSurenameRequired)), Toast.LENGTH_SHORT).show();
            }
            else {
                Intent i = new Intent(this, PerfilClient.class);
                i.putExtra("client", client);
                try {
                    EditarPerfilBDD(nouPerfil);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startActivity(i);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method CargarPerfil. Recuperamos la información de los datos del cliente
     * @return Client
     */
    public Client CargarPerfil(){
        EditText TextNom =(EditText)findViewById(R.id.EditTextNom);
        String nom = TextNom.getText().toString();

        EditText TextCognom =(EditText)findViewById(R.id.EditTextCognom);
        String cognom = TextCognom.getText().toString();

        String mail = client.getMail();
        String objectId = client.getObjectId();
        String ID_Entrenador = client.getID_Entrenador();

        double pes;
        EditText TextPes =(EditText)findViewById(R.id.EditTextPes);
        String pesString = TextPes.getText().toString();
        if (pesString.equals("")){
            pes = 0.0;
        }
        else {
            pes = Double.parseDouble(pesString);
        }

        double altura;
        EditText TextAltura  = (EditText)findViewById(R.id.EditTextAlçada);
        String alturaString = TextAltura.getText().toString();
        if (alturaString.equals("")){
            altura = 0.0;
        }
        else {
            altura = Double.parseDouble(alturaString);
        }

        EditText TextObjectiu =(EditText)findViewById(R.id.EditTextObjectiu);
        String objectiu = TextObjectiu.getText().toString();

        return new Client(nom, cognom, mail, pes, altura, objectiu, objectId, ID_Entrenador);
    }

    /**
     * Method EditarPerfilBDD
     * @param nouPerfil
     * @return success
     * @throws ParseException
     */
    public boolean EditarPerfilBDD(Client nouPerfil) throws ParseException{
        nouPerfil.getObjectId();
        boolean success = false;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("objectId", nouPerfil.getObjectId());
        params.put("nom", nouPerfil.getName());
        params.put("cognom", nouPerfil.getSurname());
        params.put("mail", nouPerfil.getMail());
        params.put("pes", nouPerfil.getWeight());
        params.put("altura", nouPerfil.getHeight());
        params.put("objectiu", nouPerfil.getObjectiu());
        params.put("foto", getFile());

        //TODO Afegir camps que falten
        ParseCloud.callFunction("editPerfilClientData", params);

        return success;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //Try to reduce the necessary memory
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;

            // String picturePath contains the path of selected Image
            ImageView imageView = (ImageView) findViewById(R.id.imageClient);
            Bitmap image = BitmapFactory.decodeFile(picturePath, options);
            imageView.setImageBitmap(image);

            //file it's a ParseFile that contains the image selected
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] dataImage = stream.toByteArray();
            setFile(new ParseFile(client.getObjectId()+".JPEG", dataImage));
            try {
                file.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

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
        Intent i = new Intent(getApplicationContext(), PerfilClient.class);
        i.putExtra("client", client);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }

}
