package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;


public class RMIClientController {
    private final NodeGUIController nodeGUIController;
    private final DisplayController displayController;
    private String nodeName;
    private String serverAddress;
    private RMIServerWorkerInterface rmiServerWorkerInterface;
    private String sessionUUID;
    private Thread clientWorker;
    private Boolean isRunning;


    public RMIClientController(NodeGUIController nodeGUIController, DisplayController displayController) {
        this.nodeGUIController = nodeGUIController;
        this.displayController = displayController;
        this.isRunning = false;
    }

    public Boolean startClient(String nodeName, String address) throws NotBoundException, MalformedURLException,
            RemoteException {
        this.nodeName = nodeName;
        this.serverAddress = "rmi://" + address + "/DisplayServer";
        this.rmiServerWorkerInterface = (RMIServerWorkerInterface) Naming.lookup(this.serverAddress);
        this.sessionUUID = this.rmiServerWorkerInterface.checkIn(nodeName);
        if (this.sessionUUID == null) {
            return false;
        }
        startClientWorker();
        isRunning = true;
        return true;
    }

    public Boolean stopClient() {
        if (isRunning) {
            try {
                clientWorker.interrupt();
                isRunning = false;
                return true;
            } catch (Exception e) {
                isRunning = true;
                return false;
            }
        } else {
            showConfigWindow(" ");
            return true;
        }
    }

    public Boolean isRunning() {
        return isRunning;
    }

    private void startClientWorker() {
        clientWorker = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        int severRespond = rmiServerWorkerInterface.heartbeat(nodeName, sessionUUID);
                        if (severRespond == DefaultConst.INVALID_SESSION) {
                            showConfigWindow(LangManger.get("disconnected"));
                            return;
                        } else if (severRespond == DefaultConst.SESSION_RENEWED_NEW_CONFIG_AVAILABLE) {
                            NodeSideProfile newProfile = rmiServerWorkerInterface.getConfig(nodeName, sessionUUID);
                            displayController.setProfile(newProfile);
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    try {
                        rmiServerWorkerInterface.checkOut(nodeName, sessionUUID);
                    } catch (RemoteException ex) {
                    }
                    showConfigWindow(LangManger.get("disconnected"));
                    return;
                } catch (RemoteException e) {
                    // Back to config panel
                    showConfigWindow(LangManger.get("remote_error"));
                    return;
                }
            }
        };

        clientWorker.start();
    }

    private void showConfigWindow(String message) {
        if (Objects.equals(message, "")) {
            message = " ";
        }
        nodeGUIController.setComponentsStatus(true, message);
        NodeConfigView nodeConfigView = nodeGUIController.getNodeConfigView();
        nodeConfigView.setVisible(true);
        nodeConfigView.toFront();
        nodeConfigView.requestFocus();
        nodeConfigView.setAlwaysOnTop(true);
        nodeConfigView.setAlwaysOnTop(false);
    }

    public NodeGUIController getNodeGUIController() {
        return nodeGUIController;
    }
}
