package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Azienda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AziendaService {

    public List<Azienda> getAllAziende() {
        List<Azienda> aziende = new ArrayList<>();
        String query = "SELECT id_azienda, azienda_nome, creation_date, update_date FROM ncp.azienda";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Azienda a = new Azienda();
                a.setIdAzienda(rs.getInt("id_azienda"));
                a.setAziendaNome(rs.getString("azienda_nome"));
                a.setCreationDate(rs.getDate("creation_date"));
                a.setUpdateDate(rs.getDate("update_date"));
                aziende.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aziende;
    }
}
