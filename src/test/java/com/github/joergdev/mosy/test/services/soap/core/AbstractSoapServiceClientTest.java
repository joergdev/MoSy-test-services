package com.github.joergdev.mosy.test.services.soap.core;

import static org.junit.Assert.*;
import com.github.joergdev.mosy.api.client.Resources;
import com.github.joergdev.mosy.api.model.Interface;
import com.github.joergdev.mosy.api.model.InterfaceMethod;
import com.github.joergdev.mosy.api.model.InterfaceType;
import com.github.joergdev.mosy.api.model.MockProfile;
import com.github.joergdev.mosy.test.services.AbstractServiceClientTest;
import com.github.joergdev.mosy.test.services.SoapService;

public abstract class AbstractSoapServiceClientTest extends AbstractServiceClientTest
{
  @Override
  protected void _before()
  {
    // Start the Service
    SoapService.main(null);

    // set endpoint to mosy
    SoapServiceClientPortSingleton.getInstance()
        .setUrl(Resources.getProperty("api_endpoint") + "/mock-services/soap/SoapService");
  }

  protected Interface getDefaultInterface()
  {
    Interface apiInterface = super.getDefaultInterface();
    apiInterface.setName("SoapService");
    apiInterface.setType(InterfaceType.SOAP);
    apiInterface.setRoutingUrl("http://localhost:5432/soap");
    apiInterface.setServicePath("SoapService");

    return apiInterface;
  }

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

  protected void invokeWsCall(String request, String assertion, Integer mockProfileID,
                              Integer recordSessionID)
    throws Exception
  {
    String result = SoapServiceClientPortSingleton.getInstance().invoke(request, mockProfileID,
        recordSessionID);

    assertEquals(assertion, result);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue,
                             MockProfile apiMockProfile)
  {
    addMockData(title, active, requestAction, returnValue, apiMockProfile, false);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue,
                             MockProfile apiMockProfile, boolean common)
  {
    if (requestAction != null)
    {
      requestAction = "<action>" + requestAction + "</action>";
    }

    returnValue = "<?xml version=\"1.0\" ?>"
                  + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" //
                  + "<S:Body>" //
                  + "<ns2:testMethodResponse xmlns:ns2=\"http://services.test.mosy.joergdev.github.com/\">"
                  + "<return>" + returnValue + "</return>" //
                  + "</ns2:testMethodResponse>" //
                  + "</S:Body>" //
                  + "</S:Envelope>";

    super.addMockData(title, active, requestAction, returnValue, apiMockProfile, common);
  }

  protected void addRecordConfig(String title, boolean enabled, String requestAction)
  {
    requestAction = "<action>" + requestAction + "</action>";

    super.addRecordConfig(title, enabled, requestAction);
  }
}