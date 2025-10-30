package com.nttdata.viewmodel;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import java.util.List;

public class HomeViewModel {

    private ProfileService profileService = new ProfileService();

    private List<Profile> allProfiles;               // Tutti i profili dal DB
    private ListModelList<Profile> filteredProfiles; // Profili filtrati da mostrare nella grid
    private Profile selectedProfile;                 // Profilo selezionato
    private String searchText;                       // Testo della ricerca
    private String messaggio = "Benvenuto nella gestione profili";

    // =========================
    // COSTRUTTORE: CARICA TUTTI I PROFILI
    // =========================
    public HomeViewModel() {
        loadProfiles();
    }

    // =========================
    // CARICA TUTTI I PROFILI DAL DB
    // =========================
    @Command
    @NotifyChange({"filteredProfiles", "allProfiles"})
    public void loadProfiles() {
        allProfiles = profileService.getAllProfiles();
        if (allProfiles != null) {
            filteredProfiles = new ListModelList<>(allProfiles);
        } else {
            filteredProfiles = new ListModelList<>();
        }
    }

    // =========================
    // FILTRA PROFILI IN BASE AL TESTO DI RICERCA
    // =========================
    @Command
    @NotifyChange("filteredProfiles")
    public void filterProfiles() {
        if (allProfiles == null) return;

        String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

        if (lowerSearch.isEmpty()) {
            filteredProfiles = new ListModelList<>(allProfiles);
            return;
        }

        List<Profile> result = allProfiles.stream()
            .filter(p -> (p.getTarget() != null && p.getTarget().toLowerCase().contains(lowerSearch)) ||
                         (p.getEntCode() != null && p.getEntCode().toLowerCase().contains(lowerSearch)) ||
                         (p.getEntValue() != null && p.getEntValue().toLowerCase().contains(lowerSearch)) ||
                         (p.getGruppo() != null && p.getGruppo().toLowerCase().contains(lowerSearch)) ||
                         (p.getApm() != null && p.getApm().toLowerCase().contains(lowerSearch)))
            .toList();

        filteredProfiles = new ListModelList<>(result);
    }

    // =========================
    // PULISCE IL CAMPO DI RICERCA E MOSTRA TUTTI I PROFILI
    // =========================
    @Command
    @NotifyChange("filteredProfiles")
    public void clearSearch() {
        searchText = "";
        if (allProfiles != null) {
            filteredProfiles = new ListModelList<>(allProfiles);
        } else {
            filteredProfiles = new ListModelList<>();
        }
    }

    // =========================
    // GETTER E SETTER
    // =========================
    public ListModelList<Profile> getFilteredProfiles() {
        return filteredProfiles;
    }

    public Profile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(Profile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getMessaggio() {
        return messaggio;
    }
}
