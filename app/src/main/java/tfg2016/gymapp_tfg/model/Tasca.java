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
    private Date initDate;
    private Date finalDate;
    private Boolean completada;
    private String comentari;
    private String objectId;

    public Tasca(String idClient, String titol, String descripcio, Date initDate, Date finalDate, Boolean completada, String comentari, String objectId) {
        this.setIdClient(idClient);
        this.setTitol(titol);
        this.setDescripcio(descripcio);
        this.setInitDate(initDate);
        this.setFinalDate(finalDate);
        this.setCompletada(completada);
        this.setComentari(comentari);
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

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }
    public Date getInitDate() {
        return this.initDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
    public Date getFinalDate() {
        return this.finalDate;
    }

    public void setCompletada(Boolean completada) {
        this.completada = completada;
    }
    public Boolean getCompletada() {
        return this.completada;
    }

    public void setComentari(String comentari) {
        this.comentari = comentari;
    }
    public String getComentari() {
        return this.comentari;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getObjectId() {
        return this.objectId;
    }
}
