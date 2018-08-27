package com.sdi.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReplicaServer extends Remote {
    void receiveReplicatedMessage(String message) throws RemoteException;
}
