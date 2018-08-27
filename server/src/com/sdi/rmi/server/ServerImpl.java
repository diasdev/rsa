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

    /*private List<String> messageList = new ArrayList<>();
    private Map<String, String> serverList = new HashMap<>();*/

    private static Boolean clientAuthenticated = false;

    public ServerImpl() {}

    public static void main(String args[]) {

        try {
            System.setProperty("java.rmi.server.hostname", "18.216.164.232");
            LocateRegistry.createRegistry(1099);
            //Runtime.getRuntime().exec("rmiregistry 1099");

            //System.out.println(java.net.InetAddress.getLocalHost());
            //System.setProperty("java.rmi.server.hostname", "192.168.2.62");

            KeyExchange keyExchangeObject = new ServerImpl();
            KeyExchange keyExchangeStub = (KeyExchange) UnicastRemoteObject.exportObject(keyExchangeObject, 1099);

            UpperCase upperCaseObject = new ServerImpl();
            UpperCase upperCaseStub = (UpperCase) UnicastRemoteObject.exportObject(upperCaseObject, 1099);

            Registry registry = LocateRegistry.getRegistry();

            registry.bind("KeyExchange", keyExchangeStub);
            registry.bind("UpperCase", upperCaseStub);

            System.out.println("RSA Server is ready");
        } catch (Exception e) {
            System.err.println("RSA Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public String exchangeKeys(String publicKey) {
        System.out.println("Exchanging keys");
        clientAuthenticated = true;
        return "teste-server";
    }

    @Override
    public String convertToUpperCase(String text) throws RemoteException {
        if (!clientAuthenticated)
            throw new RemoteException("não trocou chaves ainda");

        System.out.println("Processing message");
        return text.toUpperCase();
    }
}
