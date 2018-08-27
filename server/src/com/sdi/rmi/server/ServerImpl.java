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

    /*@Override
    public String echo(String message) throws RemoteException {
        addMessageToHistoricList(message);
        broadcastMessage(message);
        return message;
    }

    @Override
    public List<String> getListOfMessages(String clientName) throws RemoteException {
        return this.messageList;
    }

    @Override
    public void receiveReplicatedMessage(String message) throws RemoteException {
        addMessageToHistoricList(message);
    }*/

    private void addMessageToHistoricList(String message) {
        this.messageList.add(message);
    }

    private void broadcastMessage(String message){
        for(Map.Entry<String, String> entry : serverList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            replicateMessage(message, value);
        }
    }

    private void replicateMessage(String message, String destination) {
        try {
            Registry registry = LocateRegistry.getRegistry(destination);
            ReplicaServer replicaStub = (ReplicaServer) registry.lookup("ReplicaServer");
            replicaStub.receiveReplicatedMessage(message);
        }
        catch (NotBoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        catch (RemoteException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
