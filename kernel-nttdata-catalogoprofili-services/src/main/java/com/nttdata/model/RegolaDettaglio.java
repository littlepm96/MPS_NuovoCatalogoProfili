package com.nttdata.model;

public class RegolaDettaglio {

    private String nomeAggregato;
    private Integer idRegola;
    private String ruleClob;

    public RegolaDettaglio() {}

    public RegolaDettaglio(String nomeAggregato, Integer idRegola, String ruleClob) {
        this.nomeAggregato = nomeAggregato;
        this.idRegola = idRegola;
        this.ruleClob = ruleClob;
    }

    public String getNomeAggregato() {
        return nomeAggregato;
    }

    public void setNomeAggregato(String nomeAggregato) {
        this.nomeAggregato = nomeAggregato;
    }

    public Integer getIdRegola() {
        return idRegola;
    }

    public void setIdRegola(Integer idRegola) {
        this.idRegola = idRegola;
    }

    public String getRuleClob() {
        return ruleClob;
    }

    public void setRuleClob(String ruleClob) {
        this.ruleClob = ruleClob;
    }
}
