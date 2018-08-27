package com.sdi.rmi.client;

import java.rmi.registry.Registry;

public class KeyExchanger {

	private Registry registry;

	public KeyExchanger(Registry registry) {
		this.registry = registry;
	}

	public String exchangeKeysWithServer(String key) {

		return "";
	}
}
