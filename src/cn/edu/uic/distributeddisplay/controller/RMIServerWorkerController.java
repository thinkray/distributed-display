package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import cn.edu.uic.distributeddisplay.util.ProfileRow;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RMIServerWorkerController extends UnicastRemoteObject implements RMIServerWorkerInterface {
    private RMIServerController rmiServerController;

    public RMIServerWorkerController(RMIServerController rmiServerController) throws RemoteException {
        this.rmiServerController = rmiServerController;
    }

    public String checkIn(String nodeName) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if (currentProfileRow == null) {
            ProfileRow profileRow = new ProfileRow(new ServerSideProfile(), true, new Date(),
                    UUID.randomUUID().toString());
            ProfileManager.putProfileRow(nodeName, profileRow);
            return profileRow.uuid;
        } else if (!currentProfileRow.isOnline) {
            currentProfileRow.lastSeen = new Date();
            currentProfileRow.uuid = UUID.randomUUID().toString();
            return currentProfileRow.uuid;
        } else {
            return null;
        }
    }

    public Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if ((currentProfileRow == null) || (!Objects.equals(currentProfileRow.uuid, sessionUUID))) {
            return false;
        } else {
            currentProfileRow.lastSeen = new Date(0L);
            currentProfileRow.isOnline = false;
            return true;
        }
    }

    public NodeSideProfile getConfig(String nodeName, String sessionUUID) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if ((currentProfileRow == null) || (!currentProfileRow.isOnline) || (!Objects.equals(currentProfileRow.uuid,
                sessionUUID))) {
            return null;
        }

        currentProfileRow.newConfigAvailable = false;
        return new NodeSideProfile(currentProfileRow.serverSideProfile);
    }

    public int heartbeat(String nodeName, String sessionUUID) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if ((currentProfileRow == null) || (!currentProfileRow.isOnline) || (!Objects.equals(currentProfileRow.uuid,
                sessionUUID))) {
            return DefaultConst.INVALID_SESSION;
        }

        currentProfileRow.lastSeen = new Date();
        if (currentProfileRow.newConfigAvailable) {
            return DefaultConst.SESSION_RENEWED_NEW_CONFIG_AVAILABLE;
        }

        return DefaultConst.SESSION_RENEWED_NO_NEW_CONFIG;
    }

//    public int update(int new_number) throws RemoteException {
//        synchronized (this) {
//            System.out.println("Update number to: " + new_number);
//            this.number = new_number;
//            return this.number;
//        }
//    }
//
//    public int add() throws RemoteException {
//        synchronized (this) {
//            number++;
//            return number;
//        }
//    }
}
