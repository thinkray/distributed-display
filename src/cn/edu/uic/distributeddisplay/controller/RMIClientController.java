package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.ProfileManager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;


public class RMIClientController {
    private NodeGUIController nodeGUIController;
    private DisplayController displayController;
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
        try {
            clientWorker.interrupt();
            isRunning = false;
            return true;
        } catch (Exception e) {
            isRunning = true;
            return false;
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
                            showConfigWindow("Disconnected");
                            return;
                        } else if (severRespond == DefaultConst.SESSION_RENEWED_NEW_CONFIG_AVAILABLE) {
                            NodeSideProfile newProfile = rmiServerWorkerInterface.getConfig(nodeName, sessionUUID);
                            displayController.setProfile(newProfile);
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    showConfigWindow(" ");
                    return;
                } catch (RemoteException e) {
                    // Back to config panel
                    showConfigWindow("Remote error occurred");
                    throw new RuntimeException(e);
                }
            }
        };

        clientWorker.start();
    }

    private void showConfigWindow(String message) {
        nodeGUIController.getV().setVisible(true);
        nodeGUIController.setComponentsStatus(true, message);
    }

    public NodeGUIController getNodeGUIController() {
        return nodeGUIController;
    }
}
