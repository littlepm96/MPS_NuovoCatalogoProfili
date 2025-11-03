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
        String entCode = execution.getParameter("entCode"); // parametro ENT_CODE

        if (entCode != null && !entCode.isEmpty()) {
            profile = profileService.getProfileByEntCode(entCode);

            if (profile != null) {
                messaggio = "Dettagli del profilo ENT_CODE: " + entCode;
            } else {
                messaggio = "Nessun profilo trovato con ENT_CODE " + entCode;
                Clients.showNotification(messaggio, "warning", null, "middle_center", 2500);
            }
        } else {
            messaggio = "Parametro ENT_CODE mancante nella URL.";
            Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
        }
    }

    @Command
    public void goBack() {
        Executions.sendRedirect("profili.zul");
    }

    // Getters
    public Profile getProfile() { return profile; }
    public String getMessaggio() { return messaggio; }
}
