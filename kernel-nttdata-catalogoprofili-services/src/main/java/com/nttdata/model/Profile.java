package com.nttdata.model;

public class Profile {

    private String target;
    private String entCode;
    private String entValue;
    private String gruppo;
    private String description;
    private String appInstanceKey;
    private String apm;
    private String grantType;
    private String gadisTecnologia;
    private String gadisApprovatore;
    private String ats;
    private String appInstanceKey1;
    private String entListKey;

    // Costruttore vuoto
    public Profile() {}

    // Costruttore completo
    public Profile(String target, String entCode, String entValue, String gruppo,
                   String description, String appInstanceKey, String apm, String grantType,
                   String gadisTecnologia, String gadisApprovatore, String ats,
                   String appInstanceKey1, String entListKey) {
        this.target = target;
        this.entCode = entCode;
        this.entValue = entValue;
        this.gruppo = gruppo;
        this.description = description;
        this.appInstanceKey = appInstanceKey;
        this.apm = apm;
        this.grantType = grantType;
        this.gadisTecnologia = gadisTecnologia;
        this.gadisApprovatore = gadisApprovatore;
        this.ats = ats;
        this.appInstanceKey1 = appInstanceKey1;
        this.entListKey = entListKey;
    }

    // ===== GETTER E SETTER =====
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public String getEntCode() { return entCode; }
    public void setEntCode(String entCode) { this.entCode = entCode; }

    public String getEntValue() { return entValue; }
    public void setEntValue(String entValue) { this.entValue = entValue; }

    public String getGruppo() { return gruppo; }
    public void setGruppo(String gruppo) { this.gruppo = gruppo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAppInstanceKey() { return appInstanceKey; }
    public void setAppInstanceKey(String appInstanceKey) { this.appInstanceKey = appInstanceKey; }

    public String getApm() { return apm; }
    public void setApm(String apm) { this.apm = apm; }

    public String getGrantType() { return grantType; }
    public void setGrantType(String grantType) { this.grantType = grantType; }

    public String getGadisTecnologia() { return gadisTecnologia; }
    public void setGadisTecnologia(String gadisTecnologia) { this.gadisTecnologia = gadisTecnologia; }

    public String getGadisApprovatore() { return gadisApprovatore; }
    public void setGadisApprovatore(String gadisApprovatore) { this.gadisApprovatore = gadisApprovatore; }

    public String getAts() { return ats; }
    public void setAts(String ats) { this.ats = ats; }

    public String getAppInstanceKey1() { return appInstanceKey1; }
    public void setAppInstanceKey1(String appInstanceKey1) { this.appInstanceKey1 = appInstanceKey1; }

    public String getEntListKey() { return entListKey; }
    public void setEntListKey(String entListKey) { this.entListKey = entListKey; }
}
