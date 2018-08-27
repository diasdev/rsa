package com.sdi.rmi.client;

import com.sdi.rmi.interfaces.KeyExchange;
import com.sdi.rmi.interfaces.UpperCase;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientWorker {

	public void execute() throws RemoteException, NotBoundException {

		String destination = "18.216.164.232";

		Registry registry = LocateRegistry.getRegistry(destination);

		KeyExchange keyExchangeStub = (KeyExchange) registry.lookup("KeyExchange");
		keyExchangeStub.exchangeKeys("teste-client");

		UpperCase upperCaseStub = (UpperCase) registry.lookup("UpperCase");
		String textUpperCased = upperCaseStub.convertToUpperCase("textolowercase");

		System.out.println(textUpperCased);
	}
}
