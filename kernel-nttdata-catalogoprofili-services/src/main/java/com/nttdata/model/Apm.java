package com.nttdata.model;

public class Apm {

    // Campi principali (mostrati nella tabella)
    private String app;
    private String descrizione;
    private String stato;
    private Long id_session;

    // Campi aggiuntivi (mostrati solo nei dettagli)
    private String cod_struttura_change;
    private String descr_strutt_change;
    private String resp_strutt_change;
    private String cod_strutt_business;
    private String descr_strutt_business;
    private String resp_strutt_business;
    private String dati_banca;
    private String dati_cliente;
    private String dati_personali_clienti_pf;
    private String dati_personali_dipendenti;
    private String dati_sensibili_dipendenti;
    private String dati_pep;
    private String dati_sam;

    public Apm() {}

    // Getters e Setters per i campi principali
    public String getApp() { return app; }
    public void setApp(String app) { this.app = app; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Long getIdSession() { return id_session; }
    public void setIdSession(Long id_session) { this.id_session = id_session; }

    // Getters e Setters per i campi aggiuntivi
    public String getCodStrutturaChange() { return cod_struttura_change; }
    public void setCodStrutturaChange(String cod_struttura_change) { this.cod_struttura_change = cod_struttura_change; }

    public String getDescrStruttChange() { return descr_strutt_change; }
    public void setDescrStruttChange(String descr_strutt_change) { this.descr_strutt_change = descr_strutt_change; }

    public String getRespStruttChange() { return resp_strutt_change; }
    public void setRespStruttChange(String resp_strutt_change) { this.resp_strutt_change = resp_strutt_change; }

    public String getCodStruttBusiness() { return cod_strutt_business; }
    public void setCodStruttBusiness(String cod_strutt_business) { this.cod_strutt_business = cod_strutt_business; }

    public String getDescrStruttBusiness() { return descr_strutt_business; }
    public void setDescrStruttBusiness(String descr_strutt_business) { this.descr_strutt_business = descr_strutt_business; }

    public String getRespStruttBusiness() { return resp_strutt_business; }
    public void setRespStruttBusiness(String resp_strutt_business) { this.resp_strutt_business = resp_strutt_business; }

    public String getDatiBanca() { return dati_banca; }
    public void setDatiBanca(String dati_banca) { this.dati_banca = dati_banca; }

    public String getDatiCliente() { return dati_cliente; }
    public void setDatiCliente(String dati_cliente) { this.dati_cliente = dati_cliente; }

    public String getDatiPersonaliClientiPf() { return dati_personali_clienti_pf; }
    public void setDatiPersonaliClientiPf(String dati_personali_clienti_pf) { this.dati_personali_clienti_pf = dati_personali_clienti_pf; }

    public String getDatiPersonaliDipendenti() { return dati_personali_dipendenti; }
    public void setDatiPersonaliDipendenti(String dati_personali_dipendenti) { this.dati_personali_dipendenti = dati_personali_dipendenti; }

    public String getDatiSensibiliDipendenti() { return dati_sensibili_dipendenti; }
    public void setDatiSensibiliDipendenti(String dati_sensibili_dipendenti) { this.dati_sensibili_dipendenti = dati_sensibili_dipendenti; }

    public String getDatiPep() { return dati_pep; }
    public void setDatiPep(String dati_pep) { this.dati_pep = dati_pep; }

    public String getDatiSam() { return dati_sam; }
    public void setDatiSam(String dati_sam) { this.dati_sam = dati_sam; }

    @Override
    public String toString() {
        return "Apm{" +
                "app='" + app + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", stato='" + stato + '\'' +
                ", id_session=" + id_session +
                '}';
    }
}
