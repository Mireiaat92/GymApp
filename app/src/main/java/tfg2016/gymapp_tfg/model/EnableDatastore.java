package tfg2016.gymapp_tfg.model;


import com.parse.Parse;

/**
 * Created by Mireia on 29/03/2016.
 */
public class EnableDatastore extends android.app.Application {

    /**
     * Method onCreate. Es crida al iniciar la aplicació
     */
    public void onCreate() {
        //Inicialitzacióde la base de dates externa
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "1SDM9Lv7AxwkSgfmgR2kXWnhTBHhsBSYGiMfGkLW", "zq8RaTSdErsyFNhb0LbCQbwkkbpl3UCm1M9ly4Hk");
    }
}