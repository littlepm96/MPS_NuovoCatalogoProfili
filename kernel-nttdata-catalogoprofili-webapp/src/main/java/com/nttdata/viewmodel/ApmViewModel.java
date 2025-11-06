package com.nttdata.viewmodel;

import com.nttdata.model.Apm;
import com.nttdata.service.ApmService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApmViewModel {

    private ApmService apmService = new ApmService();

    private List<Apm> allApmList; // Lista originale dal DB
    private List<Apm> workingApmList; // Lista filtrabile
    private ListModelList<Apm> filteredApm; // Lista visibile in griglia
    private Apm selectedApm; // Elemento selezionato
    private String searchText;
    private String searchColumn = "Tutti";
    private String messaggio = "Gestione Applicazioni APM";

    // Variabili per paginazione
    private int pageSize = 10;
    private int currentPage = 0;
    private int totalRecords = 0;
    private int totalPages = 0;

    // =========================
    // COSTRUTTORE
    // =========================
    public ApmViewModel() {
        loadApmList();
    }

    // =========================
    // CARICA DATI DAL SERVICE
    // =========================
    @Command
    @NotifyChange({"filteredApm", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void loadApmList() {
        allApmList = apmService.getAllApm();

        if (allApmList != null && !allApmList.isEmpty()) {
            workingApmList = new ArrayList<>(allApmList);
            totalRecords = workingApmList.size();
            calculateTotalPages();
            updatePagedList();
        } else {
            allApmList = new ArrayList<>();
            workingApmList = new ArrayList<>();
            filteredApm = new ListModelList<>();
            totalRecords = 0;
            totalPages = 0;
        }
        currentPage = 0;
    }

    // =========================
    // PAGINAZIONE
    // =========================
    private void updatePagedList() {
        if (workingApmList == null || workingApmList.isEmpty()) {
            filteredApm = new ListModelList<>();
            return;
        }

        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);

        filteredApm = new ListModelList<>(workingApmList.subList(fromIndex, toIndex));
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
    @NotifyChange({"filteredApm", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void filterApm() {
        if (allApmList == null || allApmList.isEmpty()) {
            workingApmList = new ArrayList<>();
            totalRecords = 0;
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

        if (lowerSearch.isEmpty()) {
            workingApmList = new ArrayList<>(allApmList);
            totalRecords = workingApmList.size();
            currentPage = 0;
            calculateTotalPages();
            updatePagedList();
            return;
        }

        List<Apm> result;

        switch (searchColumn) {
            case "APP":
                result = allApmList.stream()
                        .filter(a -> a.getApp() != null && a.getApp().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "DESCRIZIONE":
                result = allApmList.stream()
                        .filter(a -> a.getDescrizione() != null && a.getDescrizione().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "STATO":
                result = allApmList.stream()
                        .filter(a -> a.getStato() != null && a.getStato().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            case "COD_STRUTTURA_CHANGE":
                result = allApmList.stream()
                        .filter(a -> a.getCodStrutturaChange() != null && a.getCodStrutturaChange().toLowerCase().contains(lowerSearch))
                        .collect(Collectors.toList());
                break;
            default:
                result = allApmList.stream()
                        .filter(a ->
                                (a.getApp() != null && a.getApp().toLowerCase().contains(lowerSearch)) ||
                                (a.getDescrizione() != null && a.getDescrizione().toLowerCase().contains(lowerSearch)) ||
                                (a.getStato() != null && a.getStato().toLowerCase().contains(lowerSearch)) ||
                                (a.getCodStrutturaChange() != null && a.getCodStrutturaChange().toLowerCase().contains(lowerSearch))
                        )
                        .collect(Collectors.toList());
                break;
        }

        workingApmList = result;
        totalRecords = result.size();
        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PULISCI FILTRO
    // =========================
    @Command
    @NotifyChange({"filteredApm", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void clearSearch() {
        searchText = "";
        searchColumn = "Tutti";

        if (allApmList != null && !allApmList.isEmpty()) {
            workingApmList = new ArrayList<>(allApmList);
            totalRecords = workingApmList.size();
        } else {
            workingApmList = new ArrayList<>();
            totalRecords = 0;
        }

        currentPage = 0;
        calculateTotalPages();
        updatePagedList();
    }

    // =========================
    // PAGINAZIONE COMANDI
    // =========================
    @Command
    @NotifyChange({"filteredApm", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void firstPage() {
        currentPage = 0;
        updatePagedList();
    }

    @Command
    @NotifyChange({"filteredApm", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({"filteredApm", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updatePagedList();
        }
    }

    @Command
    @NotifyChange({"filteredApm", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage"})
    public void lastPage() {
        currentPage = totalPages - 1;
        updatePagedList();
    }

    // =========================
    // AZIONI APM
    // =========================
    @Command
    public void openEdit(@BindingParam("app") String app) {
        if (app != null && !app.isEmpty()) {
            Executions.sendRedirect("modificaApm.zul?app=" + app);
        } else {
            Clients.showNotification("Campo APP non valido!", "error", null, "middle_center", 2000);
        }
    }

    @Command
    public void openDetails(@BindingParam("app") String app) {
        if (app != null && !app.isEmpty()) {
            Executions.sendRedirect("dettagliApm.zul?app=" + app);
        } else {
            Clients.showNotification("Campo APP non valido!", "error", null, "middle_center", 2000);
        }
    }

    @Command
    @NotifyChange({"filteredApm", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo"})
    public void deleteApm(@BindingParam("param") Apm apm) {
        if (apm == null || apm.getApp() == null) {
            Clients.showNotification("Applicazione non valida!", "error", null, "middle_center", 2000);
            return;
        }

        try {
            boolean success = apmService.deleteApm(apm.getApp());

            if (success) {
                Clients.showNotification("Applicazione eliminata con successo!", "info", null, "middle_center", 2000);
                loadApmList();
            } else {
                Clients.showNotification("Errore durante l'eliminazione!", "error", null, "middle_center", 2500);
            }
        } catch (Exception e) {
            Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
            e.printStackTrace();
        }
    }

    @Command
    public void confirmAndExecuteApm(@BindingParam("message") String message,
                                     @BindingParam("commandName") String commandName,
                                     @BindingParam("param") Apm param) {
        Executions.getCurrent().getDesktop().setAttribute("mainVM", this);
        org.zkoss.zul.Window window =
                (org.zkoss.zul.Window) Executions.createComponents(
                        "/zul/popUpConferma.zul", null,
                        java.util.Map.of("message", message, "commandName", commandName, "param", param)
                );
        window.doModal();
    }

    // =========================
    // GETTER E SETTER
    // =========================
    public ListModelList<Apm> getFilteredApm() {
        return filteredApm;
    }

    public Apm getSelectedApm() {
        return selectedApm;
    }

    public void setSelectedApm(Apm selectedApm) {
        this.selectedApm = selectedApm;
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

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
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
