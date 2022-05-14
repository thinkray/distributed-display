package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.ProfileManager;
import org.apache.commons.text.StringEscapeUtils;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIServerController {
    private ServerDashboardController serverDashboardController;
    private String address;
    private Integer port;
    private Boolean isRunning;
    private Registry rmiRegistry;

    public RMIServerController(ServerDashboardController serverDashboardController) {
        this.serverDashboardController = serverDashboardController;
        this.isRunning = false;
    }

    public Boolean startServer(String address, Integer port) {
        try {
            rmiRegistry = LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://" + address + ":" + port + "/DisplayServer", new RMIServerWorkerController(this));
            ProfileManager.startOnlineChecker();
            isRunning = true;
            serverDashboardController.updateConsole("<div>Start listening on " + StringEscapeUtils.escapeHtml3(address + ":" + port) + "</div>");
            return true;
        } catch (Exception e) {
            isRunning = false;
            rmiRegistry = null;
            serverDashboardController.updateConsole("<div>Cannot start listening on " + StringEscapeUtils.escapeHtml3(address + ":" + port) + "</div>");
            return false;
        }
    }

    public Boolean stopServer() {
        try {
            ProfileManager.stopOnlineChecker();
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            isRunning = false;
            serverDashboardController.updateConsole("<div>Server stopped</div>");
            return true;
        } catch (Exception e) {
            isRunning = true;
            serverDashboardController.updateConsole("<div>Failed to stop server</div>");
            return false;
        }
    }

    public Boolean isRunning() {
        return isRunning;
    }
}
