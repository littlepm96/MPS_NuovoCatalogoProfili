package com.nttdata.viewmodel;

import com.nttdata.model.Azienda;
import com.nttdata.service.AziendaService;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import java.util.List;

public class AziendaViewModel {

    private List<Azienda> aziende;
    private AziendaService aziendaService = new AziendaService();

    public List<Azienda> getAziende() {
        return aziende;
    }

    @Init
    public void init() {
        loadAziende();
    }

    @Command
    @NotifyChange("aziende")
    public void loadAziende() {
        aziende = aziendaService.getAllAziende();
    }
}
