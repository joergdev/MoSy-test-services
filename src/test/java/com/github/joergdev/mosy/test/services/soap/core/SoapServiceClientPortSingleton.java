package com.github.joergdev.mosy.test.services.soap.core;

public class SoapServiceClientPortSingleton
{
  static final ThreadLocal<Integer> SOAP_MOCK_SESSION_ID = new ThreadLocal<>();

  private static SoapServiceClientPortSingleton instance = null;
  private SoapService service;

  private SoapServiceClientPortSingleton()
  {

  }

  public static SoapServiceClientPortSingleton getInstance()
  {
    if (instance == null)
    {
      instance = new SoapServiceClientPortSingleton();
    }

    return instance;
  }

  public void setUrl(String url)
  {
    createConnection(url);
  }

  private void createConnection(String url)
  {
    try
    {
      service = new SoapServiceService(url).getSoapServicePort();
    }
    catch (Exception ex)
    {
      throw new IllegalStateException(ex);
    }
  }

  public String invoke(String request, Integer mockSessionID)
    throws Exception
  {
    SOAP_MOCK_SESSION_ID.set(mockSessionID);

    return service.testMethod(request);
  }
}