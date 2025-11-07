package com.nttdata.model;

public class Ats {

    private Integer idAts;            // ID_ATS (PK, SERIAL)
    private String atsNome;           // ATS_NOME (VARCHAR2)
    private java.sql.Date creationDate;    // CREATION_DATE (DATE)
    private java.sql.Date updateDate;      // UPDATE_DATE (DATE)
    private Integer idSession;        // ID_SESSION (NUMBER, FK)

    public Ats() {}

    // Getters e Setters
    public Integer getIdAts() { return idAts; }
    public void setIdAts(Integer idAts) { this.idAts = idAts; }

    public String getAtsNome() { return atsNome; }
    public void setAtsNome(String atsNome) { this.atsNome = atsNome; }

    public java.sql.Date getCreationDate() { return creationDate; }
    public void setCreationDate(java.sql.Date creationDate) { this.creationDate = creationDate; }

    public java.sql.Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(java.sql.Date updateDate) { this.updateDate = updateDate; }

    public Integer getIdSession() { return idSession; }
    public void setIdSession(Integer idSession) { this.idSession = idSession; }

    @Override
    public String toString() {
        return "Ats{" +
                "idAts=" + idAts +
                ", atsNome='" + atsNome + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", idSession=" + idSession +
                '}';
    }
}
