package tfg2016.gymapp_tfg.model;

import java.io.Serializable;

/**
 * Created by Mireia on 11/04/2016.
 */
public class Entrenador  implements Serializable {

    private String name;

    private String surname;

    private String mail;

    private String especialitats;

    private String objectId;


    public Entrenador(String name, String surname, String mail, String especialitats, String objectId) {
        this.setMail(mail);
        this.setName(name);
        this.setSurname(surname);
        this.setEspecialitats(especialitats);
        this.setObjectId(objectId);
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return this.mail;
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

    public void setEspecialitats(String especialitats) {
        this.especialitats = especialitats;
    }

    public String getEspecialitats() {
        return this.especialitats;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return this.objectId;
    }


}