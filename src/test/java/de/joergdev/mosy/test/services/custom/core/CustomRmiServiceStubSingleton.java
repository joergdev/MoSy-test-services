package de.joergdev.mosy.test.services.custom.core;

import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import de.joergdev.mosy.test.services.CustomRmiServiceResponseRemote;

public class CustomRmiServiceStubSingleton
{
  private static CustomRmiServiceStubSingleton instance = null;
  private CustomRmiServiceResponseRemote stub;

  private CustomRmiServiceStubSingleton()
  {}

  private void createStub()
  {
    try
    {
      stub = (CustomRmiServiceResponseRemote) Naming.lookup("rmi://localhost:5433/CustomRmiService");
    }
    catch (Exception ex)
    {
      throw new IllegalStateException(ex);
    }
  }

  public static CustomRmiServiceStubSingleton getInstance()
  {
    if (instance == null)
    {
      instance = new CustomRmiServiceStubSingleton();
    }

    return instance;
  }

  public CustomRmiServiceResponseRemote getStub()
  {
    if (stub == null)
    {
      createStub();
    }

    return stub;
  }

  public static String invoke(String action)
    throws Exception
  {
    try
    {
      return getInstance().getStub().getRequestedData(action);
    }
    catch (NoSuchObjectException ex)
    {
      getInstance().createStub();
      return getInstance().getStub().getRequestedData(action);
    }
  }
}