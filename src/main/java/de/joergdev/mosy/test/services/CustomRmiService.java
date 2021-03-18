package de.joergdev.mosy.test.services;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

public class CustomRmiService implements CustomRmiServiceResponseRemote
{
  /**
   * Startet den RMI Controller um via RMI auf das Backend zugreifen zu koennen.
   * 
   * @param args
   * @throws RemoteException
   */
  public static void main(String[] args)
    throws RemoteException
  {
    try
    {
      //JNDI Port registrieren
      LocateRegistry.createRegistry(5433);

      //Remote Interface fuer BackendResponse erstellen
      CustomRmiServiceResponseRemote response = new CustomRmiService();

      //Stub binden
      CustomRmiServiceResponseRemote stub = (CustomRmiServiceResponseRemote) UnicastRemoteObject
          .exportObject(response, 5433);

      RemoteServer.setLog(System.out);

      //stub JNDI bekannt machen
      LocateRegistry.getRegistry(5433).rebind("CustomRmiService", stub);

      System.out.println("CustomRmiService running on port 5433");
    }
    catch (Throwable th)
    {
      th.printStackTrace();

      throw new RemoteException("ERROR", th);
    }
  }

  public String getRequestedData(String action)
    throws RemoteException
  {
    return "<return>" + getResponseIntern(action) + "</return>";
  }

  private String getResponseIntern(String action)
  {
    try
    {
      if ("<action>1</action>".equals(action))
      {
        return "one";
      }
      else if ("<action>2</action>".equals(action))
      {
        return "two";
      }
      else if ("<action>3</action>".equals(action))
      {
        return "three";
      }
      else if ("<action>4</action>".equals(action))
      {
        return "four";
      }
      else if ("<action>5</action>".equals(action))
      {
        return "five";
      }
      else if ("<action>E</action>".equals(action))
      {
        throw new IllegalStateException("!ERROR!");
      }

      return "??";
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return ex.getMessage();
    }
  }
}