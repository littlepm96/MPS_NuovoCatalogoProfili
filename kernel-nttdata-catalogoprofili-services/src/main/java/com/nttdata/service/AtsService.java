package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Ats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtsService {

    /**
     * Restituisce la lista completa di tutti gli ATS presenti nel database.
     */
    public List<Ats> getAllAts() {
        List<Ats> atsList = new ArrayList<>();

        String sql = """
            SELECT id_ats, ats_nome, creation_date, update_date, id_session
            FROM ncp.ats
            ORDER BY id_ats
        """;

        System.out.println("🔍 Esecuzione query: " + sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Ats ats = new Ats();
                ats.setIdAts(rs.getInt("id_ats"));
                ats.setAtsNome(rs.getString("ats_nome"));
                ats.setCreationDate(rs.getDate("creation_date"));
                ats.setUpdateDate(rs.getDate("update_date"));
                
                // Gestione NULL per id_session
                Integer idSession = rs.getObject("id_session") != null ? rs.getInt("id_session") : null;
                ats.setIdSession(idSession);

                atsList.add(ats);
                count++;
                System.out.println("📦 ATS " + count + ": " + ats);
            }

            System.out.println("✅ Totale ATS caricati: " + atsList.size());

        } catch (SQLException e) {
            System.err.println("❌ ERRORE SQL durante il caricamento degli ATS:");
            System.err.println("   Messaggio: " + e.getMessage());
            e.printStackTrace();
        }

        return atsList;
    }

    /**
     * Restituisce un singolo ATS in base all'ID.
     */
    public Ats getAtsByIdAts(Integer idAts) {
        String sql = """
            SELECT id_ats, ats_nome, creation_date, update_date, id_session
            FROM ncp.ats
            WHERE id_ats = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAts);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Ats ats = new Ats();
                ats.setIdAts(rs.getInt("id_ats"));
                ats.setAtsNome(rs.getString("ats_nome"));
                ats.setCreationDate(rs.getDate("creation_date"));
                ats.setUpdateDate(rs.getDate("update_date"));
                
                Integer idSession = rs.getObject("id_session") != null ? rs.getInt("id_session") : null;
                ats.setIdSession(idSession);
                
                return ats;
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore caricamento ATS ID '" + idAts + "': " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Elimina un record ATS in base all'ID.
     */
    public boolean deleteAts(Integer idAts) {
        if (idAts == null)
            return false;

        String sql = "DELETE FROM ncp.ats WHERE id_ats = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAts);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore eliminazione ATS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiorna un record ATS esistente.
     */
    public boolean updateAts(Ats ats) {
        if (ats == null || ats.getIdAts() == null)
            return false;

        String sql = """
            UPDATE ncp.ats
            SET ats_nome = ?, 
                update_date = CURRENT_DATE, 
                id_session = ?
            WHERE id_ats = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ats.getAtsNome());
            
            if (ats.getIdSession() != null) {
                stmt.setInt(2, ats.getIdSession());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setInt(3, ats.getIdAts());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore aggiornamento ATS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserisce un nuovo ATS nel database.
     */
    public boolean insertAts(Ats ats) {
        if (ats == null || ats.getAtsNome() == null)
            return false;

        String sql = """
            INSERT INTO ncp.ats (ats_nome, creation_date, update_date, id_session)
            VALUES (?, CURRENT_DATE, CURRENT_DATE, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ats.getAtsNome());
            
            if (ats.getIdSession() != null) {
                stmt.setInt(2, ats.getIdSession());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore inserimento ATS: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
