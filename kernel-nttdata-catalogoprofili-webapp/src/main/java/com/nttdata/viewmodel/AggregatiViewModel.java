package com.nttdata.viewmodel;

import com.nttdata.model.Aggregato;
import com.nttdata.model.RegolaDettaglio;
import com.nttdata.service.AggregatoService;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AggregatiViewModel {

	private AggregatoService aggregatoService = new AggregatoService();

	private List<Aggregato> allAggregati;
	private List<Aggregato> workingAggregati;
	private ListModelList<Aggregato> filteredAggregati;
	private List<RegolaDettaglio> selectedRegole = new ArrayList<>();
	private Aggregato selectedAggregato;
	private String searchText;
	private String searchColumn = "Tutti";
	private String messaggio = "Gestione Aggregati";

	private int pageSize = 10;
	private int currentPage = 0;
	private int totalRecords = 0;
	private int totalPages = 0;

	@Init
	public void init() {
		System.out.println("🚀 Inizializzazione AggregatiViewModel...");
		loadAggregati();
	}

	@Command
	@NotifyChange({ "filteredAggregati", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo",
			"messaggio" })
	public void loadAggregati() {
		System.out.println("📂 Caricamento aggregati dal database...");

		try {
			allAggregati = aggregatoService.getAllAggregati();

			System.out.println("✅ Aggregati caricati: " + (allAggregati != null ? allAggregati.size() : 0));

			if (allAggregati != null && !allAggregati.isEmpty()) {
				workingAggregati = new ArrayList<>(allAggregati);
				totalRecords = workingAggregati.size();
				calculateTotalPages();
				updatePagedList();
				messaggio = "Trovati " + totalRecords + " aggregati";
			} else {
				allAggregati = new ArrayList<>();
				workingAggregati = new ArrayList<>();
				filteredAggregati = new ListModelList<>();
				totalRecords = 0;
				totalPages = 0;
				messaggio = "Nessun aggregato trovato nel database";
				System.out.println("⚠️ ATTENZIONE: Nessun aggregato trovato!");
			}
			currentPage = 0;

		} catch (Exception e) {
			System.err.println("❌ ERRORE durante il caricamento degli aggregati:");
			e.printStackTrace();
			messaggio = "Errore: " + e.getMessage();
			allAggregati = new ArrayList<>();
			workingAggregati = new ArrayList<>();
			filteredAggregati = new ListModelList<>();
		}
	}

	private void updatePagedList() {
		if (workingAggregati == null || workingAggregati.isEmpty()) {
			filteredAggregati = new ListModelList<>();
			return;
		}

		int fromIndex = currentPage * pageSize;
		int toIndex = Math.min(fromIndex + pageSize, totalRecords);

		filteredAggregati = new ListModelList<>(workingAggregati.subList(fromIndex, toIndex));
		System.out.println("📄 Pagina " + (currentPage + 1) + ": visualizzati " + filteredAggregati.size() + " record");
	}

	private void calculateTotalPages() {
		totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		if (totalPages == 0)
			totalPages = 1;
	}
	
	@Command
	@NotifyChange("selectedRegole")
	public void loadRegole(@BindingParam("nome_aggregato") String nomeAggregato) {
	    if (nomeAggregato == null || nomeAggregato.isEmpty()) {
	        selectedRegole.clear();
	        return;
	    }

	    selectedRegole = aggregatoService.getRegoleByAggregato(nomeAggregato);
	    System.out.println("📄 Regole caricate per " + nomeAggregato + ": " + selectedRegole.size());
	}


	@Command
	@NotifyChange({ "filteredAggregati", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
	public void filterAggregati() {
		if (allAggregati == null || allAggregati.isEmpty()) {
			workingAggregati = new ArrayList<>();
			totalRecords = 0;
			currentPage = 0;
			calculateTotalPages();
			updatePagedList();
			return;
		}

		String lowerSearch = (searchText != null) ? searchText.toLowerCase().trim() : "";

		if (lowerSearch.isEmpty()) {
			workingAggregati = new ArrayList<>(allAggregati);
			totalRecords = workingAggregati.size();
			currentPage = 0;
			calculateTotalPages();
			updatePagedList();
			return;
		}

		List<Aggregato> result;

		switch (searchColumn) {
		case "Nome":
			result = allAggregati.stream().filter(
					a -> a.getNomeAggregato() != null && a.getNomeAggregato().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "Tipologia":
			result = allAggregati.stream()
					.filter(a -> a.getTipologia() != null && a.getTipologia().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		case "Descrizione":
			result = allAggregati.stream()
					.filter(a -> a.getDescrizione() != null && a.getDescrizione().toLowerCase().contains(lowerSearch))
					.collect(Collectors.toList());
			break;
		default:
			result = allAggregati.stream().filter(
					a -> (a.getNomeAggregato() != null && a.getNomeAggregato().toLowerCase().contains(lowerSearch))
							|| (a.getTipologia() != null && a.getTipologia().toLowerCase().contains(lowerSearch))
							|| (a.getDescrizione() != null && a.getDescrizione().toLowerCase().contains(lowerSearch)))
					.collect(Collectors.toList());
			break;
		}

		workingAggregati = result;
		totalRecords = result.size();
		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	@Command
	@NotifyChange({ "filteredAggregati", "searchText", "searchColumn", "totalRecords", "totalPages", "currentPage",
			"recordInfo", "pageInfo" })
	public void clearSearch() {
		searchText = "";
		searchColumn = "Tutti";

		if (allAggregati != null && !allAggregati.isEmpty()) {
			workingAggregati = new ArrayList<>(allAggregati);
			totalRecords = workingAggregati.size();
		} else {
			workingAggregati = new ArrayList<>();
			totalRecords = 0;
		}

		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	@Command
	@NotifyChange({ "filteredAggregati", "totalPages", "currentPage", "pageSize", "recordInfo", "pageInfo" })
	public void changePageSize(@BindingParam("size") int size) {
		pageSize = size;
		currentPage = 0;
		calculateTotalPages();
		updatePagedList();
	}

	@Command
	@NotifyChange({ "filteredAggregati", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void firstPage() {
		currentPage = 0;
		updatePagedList();
	}

	@Command
	@NotifyChange({ "filteredAggregati", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void previousPage() {
		if (currentPage > 0) {
			currentPage--;
			updatePagedList();
		}
	}

	@Command
	@NotifyChange({ "filteredAggregati", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void nextPage() {
		if (currentPage < totalPages - 1) {
			currentPage++;
			updatePagedList();
		}
	}

	@Command
	@NotifyChange({ "filteredAggregati", "currentPage", "recordInfo", "pageInfo", "firstPage", "lastPage" })
	public void lastPage() {
		currentPage = totalPages - 1;
		updatePagedList();
	}

	@Command
	public void openEdit(@BindingParam("nome_aggregato") String nome_aggregato) {
		if (nome_aggregato != null && !nome_aggregato.isEmpty()) {
			Executions.sendRedirect("modificaAggregato.zul?nome_aggregato=" + nome_aggregato);
		} else {
			Clients.showNotification("Nome aggregato non valido!", "error", null, "middle_center", 2000);
		}
	}

	@Command
	public void openDetails(@BindingParam("nome_aggregato") String nome_aggregato) {
		if (nome_aggregato != null && !nome_aggregato.isEmpty()) {
			Executions.sendRedirect("dettagliAggregato.zul?nome_aggregato=" + nome_aggregato);
		} else {
			Clients.showNotification("Nome aggregato non valido!", "error", null, "middle_center", 2000);
		}
	}

	@Command
	@NotifyChange({ "filteredAggregati", "totalRecords", "totalPages", "currentPage", "recordInfo", "pageInfo" })
	public void deleteAggregato(@BindingParam("param") Aggregato aggregato) {
		if (aggregato == null || aggregato.getIdAggregato() == null) {
			Clients.showNotification("Aggregato non valido!", "error", null, "middle_center", 2000);
			return;
		}

		try {
			System.out.println("🗑️ Tentativo di eliminazione aggregato ID: " + aggregato.getIdAggregato());
			boolean success = aggregatoService.deleteAggregato(aggregato.getIdAggregato());

			if (success) {
				Clients.showNotification("Aggregato eliminato con successo!", "info", null, "middle_center", 2000);
				loadAggregati();
			} else {
				Clients.showNotification("Errore durante l'eliminazione!", "error", null, "middle_center", 2500);
			}
		} catch (Exception e) {
			Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
			e.printStackTrace();
		}
	}

	@Command
	public void confirmAndExecuteAggregati(@BindingParam("message") String message,
			@BindingParam("commandName") String commandName, @BindingParam("param") Aggregato param) {

		System.out.println(">>> Custom confirmAndExecute in AggregatiViewModel <<<");

		Executions.getCurrent().getDesktop().setAttribute("mainVM", this);

		org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents("/zul/popUpConferma.zul", null,
				java.util.Map.of("message", message, "commandName", commandName, "param", param));
		window.doModal();
	}

	// Getters e Setters
	public ListModelList<Aggregato> getFilteredAggregati() {
		System.out.println("🔍 getFilteredAggregati() chiamato. Size: "
				+ (filteredAggregati != null ? filteredAggregati.size() : 0));
		return filteredAggregati;
	}

	public Aggregato getSelectedAggregato() {
		return selectedAggregato;
	}

	public void setSelectedAggregato(Aggregato selectedAggregato) {
		this.selectedAggregato = selectedAggregato;
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
	public List<RegolaDettaglio> getSelectedRegole() {
	    return selectedRegole;
	}
}
