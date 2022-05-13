package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerWorkerInterface extends Remote {
    public String checkIn(String nodeName) throws RemoteException;

    public Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException;

    public NodeSideProfile getConfig(String nodeName, String sessionUUID) throws RemoteException;

    public int heartbeat(String nodeName, String sessionUUID) throws RemoteException;
}
