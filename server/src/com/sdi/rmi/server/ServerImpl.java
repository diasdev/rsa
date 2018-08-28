package com.sdi.rmi.server;

import com.sdi.rmi.interfaces.KeyExchange;
import com.sdi.rmi.interfaces.ReplicaServer;
import com.sdi.rmi.interfaces.UpperCase;

import javax.crypto.Cipher;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServerImpl implements KeyExchange, UpperCase {

    private static KeyPair pair;
    private static PublicKey clientPublicKey;

    private static Boolean clientAuthenticated = false;

    public ServerImpl() {}

    public static void main(String args[]) {

        try {
            System.setProperty("java.rmi.server.hostname", "18.216.164.232");
            LocateRegistry.createRegistry(1099);
            //Runtime.getRuntime().exec("rmiregistry 1099");

            //System.out.println(java.net.InetAddress.getLocalHost());

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

    @Override
    public PublicKey exchangeKeys(PublicKey publicKey) throws RemoteException {
        generateKeyPair();

        System.out.println("Exchanging keys");
        clientPublicKey = publicKey;
        clientAuthenticated = true;

        return getPublicKey();
    }

    @Override
    public String convertToUpperCase(String text) throws RemoteException {
        if (!clientAuthenticated)
            throw new RemoteException("não trocou chaves ainda");

        System.out.println("Processing message");

        System.out.println("Original encrypted message: " + text);

        try {
            String originalMsg = decrypt(text, getPrivateKey());
            System.out.println("Original decrypted message: " + originalMsg);

            String encryptedMsg = encrypt(originalMsg.toUpperCase(), clientPublicKey);
            System.out.println("Uppercased encrypted message: " + encryptedMsg);

            return encryptedMsg;
        }
        catch (Exception ex) {
            throw new RemoteException(ex.getLocalizedMessage());
        }
    }

    public void generateKeyPair() throws RemoteException {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, new SecureRandom());
            this.pair = generator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new RemoteException(ex.getLocalizedMessage());
        }
    }

    private PrivateKey getPrivateKey() {
        return pair.getPrivate();
    }

    private PublicKey getPublicKey() {
        return pair.getPublic();
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }
}
