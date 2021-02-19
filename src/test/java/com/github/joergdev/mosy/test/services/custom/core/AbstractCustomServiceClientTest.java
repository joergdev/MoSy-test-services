package com.github.joergdev.mosy.test.services.custom.core;

import static org.junit.Assert.*;
import java.rmi.RemoteException;
import com.github.joergdev.mosy.api.model.Interface;
import com.github.joergdev.mosy.api.model.InterfaceMethod;
import com.github.joergdev.mosy.api.model.InterfaceType;
import com.github.joergdev.mosy.api.model.MockProfile;
import com.github.joergdev.mosy.api.model.Record;
import com.github.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import com.github.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import com.github.joergdev.mosy.test.services.AbstractServiceClientTest;
import com.github.joergdev.mosy.test.services.CustomRmiService;

public abstract class AbstractCustomServiceClientTest extends AbstractServiceClientTest
{
  @Override
  protected void _before()
  {
    // Start the Service
    try
    {
      CustomRmiService.main(null);
    }
    catch (RemoteException ex)
    {
      if (!ex.getMessage().contains("internal error: ObjID already in use"))
      {
        throw new IllegalStateException(ex);
      }
    }
  }

  protected Interface getDefaultInterface()
  {
    Interface apiInterface = super.getDefaultInterface();
    apiInterface.setName("CustomService");
    apiInterface.setType(InterfaceType.CUSTOM_XML);

    return apiInterface;
  }

  protected InterfaceMethod getDefaultInterfaceMethod()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("getRequestedData");

    return apiMethod;
  }

  protected void invokeCustomCall(String request, String assertion)
    throws Exception
  {
    invokeCustomCall(request, assertion, null, null);
  }

  protected void invokeCustomCall(String request, String assertion, String mockProfileName,
                                  Integer recordSessionID)
    throws Exception
  {
    request = "<action>" + request + "</action>";

    CustomRequestRequest req = new CustomRequestRequest();
    req.setInterfaceName("CustomService");
    req.setInterfaceMethod("getRequestedData");
    req.setRequest(request);

    CustomRequestResponse response = mosyClient.customRequest(req, mockProfileName, recordSessionID);

    String result = null;

    // Routing
    if (response.isRoute())
    {
      result = CustomRmiServiceStubSingleton.invoke(request);

      // save record
      if (response.isRecord())
      {
        Record apiRecord = new Record();
        apiRecord.setInterfaceMethod(response.getInterfaceMethod());
        apiRecord.setRequestData(request);
        apiRecord.setResponse(result);

        mosyClient.saveRecord(apiRecord);
      }
    }
    // Mock
    else
    {
      result = response.getResponse();
    }

    assertEquals("<return>" + assertion + "</return>", result);
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

    returnValue = "<return>" + returnValue + "</return>";

    super.addMockData(title, active, requestAction, returnValue, apiMockProfile, common);
  }

  protected void addRecordConfig(String title, boolean enabled, String requestAction)
  {
    requestAction = "<action>" + requestAction + "</action>";

    super.addRecordConfig(title, enabled, requestAction);
  }
}