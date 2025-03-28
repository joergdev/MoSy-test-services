package de.joergdev.mosy.test.services.soap.core;

import java.util.Objects;

public class SoapServiceClientPortSingleton
{
  static final ThreadLocal<Integer> SOAP_TENANT_ID = new ThreadLocal<>();
  static final ThreadLocal<String> SOAP_MOCK_PROFILE_NAME = new ThreadLocal<>();
  static final ThreadLocal<Integer> SOAP_RECORD_SESSION_ID = new ThreadLocal<>();

  private static SoapServiceClientPortSingleton instance = null;
  private SoapService service;
  private String initialUrl;

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

  /**
   * 
   * @param url - optional, if not set local wsdl is used
   */
  public void initService(String url)
  {
    if (!isServiceInitialised() || !Objects.equals(url, initialUrl))
    {
      try
      {
        service = new SoapServiceService(url).getSoapServicePort();

        initialUrl = url;
      }
      catch (Exception ex)
      {
        throw new IllegalStateException(ex);
      }
    }
  }

  public String invoke(String request, Integer tenantId, String mockProfileName, Integer recordSessionID)
    throws Exception
  {
    SOAP_TENANT_ID.set(tenantId);
    SOAP_MOCK_PROFILE_NAME.set(mockProfileName);
    SOAP_RECORD_SESSION_ID.set(recordSessionID);

    return service.testMethod(request);
  }

  public boolean isServiceInitialised()
  {
    return service != null;
  }
}
