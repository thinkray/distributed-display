package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerWorkerInterface extends Remote {
    String checkIn(String nodeName) throws RemoteException;

    Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException;

    NodeSideProfile getConfig(String nodeName, String sessionUUID) throws RemoteException;

    int heartbeat(String nodeName, String sessionUUID) throws RemoteException;
}
