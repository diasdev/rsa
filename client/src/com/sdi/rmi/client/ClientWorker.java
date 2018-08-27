package com.sdi.rmi.client;

import com.sdi.rmi.interfaces.KeyExchange;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientWorker {

	public void execute() throws RemoteException, NotBoundException {

		String destination = "";

		Registry registry = LocateRegistry.getRegistry(destination);

		KeyExchange keyExchangeStub = (KeyExchange) registry.lookup("KeyExchange");
		keyExchangeStub.exchangeKeys("teste");

	}
}
