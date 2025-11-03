package com.nttdata.model;

public class Aggregato {

    // Campi principali (mostrati nella tabella)
    private String nomeAggregatoAp;
    private String tipologia;
    private String descrizione;
    private String adPersonam;
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
    public String getNomeAggregatoAp() { return nomeAggregatoAp; }
    public void setNomeAggregatoAp(String nomeAggregatoAp) { this.nomeAggregatoAp = nomeAggregatoAp; }

    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getAdPersonam() { return adPersonam; }
    public void setAdPersonam(String adPersonam) { this.adPersonam = adPersonam; }

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
                "nomeAggregatoAp='" + nomeAggregatoAp + '\'' +
                ", tipologia='" + tipologia + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", adPersonam='" + adPersonam + '\'' +
                ", ricertificabile='" + ricertificabile + '\'' +
                '}';
    }
}
