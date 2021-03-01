package de.joergdev.mosy.test.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomRmiServiceResponseRemote extends Remote
{
  public String getRequestedData(String action)
    throws RemoteException;
}
