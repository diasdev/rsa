package com.sdi.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface KeyExchange extends Remote {

    PublicKey exchangeKeys(PublicKey publicKey) throws RemoteException;
}
