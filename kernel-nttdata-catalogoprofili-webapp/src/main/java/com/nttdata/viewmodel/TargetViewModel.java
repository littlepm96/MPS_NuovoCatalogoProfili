package com.nttdata.viewmodel;

import com.nttdata.model.Target;
import com.nttdata.service.TargetService;
import org.zkoss.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;


public class TargetViewModel {

    // ==============================
    // SERVICES & DATA
    // ==============================
    private TargetService targetService = new TargetService(); // ✅ istanza diretta

    private List<Target> allTargets = new ArrayList<>();
    private List<Target> filteredTarget = new ArrayList<>();
    private Target selectedTarget;

    // ==============================
    // FILTRI E PAGINAZIONE
    // ==============================
    private String searchText = "";
    private String searchColumn = "Tutti";
    private int pageSize = 10;
    private int currentPage = 0;

    // ==============================
    // MESSAGGI DI STATO
    // ==============================
    private String messaggio = "";
    private String recordInfo = "";
    private String pageInfo = "";
    private boolean firstPage = true;
    private boolean lastPage = false;

    // ==============================
    // INIT
    // ==============================
    @Init
    public void init() {
        loadTargets();
        updatePagination();
    }

    // ==============================
    // METODO PRINCIPALE: CARICA TARGET
    // ==============================
    public void loadTargets() {
        allTargets = targetService.getAllTargets();
        System.out.println("Target caricati: " + allTargets.size());

        filteredTarget = new ArrayList<>(allTargets);

        if (allTargets.isEmpty()) {
            messaggio = "⚠️ Nessun record trovato nella tabella TARGET.";
        } else {
            messaggio = "Totale Target: " + allTargets.size();
        }
    }

    // ==============================
    // FILTRI DI RICERCA
    // ==============================
    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo"})
    public void filterTarget() {
        filteredTarget = new ArrayList<>();

        for (Target t : allTargets) {
            boolean match = switch (searchColumn) {
                case "NOME_TARGET" -> t.getNomeTarget() != null && t.getNomeTarget().toLowerCase().contains(searchText.toLowerCase());
                case "ACCOUNT_AUTOMATICO" -> t.getAccountAutomatico() != null && t.getAccountAutomatico().toLowerCase().contains(searchText.toLowerCase());
                case "TEMPO_ATTIVAZIONE" -> t.getTempoAttivazione() != null && t.getTempoAttivazione().toLowerCase().contains(searchText.toLowerCase());
                case "SYSTEM_OWNER" -> t.getSystemOwner() != null && t.getSystemOwner().toLowerCase().contains(searchText.toLowerCase());
                default -> t.toString().toLowerCase().contains(searchText.toLowerCase());
            };

            if (match) filteredTarget.add(t);
        }

        currentPage = 0;
        updatePagination();
    }

    @Command
    @NotifyChange({"filteredTarget", "searchText", "searchColumn", "recordInfo", "pageInfo"})
    public void clearSearch() {
        searchText = "";
        searchColumn = "Tutti";
        filteredTarget = new ArrayList<>(allTargets);
        currentPage = 0;
        updatePagination();
    }

    // ==============================
    // PAGINAZIONE
    // ==============================
    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void nextPage() {
        if ((currentPage + 1) * pageSize < filteredTarget.size()) currentPage++;
        updatePagination();
    }

    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void previousPage() {
        if (currentPage > 0) currentPage--;
        updatePagination();
    }

    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void firstPage() {
        currentPage = 0;
        updatePagination();
    }

    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void lastPage() {
        currentPage = (filteredTarget.size() - 1) / pageSize;
        updatePagination();
    }

    @Command
    @NotifyChange({"filteredTarget", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void changePageSize(@BindingParam("size") int size) {
        pageSize = size;
        currentPage = 0;
        updatePagination();
    }

    private void updatePagination() {
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, filteredTarget.size());

        List<Target> pageData = filteredTarget.subList(start, end);
        filteredTarget = new ArrayList<>(pageData);

        firstPage = currentPage == 0;
        lastPage = end >= allTargets.size();

        recordInfo = "Mostrati " + (start + 1) + " - " + end + " di " + allTargets.size() + " record";
        pageInfo = "Pagina " + (currentPage + 1) + " di " + ((allTargets.size() + pageSize - 1) / pageSize);
    }

    // ==============================
    // GETTER & SETTER
    // ==============================
    public List<Target> getFilteredTarget() {
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

    public String getRecordInfo() {
        return recordInfo;
    }

    public String getPageInfo() {
        return pageInfo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }
}
