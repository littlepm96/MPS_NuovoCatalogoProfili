package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;

public class DettagliProfiloViewModel {

    private Profile profile;
    private String messaggio = "";
    private ProfileService profileService = new ProfileService();

    @Init
    @NotifyChange({ "profile", "messaggio" })
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String idParam = execution.getParameter("id");

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                profile = profileService.getProfileById(id);

                if (profile != null) {
                    messaggio = "Dettagli del profilo ID: " + id;
                } else {
                    messaggio = "Nessun profilo trovato con ID " + id;
                    Clients.showNotification(messaggio, "warning", null, "middle_center", 2500);
                }
            } catch (NumberFormatException e) {
                messaggio = "ID non valido: " + idParam;
                Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
            }
        } else {
            messaggio = "Parametro ID mancante nella URL.";
            Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
        }
    }

    @Command
    public void goBack() {
        Executions.sendRedirect("home.zul");
    }

    // Getters
    public Profile getProfile() { return profile; }
    public String getMessaggio() { return messaggio; }
}
