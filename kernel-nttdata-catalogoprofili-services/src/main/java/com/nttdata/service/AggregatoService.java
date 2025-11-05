package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Aggregato;
import com.nttdata.model.RegolaDettaglio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AggregatoService {

	public List<Aggregato> getAllAggregati() {
        List<Aggregato> aggregati = new ArrayList<>();

        String sql = "SELECT nome_aggregato, tipologia, descrizione, ad_personam, ricertificabile FROM ncp.aggregato";

        System.out.println("🔍 Esecuzione query: " + sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Aggregato a = new Aggregato();
                a.setNomeAggregato(rs.getString("nome_aggregato"));
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

	public boolean deleteAggregato(String nome_aggregato) {
		if (nome_aggregato == null)
			return false;

		String sql = "DELETE FROM aggregato WHERE nome_aggregato = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nome_aggregato);
			int rows = stmt.executeUpdate();
			return rows > 0;

		} catch (SQLException e) {
			System.err.println("❌ Errore eliminazione aggregato: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateAggregato(Aggregato aggregato) {
		if (aggregato == null || aggregato.getNomeAggregato() == null)
			return false;

		String sql = """
				    UPDATE ncp.aggregato
				    SET tipologia = ?, descrizione = ?, ad_personam = ?, ricertificabile = ?
				    WHERE nome_aggregato = ?
				""";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, aggregato.getTipologia());
			stmt.setString(2, aggregato.getDescrizione());
			stmt.setString(3, aggregato.getAdPersonam());
			stmt.setString(4, aggregato.getRicertificabile());
			stmt.setString(5, aggregato.getNomeAggregato());

			int rows = stmt.executeUpdate();
			return rows > 0;

		} catch (SQLException e) {
			System.err.println("❌ Errore aggiornamento aggregato: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public Aggregato getAggregatoByNome(String nomeAggregatoAp) {
        String sql = """
            SELECT nome_aggregato, tipologia, descrizione, ad_personam, ricertificabile
            FROM ncp.aggregato
            WHERE nome_aggregato = ?
        """;
 
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nomeAggregatoAp);
            ResultSet rs = stmt.executeQuery();
 
            if (rs.next()) {
                Aggregato a = new Aggregato();
                a.setNomeAggregato(rs.getString("nome_aggregato"));
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
	
	public List<RegolaDettaglio> getRegoleByAggregato(String nomeAggregato) {
	    List<RegolaDettaglio> regole = new ArrayList<>();

	    String sql = """
	        SELECT a.nome_aggregato, r.id_regola, r.rule_clob
	        FROM ncp.aggregato a
	        LEFT JOIN ncp.regole_aggregato ra ON ra.id_aggregato = a.id_aggregato
	        LEFT JOIN ncp.regola r ON r.id_regola = ra.id_regola
	        WHERE a.nome_aggregato = ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, nomeAggregato);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            String nome = rs.getString("nome_aggregato");
	            Integer idRegola = rs.getObject("id_regola") != null ? rs.getInt("id_regola") : null;
	            String ruleClob = rs.getString("rule_clob");

	            regole.add(new RegolaDettaglio(nome, idRegola, ruleClob));
	        }

	    } catch (SQLException e) {
	        System.err.println("❌ Errore caricamento regole per aggregato " + nomeAggregato);
	        e.printStackTrace();
	    }

	    return regole;
	}


}
