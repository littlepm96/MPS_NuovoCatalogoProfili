package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Aggregato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AggregatoService {

    public List<Aggregato> getAllAggregati() {
        List<Aggregato> aggregati = new ArrayList<>();

        String sql = """
            SELECT nome_aggregato_ap, tipologia, descrizione, ad_personam, ricertificabile
            FROM aggregato
        """;

        System.out.println("🔍 Esecuzione query: " + sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Aggregato a = new Aggregato();
                a.setNomeAggregatoAp(rs.getString("nome_aggregato_ap"));
                a.setTipologia(rs.getString("tipologia"));
                a.setDescrizione(rs.getString("descrizione"));
                a.setAdPersonam(rs.getString("ad_personam"));
                a.setRicertificabile(rs.getString("ricertificabile"));
                aggregati.add(a);
                count++;
                System.out.println("📦 Aggregato " + count + ": " + a);
            }

            System.out.println("✅ Totale aggregati caricati: " + aggregati.size());

        } catch (SQLException e) {
            System.err.println("❌ ERRORE SQL durante il caricamento degli aggregati:");
            System.err.println("   Messaggio: " + e.getMessage());
            e.printStackTrace();
        }

        return aggregati;
    }

    public Aggregato getAggregatoByNome(String nomeAggregatoAp) {
        String sql = """
            SELECT nome_aggregato_ap, tipologia, descrizione, ad_personam, ricertificabile
            FROM aggregato
            WHERE nome_aggregato_ap = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeAggregatoAp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Aggregato a = new Aggregato();
                a.setNomeAggregatoAp(rs.getString("nome_aggregato_ap"));
                a.setTipologia(rs.getString("tipologia"));
                a.setDescrizione(rs.getString("descrizione"));
                a.setAdPersonam(rs.getString("ad_personam"));
                a.setRicertificabile(rs.getString("ricertificabile"));
                return a;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteAggregato(String nomeAggregatoAp) {
        if (nomeAggregatoAp == null) return false;

        String sql = "DELETE FROM aggregato WHERE nome_aggregato_ap = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeAggregatoAp);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore eliminazione aggregato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateAggregato(Aggregato aggregato) {
        if (aggregato == null || aggregato.getNomeAggregatoAp() == null) return false;

        String sql = """
            UPDATE aggregato
            SET tipologia = ?, descrizione = ?, ad_personam = ?, ricertificabile = ?
            WHERE nome_aggregato_ap = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aggregato.getTipologia());
            stmt.setString(2, aggregato.getDescrizione());
            stmt.setString(3, aggregato.getAdPersonam());
            stmt.setString(4, aggregato.getRicertificabile());
            stmt.setString(5, aggregato.getNomeAggregatoAp());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore aggiornamento aggregato: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
