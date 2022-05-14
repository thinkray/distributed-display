package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;
import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.ProfileManager;
import cn.edu.uic.distributeddisplay.util.ProfileRow;
import org.apache.commons.text.StringEscapeUtils;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RMIServerWorkerController extends UnicastRemoteObject implements RMIServerWorkerInterface {
    private final RMIServerController rmiServerController;

    public RMIServerWorkerController(RMIServerController rmiServerController) throws RemoteException {
        this.rmiServerController = rmiServerController;
    }

    public String checkIn(String nodeName) throws RemoteException {
        if (nodeName.length() == 0) {
            return null;
        }

        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);

        if (currentProfileRow == null) {
            ProfileRow profileRow = new ProfileRow(new ServerSideProfile(), true, new Date(),
                    UUID.randomUUID().toString());
            ProfileManager.putProfileRow(nodeName, profileRow);
            rmiServerController.getServerDashboardController().updateConsole(String.format("<div style=\"color:yellow"
                            + "\">%s Node [%s] goes online. A new profile created.</div>", new Date(),
                    StringEscapeUtils.escapeHtml3(nodeName)));
            return profileRow.uuid;
        } else if (!currentProfileRow.isOnline) {
            currentProfileRow.isOnline = true;
            currentProfileRow.lastSeen = new Date();
            currentProfileRow.uuid = UUID.randomUUID().toString();
            currentProfileRow.newConfigAvailable = true;
            rmiServerController.getServerDashboardController().updateConsole(String.format("<div style=\"color:yellow"
                            + "\">%s Node [%s] goes online. Found an existing profile.</div>", new Date(),
                    StringEscapeUtils.escapeHtml3(nodeName)));
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
            rmiServerController.getServerDashboardController().updateConsole(String.format("<div style=\"color:yellow"
                    + "\">%s Node [%s] goes offline.</div>", new Date(), StringEscapeUtils.escapeHtml3(nodeName)));
            return true;
        }
    }

    public NodeSideProfile getConfig(String nodeName, String sessionUUID) throws RemoteException {
        ProfileRow currentProfileRow = ProfileManager.getProfileRow(nodeName);
        if ((currentProfileRow == null) || (!currentProfileRow.isOnline) || (!Objects.equals(currentProfileRow.uuid,
                sessionUUID))) {
            return null;
        }

        rmiServerController.getServerDashboardController().updateConsole(String.format("<div style=\"color:lime" +
                "\">%s Distribute profile to node [%s].</div>", new Date(), StringEscapeUtils.escapeHtml3(nodeName)));

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
}
