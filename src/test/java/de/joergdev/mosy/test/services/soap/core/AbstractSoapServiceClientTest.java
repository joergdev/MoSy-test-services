package de.joergdev.mosy.test.services.soap.core;

import static org.junit.Assert.*;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.InterfaceType;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.test.services.AbstractServiceClientTest;
import de.joergdev.mosy.test.services.SoapService;

public abstract class AbstractSoapServiceClientTest extends AbstractServiceClientTest
{
  @Override
  protected void _before()
  {
    // Start the Service
    SoapService.main(null);

    if (!isMulitTanencyEnabled())
    {
      setSoapServiceEndpointToMosyMockService();
    }
  }

  @Override
  protected void doLoginAndCreateBaseData()
  {
    super.doLoginAndCreateBaseData();

    // Relevant for multi-tenancy, in this case we cannot set the url on begin because it creates an request for the ?wsdl endoint.
    // This requires an tenantId in the http header.
    // So we have to wait for the creation and initialisation of the first tenant.
    if (!SoapServiceClientPortSingleton.getInstance().isServiceInitialised() || isMulitTanencyEnabled())
    {
      setSoapServiceEndpointToMosyMockService();
    }
  }

  protected void setSoapServiceEndpointToMosyMockService()
  {
    SoapServiceClientPortSingleton.getInstance().initService(null);
  }

  @Override
  protected Interface getDefaultInterface()
  {
    Interface apiInterface = super.getDefaultInterface();
    apiInterface.setName("SoapService");
    apiInterface.setType(InterfaceType.SOAP);
    apiInterface.setRoutingUrl("http://localhost:5432/soap");
    apiInterface.setServicePath("SoapService");

    return apiInterface;
  }

  @Override
  protected InterfaceMethod getDefaultInterfaceMethod()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("testMethod");
    apiMethod.setServicePath("testMethod");

    return apiMethod;
  }

  protected void invokeWsCall(String request, String assertion)
    throws Exception
  {
    invokeWsCall(request, assertion, null, null);
  }

  protected void invokeWsCall(String request, String assertion, String mockProfileName, Integer recordSessionID)
    throws Exception
  {
    String result = SoapServiceClientPortSingleton.getInstance().invoke(request, activeTenantId, mockProfileName, recordSessionID);

    assertEquals(assertion, result);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile)
  {
    addMockData(title, active, requestAction, returnValue, apiMockProfile, false);
  }

  @Override
  protected void addMockData(String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile, boolean common)
  {
    if (requestAction != null)
    {
      requestAction = "<action>" + requestAction + "</action>";
    }

    returnValue = "<?xml version=\"1.0\" ?>" + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" //
                  + "<S:Body>" //
                  + "<ns2:testMethodResponse xmlns:ns2=\"http://services.test.mosy.joergdev.de/\">" + "<return>" + returnValue + "</return>" //
                  + "</ns2:testMethodResponse>" //
                  + "</S:Body>" //
                  + "</S:Envelope>";

    super.addMockData(title, active, requestAction, returnValue, apiMockProfile, common, null);
  }

  @Override
  protected void addRecordConfig(String title, boolean enabled, String requestAction)
  {
    requestAction = "<action>" + requestAction + "</action>";

    super.addRecordConfig(title, enabled, requestAction);
  }
}
