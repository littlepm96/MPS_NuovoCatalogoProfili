package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;

import com.nttdata.model.Aggregato;
import com.nttdata.service.AggregatoService;

public class DettagliAggregatoViewModel {

    private Aggregato aggregato;
    private String messaggio = "";
    private AggregatoService aggregatoService = new AggregatoService();

    @Init
    @NotifyChange({ "aggregato", "messaggio" })
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String nomeAggregato = execution.getParameter("nomeAggregato");

        if (nomeAggregato != null && !nomeAggregato.isEmpty()) {
            aggregato = aggregatoService.getAggregatoByNome(nomeAggregato);

            if (aggregato != null) {
                messaggio = "Dettagli dell'aggregato: " + nomeAggregato;
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
    public void goBack() {
        Executions.sendRedirect("aggregati.zul");
    }

    public Aggregato getAggregato() { return aggregato; }
    public String getMessaggio() { return messaggio; }
}
