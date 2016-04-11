package tfg2016.gymapp_tfg.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mireia on 11/04/2016.
 */
public class Tasca implements Serializable {
    private String idClient;
    private String titol;
    private String descripcio;
    private Date dueDate;
    private String objectId;

    public Tasca(String idClient, String titol, String descripcio, Date dueDate, String objectId) {
        this.setIdClient(idClient);
        this.setTitol(titol);
        this.setDescripcio(descripcio);
        this.setDueDate(dueDate);
        this.setObjectId(objectId);
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
    public String getIdClient() {
        return this.idClient;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }
    public String getTitol() {
        return this.titol;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
    public String getDescripcio() {
        return this.descripcio;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public Date getDueDate() {
        return this.dueDate;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getObjectId() {
        return this.objectId;
    }
}
