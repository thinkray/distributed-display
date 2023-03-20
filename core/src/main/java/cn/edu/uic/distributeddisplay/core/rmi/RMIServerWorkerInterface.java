package cn.edu.uic.distributeddisplay.core.rmi;

import cn.edu.uic.distributeddisplay.core.profile.ClientSideProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerWorkerInterface extends Remote {
    String checkIn(String nodeName) throws RemoteException;
    Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException;
    ClientSideProfile getConfig(String nodeName, String sessionUUID) throws RemoteException;
    int heartbeat(String nodeName, String sessionUUID) throws RemoteException;
}
