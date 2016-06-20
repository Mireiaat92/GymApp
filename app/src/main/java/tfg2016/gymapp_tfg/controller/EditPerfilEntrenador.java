package tfg2016.gymapp_tfg.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 18/05/2016.
 */
public class EditPerfilEntrenador extends AppCompatActivity {

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    private ParseFile file;
    public ParseFile getFile() {
        return file;
    }
    public void setFile(ParseFile file) {
        this.file = file;
    }


    private static Intent intent;

    Toolbar toolbar;

    private static final int LOAD_IMAGE = 1;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil_entrenador);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        setMyEntrenador((Entrenador) i.getSerializableExtra("myEntrenador"));

        this.initializeButtons();
        this.initializeEntrenadorData();
        initToolBar();
        //ivImage = (ImageView) findViewById(R.id.ivImage);
    }

    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit_perfil_entrenador);
        toolbar.setTitle(R.string.editPerfilEntrenador);

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
            //Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //startActivityForResult(i, LOAD_IMAGE);
            selectImage();
        }
    };


    public void initializeEntrenadorData() {
        EditText name = (EditText)findViewById(R.id.EditTextNom);
        name.setText(myEntrenador.getName());

        EditText surname = (EditText)findViewById(R.id.EditTextCognom);
        surname.setText(myEntrenador.getSurname());

        EditText especialitats = (EditText)findViewById(R.id.EditTextEspecialitats);
        especialitats.setText(myEntrenador.getEspecialitats());

        try
        {
            setFile(Complements.getDataFromBBDD(myEntrenador.getObjectId(), "ENTRENADORS", "objectId").get(0).getParseFile("Foto"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (getFile() != null) {
            ImageView imageView = (ImageView) findViewById(R.id.imageEntrenador);
            Complements.setImageViewWithParseFile(imageView, file, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_perfil_entrenador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.saveEditPerfilEntrenador) {
            Entrenador nouPerfil = CargarPerfil();

            if (nouPerfil.getName().equalsIgnoreCase("") || nouPerfil.getSurname().equalsIgnoreCase("")){
                Toast.makeText(this, (getResources().getString(R.string.nameandSurenameRequired)), Toast.LENGTH_SHORT).show();
            }
            else {
                Intent i = new Intent(this, PerfilEntrenador.class);
                i.putExtra("myEntrenador", myEntrenador);
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
     * Method CargarPerfil. Recuperamos la información de los datos del entrenador
     * @return Entrenador
     */
    public Entrenador CargarPerfil(){
        EditText TextNom =(EditText)findViewById(R.id.EditTextNom);
        String nom = TextNom.getText().toString();

        EditText TextCognom =(EditText)findViewById(R.id.EditTextCognom);
        String cognom = TextCognom.getText().toString();

        String mail = myEntrenador.getMail();
        String objectId = myEntrenador.getObjectId();


        EditText TextEspecialitats =(EditText)findViewById(R.id.EditTextEspecialitats);
        String especialitats = TextEspecialitats.getText().toString();

        //TODO Afegir els camps que falten IMATGE

        return new Entrenador(nom, cognom, mail, especialitats, objectId);
    }

    /**
     * Method EditarPerfilBDD
     * @param nouPerfil
     * @return success
     * @throws ParseException
     */
    public boolean EditarPerfilBDD(Entrenador nouPerfil) throws ParseException{
        nouPerfil.getObjectId();
        boolean success = false;
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("objectId", nouPerfil.getObjectId());
        params.put("nom", nouPerfil.getName());
        params.put("cognom", nouPerfil.getSurname());
        params.put("mail", nouPerfil.getMail());
        params.put("especialitats", nouPerfil.getEspecialitats());
        params.put("Foto", getFile());

        //TODO Afegir camps que falten IMATGE


        ParseCloud.callFunction("editPerfilEntrenadorData", params); //TODO Implementar funció a CloudCode

        return success;
    }
/*
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
            ImageView imageView = (ImageView) findViewById(R.id.imageEntrenador);
            Bitmap image = BitmapFactory.decodeFile(picturePath, options);
            imageView.setImageBitmap(image);

            //file it's a ParseFile that contains the image selected
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] dataImage = stream.toByteArray();
            setFile(new ParseFile(myEntrenador.getObjectId()+".JPEG", dataImage));
            try {
                file.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void doBack(){
        Intent i = new Intent(getApplicationContext(), PerfilEntrenador.class);
        i.putExtra("myEntrenador", myEntrenador);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed(){
        doBack();
    }


    //==============================================

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Complements.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Complements.checkPermission(EditPerfilEntrenador.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }
}
