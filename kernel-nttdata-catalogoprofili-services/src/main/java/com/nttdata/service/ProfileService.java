package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;

import com.nttdata.model.Profile;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

public class ProfileService {

	// =========================

	// RECUPERA TUTTI I PROFILI

	// =========================

	public List<Profile> getAllProfiles() {

		List<Profile> profiles = new ArrayList<>();

		String sql = "SELECT id_profilo, nome_abilitazione, aggregato, tipologia, descrizione_breve, sistema_target FROM profili_abilitazioni";

		try (Connection conn = DatabaseConnection.getConnection();

				PreparedStatement stmt = conn.prepareStatement(sql);

				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {

				Profile p = new Profile();

				p.setIdProfilo(rs.getInt("id_profilo"));

				p.setNomeAbilitazione(rs.getString("nome_abilitazione"));

				p.setAggregato(rs.getString("aggregato"));

				p.setTipologia(rs.getString("tipologia"));

				p.setDescrizioneBreve(rs.getString("descrizione_breve"));

				p.setSistemaTarget(rs.getString("sistema_target"));

				profiles.add(p);

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return profiles;

	}

	// =========================

	// RECUPERA PROFILO PER ID

	// =========================

	public Profile getProfileById(int id) {

		String sql = "SELECT id_profilo, nome_abilitazione, aggregato, tipologia, descrizione_breve, sistema_target "

				+ "FROM profili_abilitazioni WHERE id_profilo = ?";

		try (Connection conn = DatabaseConnection.getConnection();

				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {

				Profile p = new Profile();

				p.setIdProfilo(rs.getInt("id_profilo"));

				p.setNomeAbilitazione(rs.getString("nome_abilitazione"));

				p.setAggregato(rs.getString("aggregato"));

				p.setTipologia(rs.getString("tipologia"));

				p.setDescrizioneBreve(rs.getString("descrizione_breve"));

				p.setSistemaTarget(rs.getString("sistema_target"));

				return p;

			}

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return null; // Nessun profilo trovato

	}

	// =========================

	// ELIMINA PROFILO

	// =========================

	public boolean deleteProfile(int id) {

		String sql = "DELETE FROM profili_abilitazioni WHERE id_profilo = ?";

		try (Connection conn = DatabaseConnection.getConnection();

				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);

			int rows = stmt.executeUpdate();

			System.out.println("Righe eliminate: " + rows);

			return rows > 0;

		} catch (SQLException e) {

			e.printStackTrace();

			return false;

		}

	}

	// =========================

	// AGGIORNA PROFILO

	// =========================

	public boolean updateProfile(Profile p) {

		if (p == null)
			return false;

		String sql = "UPDATE profili_abilitazioni SET nome_abilitazione = ?, aggregato = ?, tipologia = ?, descrizione_breve = ?, sistema_target = ? "

				+ "WHERE id_profilo = ?";

		try (Connection conn = DatabaseConnection.getConnection();

				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, p.getNomeAbilitazione());

			stmt.setString(2, p.getAggregato());

			stmt.setString(3, p.getTipologia());

			stmt.setString(4, p.getDescrizioneBreve());

			stmt.setString(5, p.getSistemaTarget());

			stmt.setInt(6, p.getIdProfilo());

			int rows = stmt.executeUpdate();

			System.out.println("Righe aggiornate: " + rows);

			return rows > 0;

		} catch (SQLException e) {

			e.printStackTrace();

			return false;

		}

	}

}
