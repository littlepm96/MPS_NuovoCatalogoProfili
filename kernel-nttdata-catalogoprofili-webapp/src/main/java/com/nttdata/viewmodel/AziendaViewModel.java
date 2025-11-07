package com.nttdata.viewmodel;

import com.nttdata.model.Azienda;
import com.nttdata.service.AziendaService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AziendaViewModel {

    private AziendaService aziendaService = new AziendaService();

    private List<Azienda> allAziende;       // Lista originale dal DB
    private List<Azienda> workingAziende;   // Lista di lavoro per filtri
    private ListModelList<Azienda> filteredAziende; // Lista filtrata per grid
    private String searchText;              // Testo ricerca
    private String searchColumn = "Tutti";  // Colonna selezionata per la ricerca

    // Paginazione
    private int pageSize = 10;
    private int currentPage = 0;
    private int totalRecords = 0;
    private int totalPages = 0;

    private String messaggio = "Benvenuto nella gestione aziende";

    @Init
    public void init() {
        loadAziende();
    }

    // =========================
    // CARICA TUTTE LE AZIENDE
    // =========================
    @Command
    @NotifyChange({ "filteredAziende", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
    public void loadAziende() {
        allAziende = aziendaService.getAllAziende();

        if (allAziende != null && !allAziende.isEmpty()) {
            workingAziende = new ArrayList<>(allAziende);
            totalRecords = workingAziende.size();
            calculateTotalPages();
            updatePagedList();
        } else {
            allAziende = new ArrayList<>();
            workingAziende = new ArrayList<>();
            filteredAziende = new ListModelList<>();
            totalRecords = 0;
            totalPages = 0;
        }

        currentPage = 0;
    }

    // =========================
    // FILTRO RICERCA
    // =========================
    @Command
    @NotifyChange({ "filteredAziende", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
    public void filterAziende() {
        if (allAziende == null || allAziende.isEmpty()) {
            workingAziende = new ArrayList<>();
            totalRecords = 0;
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

        if (lowerSearch.isEmpty()) {
            workingAziende = new ArrayList<>(allAziende);
        } else {
            switch (searchColumn) {
                case "ID":
                    workingAziende = allAziende.stream()
                            .filter(a -> String.valueOf(a.getIdAzienda()).contains(lowerSearch))
                            .collect(Collectors.toList());
                    break;
                case "Nome Azienda":
                    workingAziende = allAziende.stream()
                            .filter(a -> a.getAziendaNome() != null && a.getAziendaNome().toLowerCase().contains(lowerSearch))
                            .collect(Collectors.toList());
                    break;
                default: // "Tutti"
                    workingAziende = allAziende.stream()
                            .filter(a -> (String.valueOf(a.getIdAzienda()).contains(lowerSearch)) ||
                                         (a.getAziendaNome() != null && a.getAziendaNome().toLowerCase().contains(lowerSearch)))
                            .collect(Collectors.toList());
                    break;
            }
        }

        totalRecords = workingAziende.size();
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PULISCI FILTRO
    // =========================
    @Command
    @NotifyChange({ "filteredAziende", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage",
                    "recordInfo", "pageInfo" })
    public void clearSearch() {
        searchText = "";
        searchColumn = "Tutti";

        if (allAziende != null && !allAziende.isEmpty()) {
            workingAziende = new ArrayList<>(allAziende);
            totalRecords = workingAziende.size();
        } else {
            workingAziende = new ArrayList<>();
            totalRecords = 0;
        }

        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PAGINAZIONE
    // =========================
    private void updatePagedList() {
        if (workingAziende == null || workingAziende.isEmpty()) {
            filteredAziende = new ListModelList<>();
            return;
        }

        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        filteredAziende = new ListModelList<>(workingAziende.subList(fromIndex, toIndex));
    }

    private void calculateTotalPages() {
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;
    }

    @Command
    @NotifyChange({ "filteredAziende", "totalPages", "currentPage", "pageSize", "recordInfo", "pageInfo" })
    public void changePageSize(@org.zkoss.bind.annotation.BindingParam("size") int size) {
        pageSize = size;
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    @Command
    @NotifyChange({ "filteredAziende", "currentPage", "recordInfo", "pageInfo" })
    public void firstPage() {
        currentPage = 0;
        updatePagedList();
    }

    @Command
    @NotifyChange({ "filteredAziende", "currentPage", "recordInfo", "pageInfo" })
    public void previousPage() {
        if (currentPage > 0) currentPage--;
        updatePagedList();
    }

    @Command
    @NotifyChange({ "filteredAziende", "currentPage", "recordInfo", "pageInfo" })
    public void nextPage() {
        if (currentPage < totalPages - 1) currentPage++;
        updatePagedList();
    }

    @Command
    @NotifyChange({ "filteredAziende", "currentPage", "recordInfo", "pageInfo" })
    public void lastPage() {
        currentPage = totalPages - 1;
        updatePagedList();
    }

    // =========================
    // GETTER
    // =========================
    public ListModelList<Azienda> getFilteredAziende() {
        return filteredAziende;
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
