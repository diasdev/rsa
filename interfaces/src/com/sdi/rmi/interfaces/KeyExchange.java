package com.sdi.rmi.interfaces;

import java.rmi.Remote;

public interface KeyExchange extends Remote {

    String exchangeKeys(String publicKey);
}
