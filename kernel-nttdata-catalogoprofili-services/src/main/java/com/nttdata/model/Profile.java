package com.nttdata.model;

public class Profile {
    private int idProfilo;
    private String nomeAbilitazione;
    private String aggregato;
    private String tipologia;
    private String descrizioneBreve;
    private String sistemaTarget;

    // Costruttore vuoto (necessario per JDBC e MVVM)
    public Profile() {}

    // Costruttore
    public Profile(int id, String nome, String agg, String tipo, String desc, String sistema) {
        this.idProfilo = id;
        this.nomeAbilitazione = nome;
        this.aggregato = agg;
        this.tipologia = tipo;
        this.descrizioneBreve = desc;
        this.sistemaTarget = sistema;
    }

    // Getter e Setter
    public int getIdProfilo() { return idProfilo; }
    public void setIdProfilo(int idProfilo) { this.idProfilo = idProfilo; }

    public String getNomeAbilitazione() { return nomeAbilitazione; }
    public void setNomeAbilitazione(String nomeAbilitazione) { this.nomeAbilitazione = nomeAbilitazione; }

    public String getAggregato() { return aggregato; }
    public void setAggregato(String aggregato) { this.aggregato = aggregato; }

    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getDescrizioneBreve() { return descrizioneBreve; }
    public void setDescrizioneBreve(String descrizioneBreve) { this.descrizioneBreve = descrizioneBreve; }

    public String getSistemaTarget() { return sistemaTarget; }
    public void setSistemaTarget(String sistemaTarget) { this.sistemaTarget = sistemaTarget; }
}

