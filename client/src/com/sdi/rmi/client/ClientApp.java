package com.sdi.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientApp {

    private ClientApp() {
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {

        ClientWorker worker = new ClientWorker();
        worker.execute();

    }
}