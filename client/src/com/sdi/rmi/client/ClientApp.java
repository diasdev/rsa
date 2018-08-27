package com.sdi.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

import com.sdi.rmi.interfaces.EchoServer;

public class ClientApp {

    private ClientApp() {
    }

    public static void main(String[] args) {

        String message = (args.length < 1) ? null : args[0];
        String returnedMessage = "";

        for(Map.Entry<String, String> entry : getServerList().entrySet()) {
            String serverName = entry.getKey();
            String serverAddr = entry.getValue();

            try {
                returnedMessage = tryAndGetEcho(message, serverAddr);

                if (!returnedMessage.isEmpty()) {
                    System.out.println("Server echoed message: " + returnedMessage);
                    break;
                }
            }
            catch (RemoteException e) {
                System.err.println("Server responded with an exception: " + e.toString());
                e.printStackTrace();
            }
            catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    private static String tryAndGetEcho(String message, String destination) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(destination);
        EchoServer serverStub = (EchoServer) registry.lookup("EchoServer");

        return serverStub.echo(message);
    }

    private static Map<String, String> getServerList() {
        Map<String, String>  serverList = new HashMap<>();

        serverList.put("Server1", "18.216.164.232");
        serverList.put("Server2", "18.216.164.232");
        serverList.put("Server3", "18.216.164.232");

        return serverList;
    }
}