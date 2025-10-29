package com.nttdata.viewmodel;

import java.util.List;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;

public class HomeViewModel {

    private List<Profile> profiles;
    private Profile selectedProfile;
    private String messaggio = "Profili caricati dal database PostgreSQL";
    private ProfileService profileService = new ProfileService();

    @Init
    @NotifyChange({ "profiles", "messaggio" })
    public void init() {
        loadProfiles();
    }

    @Command
    @NotifyChange({ "profiles", "messaggio" })
    public void loadProfiles() {
        profiles = profileService.getAllProfiles();
        messaggio = "Elenco aggiornato: " + (profiles != null ? profiles.size() : 0) + " profili";
    }

    @Command
    public void confirmAndExecute(
            @BindingParam("message") String message,
            @BindingParam("commandName") String commandName,
            @BindingParam("param") Object param) {

        Messagebox.show(message, "Conferma azione",
            new Messagebox.Button[] { Messagebox.Button.YES, Messagebox.Button.NO },
            Messagebox.QUESTION,
            event -> {
                if (Messagebox.ON_YES.equals(event.getName())) {
                    if ("deleteProfile".equals(commandName)) {
                        deleteProfile((Profile) param);
                    }
                }
            });
    }

    @Command
    @NotifyChange({ "profiles", "messaggio" })
    public void deleteProfile(@BindingParam("profile") Profile profile) {
        if (profile == null) return;

        try {
            boolean ok = profileService.deleteProfile(profile.getIdProfilo());
            if (ok) {
                loadProfiles();
      
                Clients.showNotification("Profilo eliminato con successo.", "info", null, "middle_center", 2000);
            } else {
               
                Clients.showNotification("Errore: impossibile eliminare il profilo.", "error", null, "middle_center", 3000);
            }
        } catch (Exception e) {
            messaggio = "Errore: " + e.getMessage();
            e.printStackTrace();
            Clients.showNotification("Si è verificato un errore: " + e.getMessage(), "error", null, "middle_center", 3000);
        }

        BindUtils.postNotifyChange(null, null, this, "profiles");
        BindUtils.postNotifyChange(null, null, this, "messaggio");
    }

    
    @Command
    public void openDetails(@BindingParam("id") int idProfilo) {
        Executions.sendRedirect("dettagliProfilo.zul?id=" + idProfilo);
    }
    
    
    @Command
    public void openEdit(@BindingParam("id") int idProfilo) {
        org.zkoss.zk.ui.Executions.sendRedirect("modificaProfilo.zul?id=" + idProfilo);
    }


    // Getters/Setters
    public List<Profile> getProfiles() { return profiles; }
    public Profile getSelectedProfile() { return selectedProfile; }
    public void setSelectedProfile(Profile selectedProfile) { this.selectedProfile = selectedProfile; }
    public String getMessaggio() { return messaggio; }
}
