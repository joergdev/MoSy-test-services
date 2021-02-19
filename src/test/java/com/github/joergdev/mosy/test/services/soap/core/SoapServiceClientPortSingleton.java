package com.github.joergdev.mosy.test.services.soap.core;

public class SoapServiceClientPortSingleton
{
  static final ThreadLocal<String> SOAP_MOCK_PROFILE_NAME = new ThreadLocal<>();
  static final ThreadLocal<Integer> SOAP_RECORD_SESSION_ID = new ThreadLocal<>();

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

  public String invoke(String request, String mockProfileName, Integer recordSessionID)
    throws Exception
  {
    SOAP_MOCK_PROFILE_NAME.set(mockProfileName);
    SOAP_RECORD_SESSION_ID.set(recordSessionID);

    return service.testMethod(request);
  }
}