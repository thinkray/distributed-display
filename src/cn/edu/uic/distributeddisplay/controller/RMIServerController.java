package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import org.apache.commons.text.StringEscapeUtils;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIServerController {
    private final ServerDashboardController serverDashboardController;
    private String address;
    private Integer port;
    private Boolean isRunning;
    private Registry rmiRegistry;
    private RMIServerWorkerController rmiServerWorkerController;

    public RMIServerController(ServerDashboardController serverDashboardController) {
        this.serverDashboardController = serverDashboardController;
        this.isRunning = false;
    }

    public Boolean startServer(String address, Integer port) {
        try {
            rmiRegistry = LocateRegistry.createRegistry(port);
            rmiServerWorkerController = new RMIServerWorkerController(this);
            Naming.rebind("rmi://" + address + ":" + port + "/DisplayServer", rmiServerWorkerController);
            ProfileManager.startOnlineChecker(this);
            isRunning = true;
            serverDashboardController.updateConsole(String.format("<div style=\"background-color: green; color: " +
                    "white;\">%s %s</div>", LangManger.get("start_listening_on"),
                    StringEscapeUtils.escapeHtml3(address + ":" + port)));
            return true;
        } catch (Exception e) {
            isRunning = false;
            rmiRegistry = null;
            serverDashboardController.updateConsole(String.format("<div style=\"background-color: red; color: white;" +
                    "\">%s %s</div>", LangManger.get("cannot_listen_on"),
                    StringEscapeUtils.escapeHtml3(address + ":" + port)));
            return false;
        }
    }

    public Boolean stopServer() {
        try {
            ProfileManager.stopOnlineChecker();
            UnicastRemoteObject.unexportObject(rmiServerWorkerController, true);
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            isRunning = false;
            serverDashboardController.updateConsole(String.format("<div style=\"background-color: olive; color: " +
                    "white;\">%s</div>", LangManger.get("server_stopped")));
            return true;
        } catch (Exception e) {
            isRunning = true;
            serverDashboardController.updateConsole(String.format("<div style=\"background-color: red; color: white;" +
                    "\">%s</div>", LangManger.get("failed_to_stop")));
            return false;
        }
    }

    public Boolean isRunning() {
        return isRunning;
    }

    public ServerDashboardController getServerDashboardController() {
        return serverDashboardController;
    }
}
