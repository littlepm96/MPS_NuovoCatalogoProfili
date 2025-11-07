package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Target;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TargetService {

    // =========================
    // RECUPERA TUTTI I TARGET
    // =========================
    public List<Target> getAllTargets() {
        List<Target> targets = new ArrayList<>();

        String sql = """
            SELECT "ID_TARGET", "NOME_TARGET", "ACCOUNT_AUTOMATICO", "TEMPO_ATTIVAZIONE",
                   "NOTE", "SYSTEM_OWNER", "ID_SESSION"
            FROM TARGET
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Target t = new Target();
                t.setIdTarget(rs.getLong("ID_TARGET"));
                t.setNomeTarget(rs.getString("NOME_TARGET"));
                t.setAccountAutomatico(rs.getString("ACCOUNT_AUTOMATICO"));
                t.setTempoAttivazione(rs.getString("TEMPO_ATTIVAZIONE"));
                t.setNote(rs.getString("NOTE"));
                t.setSystemOwner(rs.getString("SYSTEM_OWNER"));
                t.setIdSession(rs.getLong("ID_SESSION"));
                targets.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return targets;
    }

    // =========================
    // RECUPERA TARGET PER ID
    // =========================
    public Target getTargetById(Long idTarget) {
        String sql = """
            SELECT "ID_TARGET", "NOME_TARGET", "ACCOUNT_AUTOMATICO", "TEMPO_ATTIVAZIONE",
                   "NOTE", "SYSTEM_OWNER", "ID_SESSION"
            FROM TARGET
            WHERE "ID_TARGET" = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idTarget);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Target t = new Target();
                t.setIdTarget(rs.getLong("ID_TARGET"));
                t.setNomeTarget(rs.getString("NOME_TARGET"));
                t.setAccountAutomatico(rs.getString("ACCOUNT_AUTOMATICO"));
                t.setTempoAttivazione(rs.getString("TEMPO_ATTIVAZIONE"));
                t.setNote(rs.getString("NOTE"));
                t.setSystemOwner(rs.getString("SYSTEM_OWNER"));
                t.setIdSession(rs.getLong("ID_SESSION"));
                return t;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // INSERISCE NUOVO TARGET
    // =========================
    public boolean insertTarget(Target t) {
        if (t == null) return false;

        String sql = """
            INSERT INTO TARGET
            ("ID_TARGET", "NOME_TARGET", "ACCOUNT_AUTOMATICO", "TEMPO_ATTIVAZIONE",
             "NOTE", "SYSTEM_OWNER", "ID_SESSION")
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, t.getIdTarget());
            stmt.setString(2, t.getNomeTarget());
            stmt.setString(3, t.getAccountAutomatico());
            stmt.setString(4, t.getTempoAttivazione());
            stmt.setString(5, t.getNote());
            stmt.setString(6, t.getSystemOwner());
            stmt.setLong(7, t.getIdSession());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // AGGIORNA TARGET
    // =========================
    public boolean updateTarget(Target t) {
        if (t == null || t.getIdTarget() == null) return false;

        String sql = """
            UPDATE TARGET
            SET "NOME_TARGET" = ?, "ACCOUNT_AUTOMATICO" = ?, "TEMPO_ATTIVAZIONE" = ?,
                "NOTE" = ?, "SYSTEM_OWNER" = ?, "ID_SESSION" = ?
            WHERE "ID_TARGET" = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, t.getNomeTarget());
            stmt.setString(2, t.getAccountAutomatico());
            stmt.setString(3, t.getTempoAttivazione());
            stmt.setString(4, t.getNote());
            stmt.setString(5, t.getSystemOwner());
            stmt.setLong(6, t.getIdSession());
            stmt.setLong(7, t.getIdTarget());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // ELIMINA TARGET
    // =========================
    public boolean deleteTarget(Long idTarget) {
        if (idTarget == null) return false;

        String sql = "DELETE FROM TARGET WHERE \"ID_TARGET\" = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idTarget);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
