package com.sdi.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KeyExchange extends Remote {

    String exchangeKeys(String publicKey) throws RemoteException;
}
