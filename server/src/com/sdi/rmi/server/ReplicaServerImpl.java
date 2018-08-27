package com.sdi.rmi.server;

import com.sdi.rmi.interfaces.ReplicaServer;

import java.rmi.RemoteException;

public class ReplicaServerImpl implements ReplicaServer {

    public Boolean receiveReplicatedMessage(String message) throws RemoteException {
        return false;
    }
}
