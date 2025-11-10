package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileService {

	// =========================
	// RECUPERA TUTTI I PROFILI per ENT CODE
	// =========================
	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<>();

		String sql = """
		        SELECT "id", "TARGET", "ENT_CODE", "ENT_VALUE", "GRUPPO", "DESCRIPTION",
		               "APP_INSTANCE_KEY", "APM", "GRANT_TYPE",
		               "GADIS_TECNOLOGIA", "GADIS_APPROVATORE", "ATS",
		               "APP_INSTANCE_KEY_1", "ENT_LIST_KEY"
		        FROM ncp.PROFILI_ABILITAZIONI
		        ORDER BY "id"
		    """;


		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Profile p = new Profile();
				p.setId(rs.getInt("id"));
				p.setTarget(rs.getString("TARGET"));
				p.setEntCode(rs.getString("ENT_CODE"));
				p.setEntValue(rs.getString("ENT_VALUE"));
				p.setGruppo(rs.getString("GRUPPO"));
				p.setDescription(rs.getString("DESCRIPTION"));
				p.setAppInstanceKey(rs.getString("APP_INSTANCE_KEY"));
				p.setApm(rs.getString("APM"));
				p.setGrantType(rs.getString("GRANT_TYPE"));
				p.setGadisTecnologia(rs.getString("GADIS_TECNOLOGIA"));
				p.setGadisApprovatore(rs.getString("GADIS_APPROVATORE"));
				p.setAts(rs.getString("ATS"));
				p.setAppInstanceKey1(rs.getString("APP_INSTANCE_KEY_1"));
				p.setEntListKey(rs.getString("ENT_LIST_KEY"));
				profiles.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return profiles;
	}

	// =========================
	// RECUPERA PROFILO PER ENT_CODE
	// =========================
	public Profile getProfileByEntCode(String entCode) {
	    if (entCode == null || entCode.isEmpty()) return null;

	    String sql = """
	        SELECT "id", "TARGET", "ENT_CODE", "ENT_VALUE", "GRUPPO", "DESCRIPTION",
	               "APP_INSTANCE_KEY", "APM", "GRANT_TYPE",
	               "GADIS_TECNOLOGIA", "GADIS_APPROVATORE", "ATS",
	               "APP_INSTANCE_KEY_1", "ENT_LIST_KEY"
	        FROM ncp.PROFILI_ABILITAZIONI
	        WHERE "ENT_CODE" = ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, entCode);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            Profile p = new Profile();
	            p.setId(rs.getInt("id"));
	            p.setTarget(rs.getString("TARGET"));
	            p.setEntCode(rs.getString("ENT_CODE"));
	            p.setEntValue(rs.getString("ENT_VALUE"));
	            p.setGruppo(rs.getString("GRUPPO"));
	            p.setDescription(rs.getString("DESCRIPTION"));
	            p.setAppInstanceKey(rs.getString("APP_INSTANCE_KEY"));
	            p.setApm(rs.getString("APM"));
	            p.setGrantType(rs.getString("GRANT_TYPE"));
	            p.setGadisTecnologia(rs.getString("GADIS_TECNOLOGIA"));
	            p.setGadisApprovatore(rs.getString("GADIS_APPROVATORE"));
	            p.setAts(rs.getString("ATS"));
	            p.setAppInstanceKey1(rs.getString("APP_INSTANCE_KEY_1"));
	            p.setEntListKey(rs.getString("ENT_LIST_KEY"));
	            return p;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}


	// =========================
	// INSERISCE NUOVO PROFILO
	// =========================
	public boolean insertProfile(Profile p) {
		if (p == null)
			return false;

		String sql = """
				    INSERT INTO ncp.PROFILI_ABILITAZIONI
				    ("TARGET","ENT_CODE","ENT_VALUE","GRUPPO","DESCRIPTION",
				     "APP_INSTANCE_KEY","APM","GRANT_TYPE",
				     "GADIS_TECNOLOGIA","GADIS_APPROVATORE","ATS",
				     "APP_INSTANCE_KEY_1","ENT_LIST_KEY")
				    VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
				    RETURNING "id"
				""";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, p.getTarget());
			stmt.setString(2, p.getEntCode());
			stmt.setString(3, p.getEntValue());
			stmt.setString(4, p.getGruppo());
			stmt.setString(5, p.getDescription());
			stmt.setString(6, p.getAppInstanceKey());
			stmt.setString(7, p.getApm());
			stmt.setString(8, p.getGrantType());
			stmt.setString(9, p.getGadisTecnologia());
			stmt.setString(10, p.getGadisApprovatore());
			stmt.setString(11, p.getAts());
			stmt.setString(12, p.getAppInstanceKey1());
			stmt.setString(13, p.getEntListKey());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				p.setId(rs.getInt("id"));
			}

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================
	// AGGIORNA PROFILO
	// =========================
	public boolean updateProfile(Profile p) {
		if (p == null || p.getId() == 0)
			return false;

		String sql = """
				    UPDATE ncp.PROFILI_ABILITAZIONI
				    SET "TARGET" = ?, "ENT_CODE" = ?, "ENT_VALUE" = ?, "GRUPPO" = ?, "DESCRIPTION" = ?,
				        "APP_INSTANCE_KEY" = ?, "APM" = ?, "GRANT_TYPE" = ?,
				        "GADIS_TECNOLOGIA" = ?, "GADIS_APPROVATORE" = ?, "ATS" = ?,
				        "APP_INSTANCE_KEY_1" = ?, "ENT_LIST_KEY" = ?
				    WHERE "id" = ?
				""";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, p.getTarget());
			stmt.setString(2, p.getEntCode());
			stmt.setString(3, p.getEntValue());
			stmt.setString(4, p.getGruppo());
			stmt.setString(5, p.getDescription());
			stmt.setString(6, p.getAppInstanceKey());
			stmt.setString(7, p.getApm());
			stmt.setString(8, p.getGrantType());
			stmt.setString(9, p.getGadisTecnologia());
			stmt.setString(10, p.getGadisApprovatore());
			stmt.setString(11, p.getAts());
			stmt.setString(12, p.getAppInstanceKey1());
			stmt.setString(13, p.getEntListKey());
			stmt.setInt(14, p.getId());

			int rows = stmt.executeUpdate();
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========================
	// ELIMINA PROFILO PER id
	// =========================
	public boolean deleteProfileById(int id) {
		if (id <= 0)
			return false;

		String sql = "DELETE FROM ncp.PROFILI_ABILITAZIONI WHERE \"id\" = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			int rows = stmt.executeUpdate();
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
