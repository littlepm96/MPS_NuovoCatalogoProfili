package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Executions;

import com.nttdata.model.Aggregato;
import com.nttdata.service.AggregatoService;
import com.nttdata.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DettagliAggregatoViewModel {

    private Aggregato aggregato;
    private String messaggio = "";
    private List<String[]> regoleAggregato = new ArrayList<>();
    private AggregatoService aggregatoService = new AggregatoService();

    @Init
    @NotifyChange({ "aggregato", "messaggio", "regoleAggregato" })
    public void init(@ContextParam(ContextType.EXECUTION) org.zkoss.zk.ui.Execution execution) {
        String nome_aggregato = execution.getParameter("nome_aggregato");

        if (nome_aggregato != null && !nome_aggregato.isEmpty()) {
            aggregato = aggregatoService.getAggregatoByNome(nome_aggregato);

            if (aggregato != null) {
                messaggio = "Dettagli dell'aggregato: " + nome_aggregato;

                // Caricamento regole senza creare classe Regola
                regoleAggregato = new ArrayList<>();
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT r.id_regola, r.rule_clob " +
                             "FROM ncp.aggregato a " +
                             "LEFT JOIN ncp.regole_aggregato ra ON ra.id_aggregato = a.id_aggregato " +
                             "LEFT JOIN ncp.regola r ON r.id_regola = ra.id_regola " +
                             "WHERE a.nome_aggregato = ?")) {

                    stmt.setString(1, nome_aggregato);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        regoleAggregato.add(new String[]{
                                rs.getString("id_regola"),
                                rs.getString("rule_clob")
                        });
                    }

                } catch (SQLException e) {
                    Clients.showNotification("Errore caricamento regole: " + e.getMessage(),
                            "error", null, "middle_center", 3000);
                }

            } else {
                messaggio = "Nessun aggregato trovato con nome " + nome_aggregato;
                Clients.showNotification(messaggio, "warning", null, "middle_center", 2500);
            }
        } else {
            messaggio = "Parametro nome aggregato mancante nella URL.";
            Clients.showNotification(messaggio, "error", null, "middle_center", 2500);
        }
    }

    public List<String[]> getRegoleAggregato() {
        return regoleAggregato;
    }

    @Command
    public void goBack() {
        Executions.sendRedirect("aggregati.zul");
    }

    public Aggregato getAggregato() {
        return aggregato;
    }

    public String getMessaggio() {
        return messaggio;
    }
}
