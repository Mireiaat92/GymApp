package tfg2016.gymapp_tfg.resources;

import android.content.Context;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {

    Context mContext;

    /**
     * Constructor Encrypt
     * @param context
     */
    public Encrypt(Context context){
        this.mContext = context;
    }

        public String encryptPassword (String password) {
            MessageDigest objSHA = null;
            try {
                objSHA = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] bytSHA = objSHA.digest(password.getBytes());
            BigInteger intNumber = new BigInteger(1, bytSHA);
            String strHashCode = intNumber.toString(16);

            // pad with 0 if the hexa digits are less then 64.
            while (strHashCode.length() < 64) {
                strHashCode = "0" + strHashCode;
            }
            return strHashCode;
        }

}