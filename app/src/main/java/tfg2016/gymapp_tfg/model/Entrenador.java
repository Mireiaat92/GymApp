package tfg2016.gymapp_tfg.model;

/**
 * Created by Mireia on 11/04/2016.
 */
public class Entrenador {

    private String name;

    private String surname;

    private String mail;

    private String password;

    private String objectId;

    private String ID_Entrenador;

    public Entrenador(String name, String surname, String mail, /*String password,*/ String objectId) {
        this.setMail(mail);
        //this.setPassword(password);
        this.setName(name);
        this.setSurname(surname);
        this.setObjectId(objectId);

    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return this.mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return this.objectId;
    }


}