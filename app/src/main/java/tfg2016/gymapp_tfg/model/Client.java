package tfg2016.gymapp_tfg.model;

import java.io.Serializable;

public class Client implements Serializable{

    private String name;

    private String surname;

    private String mail;

    private String password;

    private String objectId;

    private String ID_Entrenador;

    public Client(String name, String surname, String mail, /*String password,*/ String objectId, String ID_Entrenador) {
        this.setMail(mail);
        //this.setPassword(password);
        this.setName(name);
        this.setSurname(surname);
        this.setObjectId(objectId);
        this.setID_Entrenador(ID_Entrenador);
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

    public void setID_Entrenador(String ID_Entrenador) {
        this.ID_Entrenador = ID_Entrenador;
    }

    public String getID_Entrenador() {
        return this.ID_Entrenador;
    }
}