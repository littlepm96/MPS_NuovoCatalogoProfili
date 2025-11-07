package com.nttdata.model;

public class Target {

    // Chiave primaria
    private Long idTarget;

    // Campi principali
    private String nomeTarget;
    private String accountAutomatico;
    private String tempoAttivazione;
    private String note;
    private String systemOwner;

    // Chiave esterna verso la tabella SESSION
    private Long idSession;

    public Target() {}

    // Getters e Setters
    public Long getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(Long idTarget) {
        this.idTarget = idTarget;
    }

    public String getNomeTarget() {
        return nomeTarget;
    }

    public void setNomeTarget(String nomeTarget) {
        this.nomeTarget = nomeTarget;
    }

    public String getAccountAutomatico() {
        return accountAutomatico;
    }

    public void setAccountAutomatico(String accountAutomatico) {
        this.accountAutomatico = accountAutomatico;
    }

    public String getTempoAttivazione() {
        return tempoAttivazione;
    }

    public void setTempoAttivazione(String tempoAttivazione) {
        this.tempoAttivazione = tempoAttivazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSystemOwner() {
        return systemOwner;
    }

    public void setSystemOwner(String systemOwner) {
        this.systemOwner = systemOwner;
    }

    public Long getIdSession() {
        return idSession;
    }

    public void setIdSession(Long idSession) {
        this.idSession = idSession;
    }

    @Override
    public String toString() {
        return "Target{" +
                "idTarget=" + idTarget +
                ", nomeTarget='" + nomeTarget + '\'' +
                ", accountAutomatico='" + accountAutomatico + '\'' +
                ", tempoAttivazione='" + tempoAttivazione + '\'' +
                ", note='" + note + '\'' +
                ", systemOwner='" + systemOwner + '\'' +
                ", idSession=" + idSession +
                '}';
    }
}
