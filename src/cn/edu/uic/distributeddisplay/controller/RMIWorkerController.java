package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import cn.edu.uic.distributeddisplay.util.ProfileRow;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.UUID;

public class RMIWorkerController extends UnicastRemoteObject implements RMIWorkerInterface {
    private RMIServerController rmiServerController;

    public RMIWorkerController(RMIServerController rmiServerController) throws RemoteException {
        this.rmiServerController = rmiServerController;
    }

    public String checkIn(String nodeName) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if ((currentProfileRow == null) || (currentProfileRow.isOnline == false)) {
            ProfileRow profileRow = new ProfileRow(new ServerSideProfile(), true, new Date(), UUID.randomUUID().toString());
            ProfileManager.putProfileRow(nodeName, profileRow);
            return profileRow.uuid;
        } else {
            return "";
        }
    }

    public Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException {
        return true;
    }

//    public NodeSideProfile getConfig(String sessionUUID) throws RemoteException {
//
//    }
//
//    public int heartbeat(String sessionUUID) throws RemoteException {
//
//    }

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
