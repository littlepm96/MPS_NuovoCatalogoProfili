package com.nttdata.model;

public class Aggregato {

    // Campi principali (mostrati nella tabella)
    private String nome_aggregato;
    private String tipologia;
    private String descrizione;
    private String ad_personam;
    private String ricertificabile;
    
    // Campi aggiuntivi (mostrati solo nei dettagli)
    private String gruppo;
    private String responsabile;
    private String dataCreazione;
    private String dataUltimaModifica;
    private String stato;
    private String note;

    public Aggregato() {}

    // Getters e Setters per i campi principali
    public String getNomeAggregato() { return nome_aggregato; }
    public void setNomeAggregato(String nome_aggregato) { this.nome_aggregato = nome_aggregato; }

    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getAdPersonam() { return ad_personam; }
    public void setAdPersonam(String ad_personam) { this.ad_personam = ad_personam; }

    public String getRicertificabile() { return ricertificabile; }
    public void setRicertificabile(String ricertificabile) { this.ricertificabile = ricertificabile; }

    // Getters e Setters per i campi aggiuntivi
    public String getGruppo() { return gruppo; }
    public void setGruppo(String gruppo) { this.gruppo = gruppo; }

    public String getResponsabile() { return responsabile; }
    public void setResponsabile(String responsabile) { this.responsabile = responsabile; }

    public String getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(String dataCreazione) { this.dataCreazione = dataCreazione; }

    public String getDataUltimaModifica() { return dataUltimaModifica; }
    public void setDataUltimaModifica(String dataUltimaModifica) { this.dataUltimaModifica = dataUltimaModifica; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "Aggregato{" +
                "nome_aggregato='" + nome_aggregato + '\'' +
                ", tipologia='" + tipologia + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", ad_personam='" + ad_personam + '\'' +
                ", ricertificabile='" + ricertificabile + '\'' +
                '}';
    }
}
