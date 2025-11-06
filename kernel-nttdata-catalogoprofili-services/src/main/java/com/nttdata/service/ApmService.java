package com.nttdata.service;

import com.nttdata.config.DatabaseConnection;
import com.nttdata.model.Apm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApmService {

    /**
     * Restituisce la lista completa di tutte le APM presenti nel database.
     */
    public List<Apm> getAllApm() {
        List<Apm> apmList = new ArrayList<>();

        String sql = """
            SELECT app, descrizione, stato, id_session,
                   cod_struttura_change, descr_strutt_change, resp_strutt_change,
                   cod_strutt_business, descr_strutt_business, resp_strutt_business,
                   dati_banca, dati_cliente, dati_personali_clienti_pf, dati_personali_dipendenti,
                   dati_sensibili_dipendenti, dati_pep, dati_sam
            FROM ncp.apm
        """;

        System.out.println("🔍 Esecuzione query: " + sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Apm apm = new Apm();
                apm.setApp(rs.getString("app"));
                apm.setDescrizione(rs.getString("descrizione"));
                apm.setStato(rs.getString("stato"));
                apm.setIdSession(rs.getLong("id_session"));
                apm.setCodStrutturaChange(rs.getString("cod_struttura_change"));
                apm.setDescrStruttChange(rs.getString("descr_strutt_change"));
                apm.setRespStruttChange(rs.getString("resp_strutt_change"));
                apm.setCodStruttBusiness(rs.getString("cod_strutt_business"));
                apm.setDescrStruttBusiness(rs.getString("descr_strutt_business"));
                apm.setRespStruttBusiness(rs.getString("resp_strutt_business"));
                apm.setDatiBanca(rs.getString("dati_banca"));
                apm.setDatiCliente(rs.getString("dati_cliente"));
                apm.setDatiPersonaliClientiPf(rs.getString("dati_personali_clienti_pf"));
                apm.setDatiPersonaliDipendenti(rs.getString("dati_personali_dipendenti"));
                apm.setDatiSensibiliDipendenti(rs.getString("dati_sensibili_dipendenti"));
                apm.setDatiPep(rs.getString("dati_pep"));
                apm.setDatiSam(rs.getString("dati_sam"));

                apmList.add(apm);
                count++;
                System.out.println("📦 APM " + count + ": " + apm);
            }

            System.out.println("✅ Totale APM caricate: " + apmList.size());

        } catch (SQLException e) {
            System.err.println("❌ ERRORE SQL durante il caricamento delle APM:");
            System.err.println("   Messaggio: " + e.getMessage());
            e.printStackTrace();
        }

        return apmList;
    }

    /**
     * Elimina un record APM in base al nome dell'app.
     */
    public boolean deleteApm(String app) {
        if (app == null)
            return false;

        String sql = "DELETE FROM ncp.apm WHERE app = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, app);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore eliminazione APM: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aggiorna un record APM esistente.
     */
    public boolean updateApm(Apm apm) {
        if (apm == null || apm.getApp() == null)
            return false;

        String sql = """
            UPDATE ncp.apm
            SET descrizione = ?, stato = ?, id_session = ?,
                cod_struttura_change = ?, descr_strutt_change = ?, resp_strutt_change = ?,
                cod_strutt_business = ?, descr_strutt_business = ?, resp_strutt_business = ?,
                dati_banca = ?, dati_cliente = ?, dati_personali_clienti_pf = ?, dati_personali_dipendenti = ?,
                dati_sensibili_dipendenti = ?, dati_pep = ?, dati_sam = ?
            WHERE app = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apm.getDescrizione());
            stmt.setString(2, apm.getStato());
            stmt.setLong(3, apm.getIdSession());
            stmt.setString(4, apm.getCodStrutturaChange());
            stmt.setString(5, apm.getDescrStruttChange());
            stmt.setString(6, apm.getRespStruttChange());
            stmt.setString(7, apm.getCodStruttBusiness());
            stmt.setString(8, apm.getDescrStruttBusiness());
            stmt.setString(9, apm.getRespStruttBusiness());
            stmt.setString(10, apm.getDatiBanca());
            stmt.setString(11, apm.getDatiCliente());
            stmt.setString(12, apm.getDatiPersonaliClientiPf());
            stmt.setString(13, apm.getDatiPersonaliDipendenti());
            stmt.setString(14, apm.getDatiSensibiliDipendenti());
            stmt.setString(15, apm.getDatiPep());
            stmt.setString(16, apm.getDatiSam());
            stmt.setString(17, apm.getApp());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Errore aggiornamento APM: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Restituisce una singola APM in base al nome dell'app.
     */
    public Apm getApmByNome(String appName) {
        String sql = """
            SELECT app, descrizione, stato, id_session,
                   cod_struttura_change, descr_strutt_change, resp_strutt_change,
                   cod_strutt_business, descr_strutt_business, resp_strutt_business,
                   dati_banca, dati_cliente, dati_personali_clienti_pf, dati_personali_dipendenti,
                   dati_sensibili_dipendenti, dati_pep, dati_sam
            FROM ncp.apm
            WHERE app = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Apm apm = new Apm();
                apm.setApp(rs.getString("app"));
                apm.setDescrizione(rs.getString("descrizione"));
                apm.setStato(rs.getString("stato"));
                apm.setIdSession(rs.getLong("id_session"));
                apm.setCodStrutturaChange(rs.getString("cod_struttura_change"));
                apm.setDescrStruttChange(rs.getString("descr_strutt_change"));
                apm.setRespStruttChange(rs.getString("resp_strutt_change"));
                apm.setCodStruttBusiness(rs.getString("cod_strutt_business"));
                apm.setDescrStruttBusiness(rs.getString("descr_strutt_business"));
                apm.setRespStruttBusiness(rs.getString("resp_strutt_business"));
                apm.setDatiBanca(rs.getString("dati_banca"));
                apm.setDatiCliente(rs.getString("dati_cliente"));
                apm.setDatiPersonaliClientiPf(rs.getString("dati_personali_clienti_pf"));
                apm.setDatiPersonaliDipendenti(rs.getString("dati_personali_dipendenti"));
                apm.setDatiSensibiliDipendenti(rs.getString("dati_sensibili_dipendenti"));
                apm.setDatiPep(rs.getString("dati_pep"));
                apm.setDatiSam(rs.getString("dati_sam"));
                return apm;
            }

        } catch (SQLException e) {
            System.err.println("❌ Errore caricamento APM '" + appName + "': " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
