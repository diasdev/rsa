package com.sdi.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EchoServer extends Remote {

    String echo(String message) throws RemoteException;

    List<String> getListOfMessages(String clientName) throws RemoteException;
}
