package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;

public class ModificaProfiloViewModel {

    private Profile profile;
    private ProfileService profileService = new ProfileService();
    private String messaggio = "";

    // =========================
    // Init: carica il profilo dal DB usando ENT_CODE
    // =========================
    @Init
    @NotifyChange({ "profile", "messaggio" })
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String entCode = execution.getParameter("entCode"); // es: modificaProfilo.zul?entCode=XYZ

        if (entCode != null && !entCode.isEmpty()) {
            profile = profileService.getProfileByEntCode(entCode);

            if (profile != null) {
                messaggio = "Modifica del profilo ENT_CODE: " + entCode;
            } else {
                messaggio = "Nessun profilo trovato con ENT_CODE " + entCode;
                Clients.showNotification(messaggio, "warning", null, "middle_center", 2500);
            }
        } else {
            messaggio = "Parametro ENT_CODE mancante nella URL.";
            Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
        }
    }

    // =========================
    // Salva modifiche
    // =========================
    @Command
    @NotifyChange("messaggio")
    public void saveChanges() {
        if (profile == null) {
            messaggio = "Profilo non trovato!";
            return;
        }

        try {
            boolean ok = profileService.updateProfile(profile);
            if (ok) {
                messaggio = "Profilo aggiornato con successo!";
                Clients.showNotification("Profilo aggiornato!", "info", null, "middle_center", 2000);
                Executions.sendRedirect("home.zul"); // torna alla home dopo il salvataggio
            } else {
                messaggio = "Errore durante l'aggiornamento!";
                Clients.showNotification("Errore!", "error", null, "middle_center", 2000);
            }
        } catch (Exception e) {
            messaggio = "Errore: " + e.getMessage();
            Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
            e.printStackTrace();
        }
    }

    // =========================
    // Getter / Setter
    // =========================
    public Profile getProfile() { return profile; }
    public String getMessaggio() { return messaggio; }
}
