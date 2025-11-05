/*
 * package com.nttdata.viewmodel;
 * 
 * import org.zkoss.bind.annotation.*; import org.zkoss.zk.ui.util.Clients;
 * import org.zkoss.zk.ui.Executions;
 * 
 * import com.nttdata.model.Aggregato; import
 * com.nttdata.service.AggregatoService;
 * 
 * public class DettagliAggregatoViewModel {
 * 
 * private Aggregato aggregato; private String messaggio = ""; private
 * AggregatoService aggregatoService = new AggregatoService();
 * 
 * @Init
 * 
 * @NotifyChange({ "aggregato", "messaggio" }) public void
 * init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution
 * execution) { String nome_aggregato = execution.getParameter("nome_aggregato");
 * 
 * if (nome_aggregato != null && !nome_aggregato.isEmpty()) { aggregato =
 * aggregatoService.getAggregatoByNome(nome_aggregato);
 * 
 * if (aggregato != null) { messaggio = "Dettagli dell'aggregato: " +
 * nome_aggregato; } else { messaggio = "Nessun aggregato trovato con nome " +
 * nome_aggregato; Clients.showNotification(messaggio, "warning", null,
 * "middle_center", 2500); } } else { messaggio =
 * "Parametro nome aggregato mancante nella URL.";
 * Clients.showNotification(messaggio, "error", null, "middle_center", 2500); }
 * }
 * 
 * @Command public void goBack() { Executions.sendRedirect("aggregati.zul"); }
 * 
 * public Aggregato getAggregato() { return aggregato; } public String
 * getMessaggio() { return messaggio; } }
 */