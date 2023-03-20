package cn.edu.uic.distributeddisplay.client.controller;

import cn.edu.uic.distributeddisplay.client.view.ClientConfigView;
import cn.edu.uic.distributeddisplay.core.profile.ClientSideProfile;
import cn.edu.uic.distributeddisplay.core.rmi.RMIServerWorkerInterface;
import cn.edu.uic.distributeddisplay.core.util.DefaultConst;
import cn.edu.uic.distributeddisplay.core.util.LangManger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;


public class RMIClientController {
    private final ClientGUIController clientGUIController;
    private final ClientDisplayController clientDisplayController;
    private String nodeName;
    private String serverAddress;
    private RMIServerWorkerInterface rmiServerWorkerInterface;
    private String sessionUUID;
    private Thread clientWorker;
    private Boolean isRunning;


    public RMIClientController(ClientGUIController clientGUIController, ClientDisplayController clientDisplayController) {
        this.clientGUIController = clientGUIController;
        this.clientDisplayController = clientDisplayController;
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
            showClientConfigWindow(" ");
            return true;
        }
    }

    public Boolean isRunning() {
        return isRunning;
    }

    private void startClientWorker() {
        clientWorker = new Thread(() -> {
            try {
                int currentMode = DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN;

                while (true) {
                    try {
                        if ((currentMode == DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN) || (currentMode == DefaultConst.CLIENT_MODE_RECHECK_IN)) {
                            // First check in or recheck in after network issues
                            rmiServerWorkerInterface = (RMIServerWorkerInterface) Naming.lookup(serverAddress);
                            sessionUUID = rmiServerWorkerInterface.checkIn(nodeName);
                            if (sessionUUID == null) {
                                clientDisplayController.setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);
                                showClientConfigWindow(LangManger.get("error_duplicate_node_name_client"));
                                isRunning = false;
                                return;
                            }
                            clientDisplayController.setNodeStatus(DefaultConst.CLIENT_CONNECTED);
                            currentMode = DefaultConst.CLIENT_MODE_HEARTBEAT;
                        } else if (currentMode == DefaultConst.CLIENT_MODE_HEARTBEAT) {
                            // Normal heartbeats
                            int severRespond = rmiServerWorkerInterface.heartbeat(nodeName, sessionUUID);
                            if (severRespond == DefaultConst.INVALID_SESSION) {
                                clientDisplayController.setNodeStatus(DefaultConst.CLIENT_RETRYING);
                                currentMode = DefaultConst.CLIENT_MODE_RECHECK_IN;
                            } else if (severRespond == DefaultConst.SESSION_RENEWED_NEW_CONFIG_AVAILABLE) {
                                ClientSideProfile newProfile = rmiServerWorkerInterface.getConfig(nodeName,
                                        sessionUUID);
                                clientDisplayController.setProfile(newProfile);
                            }
                        }
                        Thread.sleep(1000);
                    } catch (RemoteException | NotBoundException | MalformedURLException e) {
                        if (currentMode == DefaultConst.CLIENT_MODE_FIRST_TIME_CHECK_IN) {
                            clientDisplayController.setNodeStatus(DefaultConst.CLIENT_NOT_CONNECTED);
                            showClientConfigWindow(LangManger.get("cannot_connect"));
                            isRunning = false;
                            return;
                        } else if ((currentMode == DefaultConst.CLIENT_MODE_RECHECK_IN) || (currentMode == DefaultConst.CLIENT_MODE_HEARTBEAT)) {
                            clientDisplayController.setNodeStatus(DefaultConst.CLIENT_RETRYING);
                            // Retry after 10 seconds (make sure the current session has already expired)
                            Thread.sleep(7000);
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
                showClientConfigWindow(LangManger.get("disconnected"));
            }
        });

        clientWorker.start();
    }

    private void showClientConfigWindow(String message) {
        if (Objects.equals(message, "")) {
            message = " ";
        }
        clientGUIController.setComponentsStatus(true, message);
        ClientConfigView clientConfigView = clientGUIController.getNodeConfigView();
        clientConfigView.setVisible(true);
        clientConfigView.toFront();
        clientConfigView.requestFocus();
        clientConfigView.setAlwaysOnTop(true);
    }

    public ClientGUIController getClientGUIController() {
        return clientGUIController;
    }

    public ClientDisplayController getClientDisplayController() {
        return clientDisplayController;
    }
}
