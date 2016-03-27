package tfg2016.gymapp_tfg.resources;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encrypt {

    Context mContext;

    /**
     * Constructor Encrypt
     * @param context
     */
    public Encrypt(Context context){
        this.mContext = context;
    }

    /**
     * Method encryptPassword
     * @param password
     * @return sha1
     */
    public String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    /**
     * Method byteToHex
     * @param hash
     * @return result
     */
    public String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}