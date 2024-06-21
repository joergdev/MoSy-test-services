package de.joergdev.mosy.test.services.custom.core;

import static org.junit.Assert.*;
import java.rmi.RemoteException;
import java.util.Map;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.InterfaceType;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.api.request.mockservices.CustomRequestRequest;
import de.joergdev.mosy.api.response.mockservices.CustomRequestResponse;
import de.joergdev.mosy.test.services.AbstractServiceClientTest;
import de.joergdev.mosy.test.services.CustomRmiService;

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

  @Override
  protected Interface getDefaultInterface()
  {
    Interface apiInterface = super.getDefaultInterface();
    apiInterface.setName("CustomService");
    apiInterface.setType(InterfaceType.CUSTOM_XML);

    return apiInterface;
  }

  @Override
  protected InterfaceMethod getDefaultInterfaceMethod()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("getRequestedData");

    return apiMethod;
  }

  protected void invokeCustomCall(String request, String assertion)
    throws Exception
  {
    invokeCustomCall(request, assertion, null, null, null, null);
  }

  protected void invokeCustomCall(String request, String assertion, String mockProfileName, Integer recordSessionID, Long minDelay, Long maxDelay)
    throws Exception
  {
    request = "<action>" + request + "</action>";

    CustomRequestRequest req = new CustomRequestRequest();
    req.setInterfaceName("CustomService");
    req.setInterfaceMethod("getRequestedData");
    req.setRequest(request);

    long timeStart = System.currentTimeMillis();

    CustomRequestResponse response = mosyClient.customRequest(req, mockProfileName, recordSessionID);

    checkDelay(timeStart, minDelay, maxDelay);

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

  private void checkDelay(long timeStart, Long minDelay, Long maxDelay)
  {
    long diff = System.currentTimeMillis() - timeStart;

    if (minDelay != null && diff < minDelay)
    {
      fail("diff<minDelay; diff: " + diff + "; minDelay" + minDelay);
    }

    if (maxDelay != null && diff > maxDelay)
    {
      fail("diff > maxDelay; diff: " + diff + "; maxDelay" + maxDelay);
    }
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile)
  {
    addMockData(apiMethod, title, active, requestAction, returnValue, apiMockProfile, false, null, null, null, null);
  }

  @Override
  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile,
                             boolean common, Integer httpReturnCode, Map<String, String> pathParams, Map<String, String> urlArguments, Long delay)
  {
    if (requestAction != null)
    {
      requestAction = "<action>" + requestAction + "</action>";
    }

    returnValue = "<return>" + returnValue + "</return>";

    super.addMockData(apiMethodMd, title, active, requestAction, returnValue, apiMockProfile, common, httpReturnCode, pathParams, urlArguments, delay);
  }

  @Override
  protected void addRecordConfig(String title, boolean enabled, String requestAction)
  {
    requestAction = "<action>" + requestAction + "</action>";

    super.addRecordConfig(title, enabled, requestAction);
  }
}
