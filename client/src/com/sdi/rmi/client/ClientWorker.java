package com.sdi.rmi.client;

import com.sdi.rmi.interfaces.KeyExchange;
import com.sdi.rmi.interfaces.UpperCase;

import javax.crypto.Cipher;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClientWorker {

	private KeyPair pair;
	private PublicKey serverpublicKey;

	public void execute() throws RemoteException, NotBoundException {

		generateKeyPair();

		String destination = "18.216.164.232";
		Registry registry = LocateRegistry.getRegistry(destination);

		KeyExchange keyExchangeStub = (KeyExchange) registry.lookup("KeyExchange");
		serverpublicKey = keyExchangeStub.exchangeKeys(getPublicKey());

		UpperCase upperCaseStub = (UpperCase) registry.lookup("UpperCase");

		String textUpperCased = upperCaseStub.convertToUpperCase(encrypt("textolowercase", serverpublicKey));
		System.out.println("Encrypted uppercased text: " + textUpperCased);

		String decryptedText = decrypt(textUpperCased, getPrivateKey());
		System.out.println("Dcrypted uppercased text: " + decryptedText);

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

	public static String encrypt(String plainText, PublicKey publicKey) throws RemoteException {
		try {
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

			return Base64.getEncoder().encodeToString(cipherText);
		}
		catch (Exception ex) {
			throw new RemoteException(ex.getLocalizedMessage());
		}
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) throws RemoteException {
		try {
			byte[] bytes = Base64.getDecoder().decode(cipherText);

			Cipher decriptCipher = Cipher.getInstance("RSA");
			decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

			return new String(decriptCipher.doFinal(bytes), UTF_8);
		}
		catch (Exception ex) {
			throw new RemoteException(ex.getLocalizedMessage());
		}
	}
}
