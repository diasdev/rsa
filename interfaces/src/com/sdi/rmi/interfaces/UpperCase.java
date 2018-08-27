package com.sdi.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UpperCase extends Remote {

    String convertToUpperCase(String text) throws RemoteException;
}
