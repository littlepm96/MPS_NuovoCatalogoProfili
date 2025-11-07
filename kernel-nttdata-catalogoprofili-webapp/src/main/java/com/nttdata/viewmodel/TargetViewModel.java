package com.nttdata.viewmodel;

import com.nttdata.model.Target;
import com.nttdata.service.TargetService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TargetViewModel {

    private TargetService targetService = new TargetService();

    private List<Target> allTargets; // Lista originale dal DB
    private List<Target> workingTargets; // Lista filtrata di lavoro
    private ListModelList<Target> filteredTarget; // Lista visibile nella grid

    private Target selectedTarget;

    private String searchText = "";
    private String searchColumn = "Tutti";

    private String messaggio = "Benvenuto nella gestione Target";

    // Paginazione
    private int pageSize = 10;
    private int currentPage = 0;
    private int totalRecords = 0;
    private int totalPages = 0;

    // =========================
    // INIT
    // =========================
    @Init
    public void init() {
        loadTargets();
    }

    // =========================
    // CARICA TUTTI I TARGET DAL DB
    // =========================
    @Command
    @NotifyChange({ "filteredTarget", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
    public void loadTargets() {
        allTargets = targetService.getAllTargets();

        if (allTargets != null && !allTargets.isEmpty()) {
            workingTargets = new ArrayList<>(allTargets);
            totalRecords = workingTargets.size();
            calculateTotalPages();
            updatePagedList();
        } else {
            allTargets = new ArrayList<>();
            workingTargets = new ArrayList<>();
            filteredTarget = new ListModelList<>();
            totalRecords = 0;
            totalPages = 0;
        }
        currentPage = 0;
    }

    // =========================
    // AGGIORNA LA LISTA PAGINATA
    // =========================
    private void updatePagedList() {
        if (workingTargets == null || workingTargets.isEmpty()) {
            filteredTarget = new ListModelList<>();
            return;
        }

        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        filteredTarget = new ListModelList<>(workingTargets.subList(fromIndex, toIndex));
    }

    // =========================
    // CALCOLA TOTALE PAGINE
    // =========================
    private void calculateTotalPages() {
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;
    }

    // =========================
    // FILTRA TARGET IN BASE A TESTO E COLONNA
    // =========================
    @Command
    @NotifyChange({ "filteredTarget", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
    public void filterTarget() {
        if (allTargets == null || allTargets.isEmpty()) {
            workingTargets = new ArrayList<>();
            totalRecords = 0;
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

        if (lowerSearch.isEmpty()) {
            workingTargets = new ArrayList<>(allTargets);
            totalRecords = workingTargets.size();
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        List<Target> result;
        switch (searchColumn) {
            case "NOME_TARGET":
                result = allTargets.stream()
                        .filter(t -> t.getNomeTarget() != null && t.getNomeTarget().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "ACCOUNT_AUTOMATICO":
                result = allTargets.stream()
                        .filter(t -> t.getAccountAutomatico() != null && t.getAccountAutomatico().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "TEMPO_ATTIVAZIONE":
                result = allTargets.stream()
                        .filter(t -> t.getTempoAttivazione() != null && t.getTempoAttivazione().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "SYSTEM_OWNER":
                result = allTargets.stream()
                        .filter(t -> t.getSystemOwner() != null && t.getSystemOwner().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            default: // "Tutti"
                result = allTargets.stream()
                        .filter(t -> (t.getNomeTarget() != null && t.getNomeTarget().toLowerCase().contains(lowerSearch))
                                || (t.getAccountAutomatico() != null && t.getAccountAutomatico().toLowerCase().contains(lowerSearch))
                                || (t.getTempoAttivazione() != null && t.getTempoAttivazione().toLowerCase().contains(lowerSearch))
                                || (t.getSystemOwner() != null && t.getSystemOwner().toLowerCase().contains(lowerSearch)))
                        .collect(Collectors.toList());
                break;
        }

        workingTargets = result;
        totalRecords = result.size();
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PULISCE LA RICERCA
    // =========================
    @Command
    @NotifyChange({ "filteredTarget", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
    public void clearSearch() {
        searchText = "";
        searchColumn = "Tutti";
        workingTargets = new ArrayList<>(allTargets);
        totalRecords = workingTargets.size();
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // CAMBIA DIMENSIONE PAGINA
    // =========================
    @Command
    @NotifyChange({ "filteredTarget", "totalPages", "currentPage", "pageSize", "recordInfo", "pageInfo" })
    public void changePageSize(@BindingParam("size") int size) {
        pageSize = size;
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // NAVIGAZIONE
    // =========================
    @Command
    @NotifyChange({ "filteredTarget", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
    public void firstPage() {
        currentPage = 0;
        updatePagedList();
    }

    @Command
    @NotifyChange({ "filteredTarget", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({ "filteredTarget", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({ "filteredTarget", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
    public void lastPage() {
        currentPage = totalPages - 1;
        updatePagedList();
    }

    // =========================
    // AZIONI SU TARGET
    // =========================
    @Command
    public void openEdit(@BindingParam("nomeTarget") String nomeTarget) {
        if (nomeTarget != null && !nomeTarget.isEmpty()) {
            Executions.sendRedirect("modificaTarget.zul?nomeTarget=" + nomeTarget);
        } else {
            Clients.showNotification("Nome Target non valido!", "error", null, "middle_center", 2000);
        }
    }

    @Command
    public void openDetails(@BindingParam("nomeTarget") String nomeTarget) {
        if (nomeTarget != null && !nomeTarget.isEmpty()) {
            Executions.sendRedirect("dettagliTarget.zul?nomeTarget=" + nomeTarget);
        } else {
            Clients.showNotification("Nome Target non valido!", "error", null, "middle_center", 2000);
        }
    }


    // =========================
    // GETTER E SETTER
    // =========================
    public ListModelList<Target> getFilteredTarget() {
        return filteredTarget;
    }

    public Target getSelectedTarget() {
        return selectedTarget;
    }

    public void setSelectedTarget(Target selectedTarget) {
        this.selectedTarget = selectedTarget;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getFromRecord() {
        return totalRecords == 0 ? 0 : (currentPage * pageSize) + 1;
    }

    public int getToRecord() {
        return Math.min((currentPage + 1) * pageSize, totalRecords);
    }

    public boolean isFirstPage() {
        return currentPage == 0;
    }

    public boolean isLastPage() {
        return currentPage >= totalPages - 1;
    }

    public String getRecordInfo() {
        return getFromRecord() + " - " + getToRecord() + " di " + totalRecords + " record";
    }

    public String getPageInfo() {
        return "Pagina " + (currentPage + 1) + " di " + totalPages;
    }
}
