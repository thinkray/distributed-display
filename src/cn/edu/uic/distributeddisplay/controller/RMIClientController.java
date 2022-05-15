package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.LangManger;
import cn.edu.uic.distributeddisplay.view.NodeConfigView;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.AccessException;
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

    public Boolean startClient(String nodeName, String address) {
        this.nodeName = nodeName;
        this.serverAddress = "rmi://" + address + "/DisplayServer";
        startClientWorker();
        this.isRunning = clientWorker.isAlive();
        return this.isRunning;
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
            showNodeConfigWindow(" ");
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
                    int currentMode = DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN;

                    while (true) {
                        try {
                            if ((currentMode == DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN) || (currentMode == DefaultConst.CLIENT_MODE_RECHECK_IN)) {
                                rmiServerWorkerInterface = (RMIServerWorkerInterface) Naming.lookup(serverAddress);
                                sessionUUID = rmiServerWorkerInterface.checkIn(nodeName);
                                if (sessionUUID == null) {
                                    displayController.setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);
                                    showNodeConfigWindow(LangManger.get("cannot_connect"));
                                    isRunning = false;
                                    return;
                                }
                                displayController.setNodeStatus(DefaultConst.CLIENT_CONNECTED);
                                currentMode = DefaultConst.CLIENT_MODE_HEARTBEAT;
                            } else if (currentMode == DefaultConst.CLIENT_MODE_HEARTBEAT) {
                                int severRespond = rmiServerWorkerInterface.heartbeat(nodeName, sessionUUID);
                                if (severRespond == DefaultConst.INVALID_SESSION) {
                                    displayController.setNodeStatus(DefaultConst.CLIENT_RETRYING);
                                    currentMode = DefaultConst.CLIENT_MODE_RECHECK_IN;
                                } else if (severRespond == DefaultConst.SESSION_RENEWED_NEW_CONFIG_AVAILABLE) {
                                    NodeSideProfile newProfile = rmiServerWorkerInterface.getConfig(nodeName, sessionUUID);
                                    displayController.setProfile(newProfile);
                                }
                            }
                            Thread.sleep(1000);
                        } catch (RemoteException | NotBoundException | MalformedURLException e) {
                            if (currentMode == DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN) {
                                displayController.setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);
                                showNodeConfigWindow(LangManger.get("cannot_connect"));
                                isRunning = false;
                                return;
                            } else if ((currentMode == DefaultConst.CLIENT_MODE_RECHECK_IN) || (currentMode == DefaultConst.CLIENT_MODE_HEARTBEAT)) {
                                displayController.setNodeStatus(DefaultConst.CLIENT_RETRYING);
                                // Retry after 10 seconds (make sure the current session already expired)
                                Thread.sleep(10000);
                                currentMode = DefaultConst.CLIENT_MODE_RECHECK_IN;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    if (rmiServerWorkerInterface != null) {
                        try {
                            rmiServerWorkerInterface.checkOut(nodeName, sessionUUID);
                        } catch (RemoteException ex) {
                            // Pass
                        }
                    }
                    showNodeConfigWindow(LangManger.get("disconnected"));
                    return;
                }
            }
        };

        clientWorker.start();
    }

    private void showNodeConfigWindow(String message) {
        if (Objects.equals(message, "")) {
            message = " ";
        }
        nodeGUIController.setComponentsStatus(true, message);
        NodeConfigView nodeConfigView = nodeGUIController.getNodeConfigView();
        nodeConfigView.setVisible(true);
        nodeConfigView.toFront();
        nodeConfigView.requestFocus();
        nodeConfigView.setAlwaysOnTop(true);
    }

    public NodeGUIController getNodeGUIController() {
        return nodeGUIController;
    }

    public DisplayController getDisplayController() {
        return displayController;
    }
}
