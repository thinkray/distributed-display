package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.NodeSideProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIWorkerInterface extends Remote {
    public String checkIn(String nodeName) throws RemoteException;

    public Boolean checkOut(String nodeName, String sessionUUID) throws RemoteException;

//    public NodeSideProfile getConfig(String sessionUUID) throws RemoteException;
//
//    public int heartbeat(String sessionUUID) throws RemoteException;
}
