package com.nttdata.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import com.nttdata.model.Profile;
import com.nttdata.model.Aggregato;

public class PopUpConfermaViewModel {

    private String message;
    private String commandName;
    private Object param;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("message") String message,
                             @ExecutionArgParam("commandName") String commandName,
                             @ExecutionArgParam("param") Object param) {
        this.message = message;
        this.commandName = commandName;
        this.param = param;
    }

    public String getMessage() {
        return message;
    }

    @Command
    public void confirm(@ContextParam(ContextType.VIEW) Window window) {
        window.detach();

        Object vmObj = Executions.getCurrent().getDesktop().getAttribute("mainVM");

        if (vmObj == null) {
            Clients.showNotification("Errore: ViewModel principale non trovato!",
                    "error", null, "middle_center", 2500);
            return;
        }

        try {
            if (vmObj instanceof ProfiliViewModel && param instanceof Profile) {
                ProfiliViewModel profiliVM = (ProfiliViewModel) vmObj;
                Profile profile = (Profile) param;

                if ("deleteProfile".equals(commandName)) {
                    profiliVM.deleteProfile(profile);
                }

            } else if (vmObj instanceof AggregatiViewModel && param instanceof Aggregato) {
                AggregatiViewModel aggregatiVM = (AggregatiViewModel) vmObj;
                Aggregato aggregato = (Aggregato) param;

                if ("deleteAggregato".equals(commandName)) {
                    aggregatiVM.deleteAggregato(aggregato);
                }

            } else {
                Clients.showNotification("Errore: Comando o parametro non riconosciuto!",
                        "error", null, "middle_center", 2500);
            }

        } catch (Exception e) {
            Clients.showNotification("Errore durante l'esecuzione del comando: " + e.getMessage(),
                    "error", null, "middle_center", 3000);
            e.printStackTrace();
        }
    }

    @Command
    public void cancel(@ContextParam(ContextType.VIEW) Window window) {
        window.detach();
    }
}
