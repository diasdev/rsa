package com.sdi.rmi.server;

import com.sdi.rmi.interfaces.KeyExchange;
import com.sdi.rmi.interfaces.ReplicaServer;
import com.sdi.rmi.interfaces.UpperCase;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerImpl implements KeyExchange, UpperCase {

    private List<String> messageList = new ArrayList<>();
    private Map<String, String> serverList = new HashMap<>();

    private Boolean clientAuthenticated = false;

    public ServerImpl() {}

    public static void main(String args[]) {

        try {
            KeyExchange keyExchangeObject = new ServerImpl();
            KeyExchange keyExchangeStub = (KeyExchange) UnicastRemoteObject.exportObject(keyExchangeObject, 0);

            UpperCase upperCaseObject = new ServerImpl();
            UpperCase upperCaseStub = (UpperCase) UnicastRemoteObject.exportObject(upperCaseObject, 0);

            Registry registry = LocateRegistry.getRegistry();

            registry.bind("KeyExchange", keyExchangeStub);
            registry.bind("UpperCase", upperCaseStub);

            System.err.println("Echo Server is ready");
        } catch (Exception e) {
            System.err.println("Echo Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public String exchangeKeys(String publicKey) {
        clientAuthenticated = true;
        return "teste-server";
    }

    @Override
    public String convertToUpperCase(String text) throws RemoteException {
        if (!clientAuthenticated)
            throw new RemoteException("não trocou chaves ainda");

        return text.toUpperCase();
    }
}
