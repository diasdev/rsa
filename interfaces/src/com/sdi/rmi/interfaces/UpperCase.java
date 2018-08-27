package com.sdi.rmi.interfaces;

import java.rmi.Remote;

public interface UpperCase extends Remote {

    String convertToUpperCase(String text);
}
