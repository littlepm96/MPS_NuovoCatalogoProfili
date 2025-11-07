package com.nttdata.model;

import java.sql.Date;

public class Azienda {
    private int idAzienda;
    private String aziendaNome;
    private Date creationDate;
    private Date updateDate;

    // =========================
    // CAMPi PER LA RICERCA/PAGINAZIONE (non persistenti)
    // =========================
    private transient String searchText;
    private transient String searchColumn = "Tutti";

    public Azienda() {}

    public int getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(int idAzienda) {
        this.idAzienda = idAzienda;
    }

    public String getAziendaNome() {
        return aziendaNome;
    }

    public void setAziendaNome(String aziendaNome) {
        this.aziendaNome = aziendaNome;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    // =========================
    // GETTER/SETTER PER RICERCA
    // =========================
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    @Override
    public String toString() {
        return "Azienda{" +
                "idAzienda=" + idAzienda +
                ", aziendaNome='" + aziendaNome + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
