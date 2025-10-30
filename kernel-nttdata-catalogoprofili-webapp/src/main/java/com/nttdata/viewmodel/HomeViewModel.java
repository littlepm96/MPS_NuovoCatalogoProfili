package com.nttdata.viewmodel;

import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.*;
import org.zkoss.bind.BindUtils;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;

public class HomeViewModel {

    // =========================
    // ATTRIBUTI
    // =========================
    private List<Profile> profiles;
    private List<Profile> filteredProfiles;
    private Profile selectedProfile;

    private String messaggio = "Profili caricati dal database PostgreSQL";
    private ProfileService profileService = new ProfileService();

    // Campi per la ricerca
    private String searchText = "";
    private String searchColumn = "target";

    // =========================
    // INIT
    // =========================
    @Init
    @NotifyChange({ "profiles", "filteredProfiles", "messaggio" })
    public void init() {
        loadProfiles();
    }

    // =========================
    // CARICA PROFILI
    // =========================
    @Command
    @NotifyChange({ "profiles", "filteredProfiles", "messaggio" })
    public void loadProfiles() {
        profiles = profileService.getAllProfiles();
        filteredProfiles = profiles;
        messaggio = "Elenco aggiornato: " + (profiles != null ? profiles.size() : 0) + " profili";
    }

    // =========================
    // CONFERMA ED ESEGUE AZIONE
    // =========================
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

    // =========================
    // ELIMINA PROFILO
    // =========================
    @Command
    @NotifyChange({ "profiles", "filteredProfiles", "messaggio" })
    public void deleteProfile(@BindingParam("profile") Profile profile) {
        if (profile == null || profile.getEntCode() == null) return;

        try {
            boolean ok = profileService.deleteProfile(profile.getEntCode());
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
        BindUtils.postNotifyChange(null, null, this, "filteredProfiles");
        BindUtils.postNotifyChange(null, null, this, "messaggio");
    }

    // =========================
    // APERTURA PAGINE
    // =========================
    @Command
    public void openDetails(@BindingParam("entCode") String entCode) {
        Executions.sendRedirect("dettagliProfilo.zul?entCode=" + entCode);
    }

    @Command
    public void openEdit(@BindingParam("entCode") String entCode) {
        Executions.sendRedirect("modificaProfilo.zul?entCode=" + entCode);
    }

    // =========================
    // FILTRO PROFILI
    // =========================
    @Command
    @NotifyChange("filteredProfiles")
    public void filterProfiles() {
        if (searchText == null || searchText.isEmpty()) {
            filteredProfiles = profiles;
            return;
        }

        String lowerText = searchText.toLowerCase();

        filteredProfiles = profiles.stream()
            .filter(p -> {
                switch (searchColumn) {
                    case "target": return contains(p.getTarget(), lowerText);
                    case "entCode": return contains(p.getEntCode(), lowerText);
                    case "entValue": return contains(p.getEntValue(), lowerText);
                    case "gruppo": return contains(p.getGruppo(), lowerText);
                    case "description": return contains(p.getDescription(), lowerText);
                    case "appInstanceKey": return contains(p.getAppInstanceKey(), lowerText);
                    case "apm": return contains(p.getApm(), lowerText);
                    case "grantType": return contains(p.getGrantType(), lowerText);
                    case "gadisTecnologia": return contains(p.getGadisTecnologia(), lowerText);
                    case "gadisApprovatore": return contains(p.getGadisApprovatore(), lowerText);
                    case "ats": return contains(p.getAts(), lowerText);
                    case "appInstanceKey1": return contains(p.getAppInstanceKey1(), lowerText);
                    case "entListKey": return contains(p.getEntListKey(), lowerText);
                    default: return false;
                }
            })
            .collect(Collectors.toList());
    }

    private boolean contains(String field, String text) {
        return field != null && field.toLowerCase().contains(text);
    }

    // =========================
    // GETTERS E SETTERS
    // =========================
    public List<Profile> getProfiles() { return profiles; }
    public void setProfiles(List<Profile> profiles) { this.profiles = profiles; }

    public List<Profile> getFilteredProfiles() { return filteredProfiles; }
    public void setFilteredProfiles(List<Profile> filteredProfiles) { this.filteredProfiles = filteredProfiles; }

    public Profile getSelectedProfile() { return selectedProfile; }
    public void setSelectedProfile(Profile selectedProfile) { this.selectedProfile = selectedProfile; }

    public String getMessaggio() { return messaggio; }
    public void setMessaggio(String messaggio) { this.messaggio = messaggio; }

    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }

    public String getSearchColumn() { return searchColumn; }
    public void setSearchColumn(String searchColumn) { this.searchColumn = searchColumn; }

    public ProfileService getProfileService() { return profileService; }
    public void setProfileService(ProfileService profileService) { this.profileService = profileService; }
}
