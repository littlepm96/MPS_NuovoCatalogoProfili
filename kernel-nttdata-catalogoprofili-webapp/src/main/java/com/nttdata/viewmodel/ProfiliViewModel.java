package com.nttdata.viewmodel;

import com.nttdata.model.Profile;
import com.nttdata.service.ProfileService;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfiliViewModel {

	private ProfileService profileService = new ProfileService();

	private List<Profile> allProfiles; // Lista originale dal DB (NON modificare)
	private List<Profile> workingProfiles; // Lista di lavoro per i filtri
	private ListModelList<Profile> filteredProfiles; // Profili filtrati da mostrare nella grid
	private Profile selectedProfile; // Profilo selezionato
	private String searchText; // Testo della ricerca
	private String searchColumn = "Tutti"; // Colonna selezionata per la ricerca
	private String messaggio = "Benvenuto nella gestione profili";

	// Variabili per paginazione
	private int pageSize = 10; // Righe per pagina (default 10)
	private int currentPage = 0; // Pagina corrente (0-based)
	private int totalRecords = 0; // Totale record
	private int totalPages = 0; // Totale pagine

	// =========================
	// COSTRUTTORE: CARICA TUTTI I PROFILI
	// =========================
	public ProfiliViewModel() {
		loadProfiles();
	}

	// =========================
	// CARICA TUTTI I PROFILI DAL DB
	// =========================
	@Command
	@NotifyChange({ "filteredProfiles", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
	public void loadProfiles() {
		allProfiles = profileService.getAllProfiles();

		if (allProfiles != null && !allProfiles.isEmpty()) {
			workingProfiles = new ArrayList<>(allProfiles); // Copia mutabile
			totalRecords = workingProfiles.size();
			calculateTotalPages();
			updatePagedList();
		} else {
			allProfiles = new ArrayList<>();
			workingProfiles = new ArrayList<>();
			filteredProfiles = new ListModelList<>();
			totalRecords = 0;
			totalPages = 0;
		}
		currentPage = 0;
	}

	// =========================
	// AGGIORNA LA LISTA PAGINATA
	// =========================
	private void updatePagedList() {
		if (workingProfiles == null || workingProfiles.isEmpty()) {
			filteredProfiles = new ListModelList<>();
			return;
		}

		int fromIndex = currentPage * pageSize;
		int toIndex = Math.min(fromIndex + pageSize, totalRecords);

		filteredProfiles = new ListModelList<>(workingProfiles.subList(fromIndex, toIndex));
	}

	// =========================
	// CALCOLA TOTALE PAGINE
	// =========================
	private void calculateTotalPages() {
		totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		if (totalPages == 0)
			totalPages = 1;
	}

	// =========================
	// FILTRA PROFILI IN BASE AL TESTO DI RICERCA E COLONNA SELEZIONATA
	// =========================
	@Command
	@NotifyChange({ "filteredProfiles", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
	public void filterProfiles() {
		if (allProfiles == null || allProfiles.isEmpty()) {
			workingProfiles = new ArrayList<>();
			totalRecords = 0;
			currentPage = 0;
			calculateTotalPages();
			updatePagedList();
			return;
		}

		String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

		// Se il campo di ricerca è vuoto, mostra tutti i profili
		if (lowerSearch.isEmpty()) {
			workingProfiles = new ArrayList<>(allProfiles);
			totalRecords = workingProfiles.size();
			currentPage = 0;
			calculateTotalPages();
			updatePagedList();
			return;
		}

		// Filtra in base alla colonna selezionata
		List<Profile> result;

		switch (searchColumn) {
		case "Target":
			result = allProfiles.stream()
					.filter(p -> p.getTarget() != null && p.getTarget().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "Ent_code":
			result = allProfiles.stream()
					.filter(p -> p.getEntCode() != null && p.getEntCode().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "Ent_value":
			result = allProfiles.stream()
					.filter(p -> p.getEntValue() != null && p.getEntValue().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "Gruppo":
			result = allProfiles.stream()
					.filter(p -> p.getGruppo() != null && p.getGruppo().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "APM":
			result = allProfiles.stream()
					.filter(p -> p.getApm() != null && p.getApm().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		default: // "Tutti"
			result = allProfiles.stream()
					.filter(p -> (p.getTarget() != null && p.getTarget().toLowerCase().contains(lowerSearch))
							|| (p.getEntCode() != null && p.getEntCode().toLowerCase().contains(lowerSearch))
							|| (p.getEntValue() != null && p.getEntValue().toLowerCase().contains(lowerSearch))
							|| (p.getGruppo() != null && p.getGruppo().toLowerCase().contains(lowerSearch))
							|| (p.getApm() != null && p.getApm().toLowerCase().contains(lowerSearch)))
					.collect(Collectors.toList());
			break;
		}

		workingProfiles = result;
		totalRecords = result.size();
		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	// =========================
	// PULISCE IL CAMPO DI RICERCA E MOSTRA TUTTI I PROFILI
	// =========================
	@Command
	@NotifyChange({ "filteredProfiles", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage",
			"recordInfo", "pageInfo" })
	public void clearSearch() {
		searchText = "";
		searchColumn = "Tutti";

		// Ripristina la lista di lavoro dalla lista originale
		if (allProfiles != null && !allProfiles.isEmpty()) {
			workingProfiles = new ArrayList<>(allProfiles);
			totalRecords = workingProfiles.size();
		} else {
			workingProfiles = new ArrayList<>();
			totalRecords = 0;
		}

		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	// =========================
	// CAMBIA DIMENSIONE PAGINA
	// =========================
	@Command
	@NotifyChange({ "filteredProfiles", "totalPages", "currentPage", "pageSize", "recordInfo", "pageInfo" })
	public void changePageSize(@org.zkoss.bind.annotation.BindingParam("size") int size) {
		pageSize = size;
		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	// =========================
	// NAVIGAZIONE PAGINAZIONE
	// =========================
	@Command
	@NotifyChange({ "filteredProfiles", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void firstPage() {
		currentPage = 0;
		updatePagedList();
	}

	@Command
	@NotifyChange({ "filteredProfiles", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void previousPage() {
		if (currentPage > 0) {
			currentPage--;
			updatePagedList();
		}
	}

	@Command
	@NotifyChange({ "filteredProfiles", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void nextPage() {
		if (currentPage < totalPages - 1) {
			currentPage++;
			updatePagedList();
		}
	}

	@Command
	@NotifyChange({ "filteredProfiles", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void lastPage() {
		currentPage = totalPages - 1;
		updatePagedList();
	}

	// =========================
	// AZIONI SUI PROFILI
	// =========================

	/**
	 * Apre la pagina di modifica del profilo
	 */
	@Command
	public void openEdit(@BindingParam("entCode") String entCode) {
		if (entCode != null && !entCode.isEmpty()) {
			Executions.sendRedirect("modificaProfilo.zul?entCode=" + entCode);
		} else {
			Clients.showNotification("Codice ENT_CODE non valido!", "error", null, "middle_center", 2000);
		}
	}

	/**
	 * Apre la pagina dei dettagli del profilo
	 */
	@Command
	public void openDetails(@BindingParam("profile") Profile profile) {
	    if (profile != null && profile.getEntCode() != null && !profile.getEntCode().isEmpty()) {
	        Executions.sendRedirect("dettagliProfilo.zul?entCode=" + profile.getEntCode());
	    } else {
	        Clients.showNotification("Codice ENT_CODE non valido!", "error", null, "middle_center", 2000);
	    }
	}


	/**
	 * Elimina un profilo dopo conferma
	 */
	@Command
	@NotifyChange({ "filteredProfiles", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
	public void deleteProfile(@BindingParam("param") Profile profile) {
	    if (profile == null) {
	        Clients.showNotification("Profilo non valido!", "error", null, "middle_center", 2000);
	        return;
	    }

	    try {
	        boolean success = profileService.deleteProfileById(profile.getId()); // <-- usa ID

	        if (success) {
	            Clients.showNotification("Profilo eliminato con successo!", "info", null, "middle_center", 2000);
	            loadProfiles(); // ricarica i dati
	        } else {
	            Clients.showNotification("Errore durante l'eliminazione del profilo!", "error", null, "middle_center", 2500);
	        }
	    } catch (Exception e) {
	        Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
	        e.printStackTrace();
	    }
	}


	/**
	 * Mostra una finestra di conferma prima di eseguire un comando
	 */
	@Command
	public void confirmAndExecuteProfile(@BindingParam("message") String message,
			@BindingParam("commandName") String commandName, @BindingParam("param") Profile param) {

		// Imposta il riferimento al ViewModel principale nel desktop
		Executions.getCurrent().getDesktop().setAttribute("mainVM", this);

		// Crea e mostra il popup personalizzato
		org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents("/zul/popUpConferma.zul", null,
				java.util.Map.of("message", message, "commandName", commandName, "param", param));
		window.doModal();
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

	// =========================
	// STRINGHE FORMATTATE PER LA PAGINAZIONE
	// =========================
	public String getRecordInfo() {
		return getFromRecord() + " - " + getToRecord() + " di " + totalRecords + " record";
	}

	public String getPageInfo() {
		return "Pagina " + (currentPage + 1) + " di " + totalPages;
	}

	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}
}
