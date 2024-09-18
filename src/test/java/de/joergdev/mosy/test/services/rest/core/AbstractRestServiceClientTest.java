package de.joergdev.mosy.test.services.rest.core;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.Map.Entry;
import javax.ws.rs.core.Response;
import de.joergdev.mosy.api.client.Resources;
import de.joergdev.mosy.api.model.HttpMethod;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.InterfaceType;
import de.joergdev.mosy.shared.Utils;
import de.joergdev.mosy.test.services.AbstractServiceClientTest;
import de.joergdev.mosy.test.services.rest.model.TestableModel;

public abstract class AbstractRestServiceClientTest extends AbstractServiceClientTest
{
  protected InterfaceMethod apiMethodPUT = getDefaultInterfaceMethodPUT();
  protected InterfaceMethod apiMethodPOST = getDefaultInterfaceMethodPOST();
  protected InterfaceMethod apiMethodDELETE = getDefaultInterfaceMethodDELETE();

  /** cars/{id}/parts/{partid}/subparts/{subpartid} */
  protected InterfaceMethod apiMethodPOSTSubparts = getDefaultInterfaceMethodPOSTSubparts();

  /** cars/{id} */
  protected InterfaceMethod apiMethodPOSTCar = getDefaultInterfaceMethodPOSTCar();

  @Override
  protected void _before()
  {
    // Start service
    RestService.main(null);

    // set endpoint to mosy
    RestServiceClientPortSingleton.getInstance().setBaseUrl(Resources.getApiEndpoint() + "/mock-services/rest/localhost:5432/api");

    // save additional methods
    apiMethodPUT = assureInterfaceMethodExists(apiMethodPUT);
    apiMethodPOST = assureInterfaceMethodExists(apiMethodPOST);
    apiMethodDELETE = assureInterfaceMethodExists(apiMethodDELETE);
    apiMethodPOSTSubparts = assureInterfaceMethodExists(apiMethodPOSTSubparts);
    apiMethodPOSTCar = assureInterfaceMethodExists(apiMethodPOSTCar);
  }

  public InterfaceMethod getApiMethodGET()
  {
    return getApiInterfaceMethod();
  }

  @Override
  protected Interface getDefaultInterface()
  {
    Interface apiInterface = super.getDefaultInterface();
    apiInterface.setName("RestAPI");
    apiInterface.setType(InterfaceType.REST);
    apiInterface.setRoutingUrl("http://localhost:5432/api");
    apiInterface.setServicePath("localhost:5432/api");

    return apiInterface;
  }

  /**
   * return GET Method cars
   */
  @Override
  protected InterfaceMethod getDefaultInterfaceMethod()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("Get cars");
    apiMethod.setServicePath("cars");
    apiMethod.setHttpMethod(HttpMethod.GET);

    return apiMethod;
  }

  protected InterfaceMethod getDefaultInterfaceMethodPUT()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("put car");
    apiMethod.setServicePath("cars");
    apiMethod.setHttpMethod(HttpMethod.PUT);

    return apiMethod;
  }

  protected InterfaceMethod getDefaultInterfaceMethodPOST()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("post car");
    apiMethod.setServicePath("cars");
    apiMethod.setHttpMethod(HttpMethod.POST);

    return apiMethod;
  }

  protected InterfaceMethod getDefaultInterfaceMethodPOSTSubparts()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("post car subpart");
    apiMethod.setServicePath("cars/{id}/parts/{partid}/subparts/{subpartid}");
    apiMethod.setHttpMethod(HttpMethod.POST);

    return apiMethod;
  }

  private InterfaceMethod getDefaultInterfaceMethodPOSTCar()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("post car with id");
    apiMethod.setServicePath("cars/{id}");
    apiMethod.setHttpMethod(HttpMethod.POST);

    return apiMethod;
  }

  protected InterfaceMethod getDefaultInterfaceMethodDELETE()
  {
    InterfaceMethod apiMethod = super.getDefaultInterfaceMethod();
    apiMethod.setName("delete car");
    apiMethod.setServicePath("cars/{id}");
    apiMethod.setHttpMethod(HttpMethod.DELETE);

    return apiMethod;
  }

  protected void invokeWsCall(InterfaceMethod method, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, (TestableModel) null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Integer assertedHttpStatus, String assertion, Map<String, Object> urlArguments)
    throws Exception
  {
    invokeWsCall(method, null, urlArguments, null, null, null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Integer assertedHttpStatus, String mockProfileName, String assertion)
    throws Exception
  {
    invokeWsCall(method, null, mockProfileName, null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, TestableModel model, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, model, null, null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, String mockProfileName, Integer recordSessionID, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, null, mockProfileName, recordSessionID, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, TestableModel model, String mockProfileName, Integer recordSessionID, Integer assertedHttpStatus,
                              String assertion)
    throws Exception
  {
    invokeWsCall(method, null, model, mockProfileName, recordSessionID, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Map<String, String> pathParams, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, pathParams, null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Map<String, String> pathParams, TestableModel model, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, pathParams, model, null, null, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Map<String, String> pathParams, TestableModel model, String mockProfileName, Integer recordSessionID,
                              Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    invokeWsCall(method, pathParams, null, model, mockProfileName, recordSessionID, assertedHttpStatus, assertion);
  }

  protected void invokeWsCall(InterfaceMethod method, Map<String, String> pathParams, Map<String, Object> urlArguments, TestableModel model,
                              String mockProfileName, Integer recordSessionID, Integer assertedHttpStatus, String assertion)
    throws Exception
  {
    String urlPath = method.getServicePath();

    if (pathParams != null)
    {
      for (Entry<String, String> pathParam : pathParams.entrySet())
      {
        urlPath = urlPath.replace("{" + pathParam.getKey() + "}", pathParam.getValue());
      }
    }

    Response result = RestServiceClientPortSingleton.getInstance().invoke(method.getHttpMethod(), urlPath, model, mockProfileName, recordSessionID,
        urlArguments);

    assertEquals(assertedHttpStatus, Integer.valueOf(result.getStatus()));

    if (!Utils.isEmpty(assertion))
    {
      assertEquals(Utils.formatJSON(assertion, false), Utils.formatJSON(result.readEntity(String.class), false));
    }
  }
}
