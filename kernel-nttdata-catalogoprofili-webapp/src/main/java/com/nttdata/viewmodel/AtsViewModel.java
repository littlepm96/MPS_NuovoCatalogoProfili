package com.nttdata.viewmodel;

import com.nttdata.model.Ats;
import com.nttdata.service.AtsService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AtsViewModel {

    private AtsService atsService = new AtsService();

    private List<Ats> allAtsList;
    private List<Ats> workingAtsList;
    private ListModelList<Ats> filteredAts;
    private Ats selectedAts;
    private String searchText;
    private String searchColumn = "Tutti";
    private String messaggio = "Gestione ATS";

    // Variabili per paginazione
    private int pageSize = 10;
    private int currentPage = 0;
    private int totalRecords = 0;
    private int totalPages = 0;

    // =========================
    // COSTRUTTORE
    // =========================
    public AtsViewModel() {
        loadAtsList();
    }

    // =========================
    // CARICA DATI DAL SERVICE
    // =========================
    @Command
    @NotifyChange({"filteredAts", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void loadAtsList() {
        allAtsList = atsService.getAllAts();

        if (allAtsList != null && !allAtsList.isEmpty()) {
            workingAtsList = new ArrayList<>(allAtsList);
            totalRecords = workingAtsList.size();
            calculateTotalPages();
            updatePagedList();
        } else {
            allAtsList = new ArrayList<>();
            workingAtsList = new ArrayList<>();
            filteredAts = new ListModelList<>();
            totalRecords = 0;
            totalPages = 0;
        }
        currentPage = 0;
    }

    // =========================
    // PAGINAZIONE
    // =========================
    private void updatePagedList() {
        if (workingAtsList == null || workingAtsList.isEmpty()) {
            filteredAts = new ListModelList<>();
            return;
        }

        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        filteredAts = new ListModelList<>(workingAtsList.subList(fromIndex, toIndex));
    }

    private void calculateTotalPages() {
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0)
            totalPages = 1;
    }

    // =========================
    // FILTRO DI RICERCA
    // =========================
    @Command
    @NotifyChange({"filteredAts", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void filterAts() {
        if (allAtsList == null || allAtsList.isEmpty()) {
            workingAtsList = new ArrayList<>();
            totalRecords = 0;
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

        if (lowerSearch.isEmpty()) {
            workingAtsList = new ArrayList<>(allAtsList);
            totalRecords = workingAtsList.size();
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        List<Ats> result;

        switch (searchColumn) {
            case "ID_ATS":
                result = allAtsList.stream()
                        .filter(a -> a.getIdAts() != null && a.getIdAts().toString().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "ATS_NOME":
                result = allAtsList.stream()
                        .filter(a -> a.getAtsNome() != null && a.getAtsNome().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "ID_SESSION":
                result = allAtsList.stream()
                        .filter(a -> a.getIdSession() != null && a.getIdSession().toString().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            default:
                result = allAtsList.stream()
                        .filter(a ->
                                (a.getIdAts() != null && a.getIdAts().toString().contains(lowerSearch)) ||
                                (a.getAtsNome() != null && a.getAtsNome().toLowerCase().contains(lowerSearch)) ||
                                (a.getIdSession() != null && a.getIdSession().toString().contains(lowerSearch))
                        )
                        .collect(Collectors.toList());
                break;
        }

        workingAtsList = result;
        totalRecords = result.size();
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PULISCI FILTRO
    // =========================
    @Command
    @NotifyChange({"filteredAts", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void clearSearch() {
        searchText = "";
        searchColumn = "Tutti";

        if (allAtsList != null && !allAtsList.isEmpty()) {
            workingAtsList = new ArrayList<>(allAtsList);
            totalRecords = workingAtsList.size();
        } else {
            workingAtsList = new ArrayList<>();
            totalRecords = 0;
        }

        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // CAMBIO DIMENSIONE PAGINA
    // =========================
    @Command
    @NotifyChange({"filteredAts", "totalPages", "currentPage", "pageSize", "recordInfo", "pageInfo"})
    public void changePageSize(@BindingParam("size") int size) {
        pageSize = size;
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PAGINAZIONE COMANDI
    // =========================
    @Command
    @NotifyChange({"filteredAts", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void firstPage() {
        currentPage = 0;
        updatePagedList();
    }

    @Command
    @NotifyChange({"filteredAts", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({"filteredAts", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({"filteredAts", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void lastPage() {
        currentPage = totalPages - 1;
        updatePagedList();
    }

    // =========================
    // AZIONI ATS
    // =========================
    @Command
    public void openEdit(@BindingParam("idAts") Integer idAts) {
        if (idAts != null) {
            Executions.sendRedirect("modificaAts.zul?idAts=" + idAts);
        } else {
            Clients.showNotification("ID ATS non valido!", "error", null, "middle_center", 2000);
        }
    }

    @Command
    public void openDetails(@BindingParam("idAts") Integer idAts) {
        if (idAts != null) {
            Executions.sendRedirect("dettagliAts.zul?idAts=" + idAts);
        } else {
            Clients.showNotification("ID ATS non valido!", "error", null, "middle_center", 2000);
        }
    }

    @Command
    @NotifyChange({"filteredAts", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void deleteAts(@BindingParam("param") Ats ats) {
        if (ats == null || ats.getIdAts() == null) {
            Clients.showNotification("ATS non valido!", "error", null, "middle_center", 2000);
            return;
        }

        try {
            boolean success = atsService.deleteAts(ats.getIdAts());

            if (success) {
                Clients.showNotification("ATS eliminato con successo!", "info", null, "middle_center", 2000);
                loadAtsList();
            } else {
                Clients.showNotification("Errore durante l'eliminazione!", "error", null, "middle_center", 2500);
            }
        } catch (Exception e) {
            Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
            e.printStackTrace();
        }
    }

    // =========================
    // GETTER E SETTER
    // =========================
    public ListModelList<Ats> getFilteredAts() { return filteredAts; }
    public Ats getSelectedAts() { return selectedAts; }
    public void setSelectedAts(Ats selectedAts) { this.selectedAts = selectedAts; }
    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }
    public String getMessaggio() { return messaggio; }
    public String getSearchColumn() { return searchColumn; }
    public void setSearchColumn(String searchColumn) { this.searchColumn = searchColumn; }
    public int getPageSize() { return pageSize; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalRecords() { return totalRecords; }
    public int getTotalPages() { return totalPages; }
    public int getFromRecord() { return totalRecords == 0 ? 0 : (currentPage * pageSize) + 1; }
    public int getToRecord() { return Math.min((currentPage + 1) * pageSize, totalRecords); }
    public boolean isFirstPage() { return currentPage == 0; }
    public boolean isLastPage() { return currentPage >= totalPages - 1; }
    public String getRecordInfo() { return getFromRecord() + " - " + getToRecord() + " di " + totalRecords + " record"; }
    public String getPageInfo() { return "Pagina " + (currentPage + 1) + " di " + totalPages; }
}
