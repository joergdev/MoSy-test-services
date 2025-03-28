package de.joergdev.mosy.test.services.rest.core;

import java.util.Map;
import java.util.StringTokenizer;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import de.joergdev.mosy.api.APIConstants;
import de.joergdev.mosy.api.model.HttpMethod;
import de.joergdev.mosy.test.services.rest.model.TestableModel;

public class RestServiceClientPortSingleton
{
  private static RestServiceClientPortSingleton instance = null;

  private String baseUrl;

  private RestServiceClientPortSingleton()
  {

  }

  public static RestServiceClientPortSingleton getInstance()
  {
    if (instance == null)
    {
      instance = new RestServiceClientPortSingleton();
    }

    return instance;
  }

  public void setBaseUrl(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  public Response invoke(HttpMethod httpMethod, String path, TestableModel model, String mockProfileName,
                         Integer recordSessionID, Map<String, Object> queryParams)
    throws Exception
  {
    Client client = ClientBuilder.newClient();

    // Build endppoint (WebTarget)
    WebTarget webTarget = client.target(baseUrl);

    StringTokenizer tok = new StringTokenizer(path, "/");
    while (tok.hasMoreTokens())
    {
      webTarget = webTarget.path(tok.nextToken());
    }

    // Query Params
    if (queryParams != null)
    {
      for (String queryParamKey : queryParams.keySet())
      {
        Object queryParamValue = queryParams.get(queryParamKey);

        if (queryParamValue != null)
        {
          webTarget = webTarget.queryParam(queryParamKey, queryParamValue);
        }
      }
    }

    Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

    // set mockProfileID
    if (mockProfileName != null)
    {
      invocationBuilder.header(APIConstants.HTTP_HEADER_MOCK_PROFILE_NAME, mockProfileName);
    }

    // set recordSessionID
    if (recordSessionID != null)
    {
      invocationBuilder.header(APIConstants.HTTP_HEADER_RECORD_SESSION_ID, String.valueOf(recordSessionID));
    }

    // invoke
    Response response = invokeAndGetResponse(httpMethod, model, invocationBuilder,
        webTarget.getUri().toString());

    return response;
  }

  private Response invokeAndGetResponse(HttpMethod method, Object entity,
                                        Invocation.Builder invocationBuilder, String endpoint)
  {
    Response response = null;

    long timeStart = System.currentTimeMillis();

    if (HttpMethod.GET.equals(method))
    {
      response = invocationBuilder.get();
    }
    else if (HttpMethod.PUT.equals(method))
    {
      response = invocationBuilder.put(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }
    else if (HttpMethod.POST.equals(method))
    {
      response = invocationBuilder.post(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }
    else if (HttpMethod.DELETE.equals(method))
    {
      response = invocationBuilder.delete();
    }

    System.out
        .println("Invoked API-Call " + endpoint + "; time (ms): " + (System.currentTimeMillis() - timeStart));

    return response;
  }
}