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
    // Init: carica il profilo dal DB
    // =========================
    @Init
    @NotifyChange("profile")
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String idParam = execution.getParameter("id"); // es: modificaProfilo.zul?id=3

        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                profile = profileService.getProfileById(id);

                if (profile != null) {
                    messaggio = "Modifica del profilo ID: " + id;
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
