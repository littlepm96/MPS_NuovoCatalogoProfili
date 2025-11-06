package com.nttdata.viewmodel;

import com.nttdata.model.Apm;
import com.nttdata.service.ApmService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

public class DettagliApmViewModel {

    private ApmService apmService = new ApmService();
    private Apm apm;
    private String messaggio = "Dettaglio Applicazione APM";

    // =========================
    // INIT CON PARAMETRO APP
    // =========================
    @Init
    public void init(@QueryParam("app") String app) {
        if (app != null && !app.isEmpty()) {
            try {
            	apm = apmService.getApmByNome(app);

                if (apm == null) {
                    Clients.showNotification("Nessun dettaglio trovato per l'app: " + app, "warning", null, "middle_center", 3000);
                }
            } catch (Exception e) {
                Clients.showNotification("Errore durante il caricamento dei dettagli: " + e.getMessage(),
                        "error", null, "middle_center", 3000);
                e.printStackTrace();
            }
        } else {
            Clients.showNotification("Parametro APP non valido!", "error", null, "middle_center", 2000);
        }
    }

    // =========================
    // COMANDI DI NAVIGAZIONE
    // =========================
    @Command
    public void goBack() {
        Executions.sendRedirect("apm.zul");
    }

    @Command
    public void openEdit() {
        if (apm != null && apm.getApp() != null) {
            Executions.sendRedirect("modificaApm.zul?app=" + apm.getApp());
        } else {
            Clients.showNotification("Impossibile aprire la modifica: APP non valida!", "error", null, "middle_center", 2000);
        }
    }

    // =========================
    // GETTER E SETTER
    // =========================
    public Apm getApm() {
        return apm;
    }

    public void setApm(Apm apm) {
        this.apm = apm;
    }

    public String getMessaggio() {
        return messaggio;
    }
}
