package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import com.nttdata.model.Aggregato;
import com.nttdata.service.AggregatoService;

public class ModificaAggregatoViewModel {

    private Aggregato aggregato;
    private AggregatoService aggregatoService = new AggregatoService();
    private String messaggio = "";

    @Init
    @NotifyChange({ "aggregato", "messaggio" })
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String nomeAggregato = execution.getParameter("nomeAggregato");

        if (nomeAggregato != null && !nomeAggregato.isEmpty()) {
            aggregato = aggregatoService.getAggregatoByNome(nomeAggregato);

            if (aggregato != null) {
                messaggio = "Modifica dell'aggregato: " + nomeAggregato;
            } else {
                messaggio = "Nessun aggregato trovato con nome " + nomeAggregato;
                Clients.showNotification(messaggio, "warning", null, "middle_center", 2500);
            }
        } else {
            messaggio = "Parametro nome aggregato mancante nella URL.";
            Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
        }
    }

    @Command
    @NotifyChange("messaggio")
    public void saveChanges() {
        if (aggregato == null) {
            messaggio = "Aggregato non trovato!";
            return;
        }

        try {
            boolean ok = aggregatoService.updateAggregato(aggregato);
            if (ok) {
                messaggio = "Aggregato aggiornato con successo!";
                Clients.showNotification("Aggregato aggiornato!", "info", null, "middle_center", 2000);
                Executions.sendRedirect("aggregati.zul");
            } else {
                messaggio = "Errore durante l'aggiornamento!";
                Clients.showNotification("Errore!", "error", null, "middle_center", 2000);
            }
        } catch (Exception e) {
            messaggio = "Errore: " + e.getMessage();
            Clients.showNotification("Errore: " + e.getMessage(), "error", null, "middle_center", 3000);
            e.printStackTrace();
        }
    }

    public Aggregato getAggregato() { return aggregato; }
    public String getMessaggio() { return messaggio; }
}
