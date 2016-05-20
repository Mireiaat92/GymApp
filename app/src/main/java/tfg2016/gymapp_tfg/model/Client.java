package tfg2016.gymapp_tfg.model;

import java.io.Serializable;

public class Client implements Serializable{

    private String name;

    private String surname;

    private String mail;

    private Double weight;

    private Double height;

    private String objectiu;

    private String objectId;

    private String ID_Entrenador;

    public Client(String name, String surname, String mail, Double weight, Double height, String objectiu, String objectId, String ID_Entrenador) {
        this.setMail(mail);
        this.setName(name);
        this.setSurname(surname);
        this.setWeight(weight);
        this.setHeight(height);
        this.setObjectiu(objectiu);
        this.setObjectId(objectId);
        this.setID_Entrenador(ID_Entrenador);
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

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getHeight() {
        return this.height;
    }

    public void setObjectiu(String objectiu) {
        this.objectiu = objectiu;
    }

    public String getObjectiu() {
        return this.objectiu;
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