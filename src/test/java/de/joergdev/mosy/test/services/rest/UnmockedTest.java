package de.joergdev.mosy.test.services.rest;

import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.util.Map;
import jakarta.ws.rs.core.Response;
import org.junit.Test;
import de.joergdev.mosy.api.model.HttpMethod;
import de.joergdev.mosy.shared.Utils;
import de.joergdev.mosy.test.services.rest.core.RestService;
import de.joergdev.mosy.test.services.rest.core.RestServiceClientPortSingleton;
import de.joergdev.mosy.test.services.rest.model.Car;
import de.joergdev.mosy.test.services.rest.model.Subpart;
import de.joergdev.mosy.test.services.rest.model.TestableModel;

public class UnmockedTest
{
  @Test
  public void testUnmocked()
  {
    // Start the Service
    RestService.main(null);

    try
    {
      RestServiceClientPortSingleton.getInstance().setBaseUrl("http://localhost:5432/api");

      // GET
      Response response = invoke(HttpMethod.GET, "cars", null, null);
      assertEquals(200, response.getStatus());
      assertEquals(
          "{\"cars\":[{\"id\":1,\"type\":\"Audi\",\"age\":5},{\"id\":2,\"type\":\"VW\",\"age\":1},{\"id\":3,\"type\":\"BMW\",\"age\":10}]}",
          response.readEntity(String.class));

      // DELETE 410
      response = invoke(HttpMethod.DELETE, "cars/666", null, null);
      assertEquals(HttpURLConnection.HTTP_GONE, response.getStatus());

      // DELETE 202
      response = invoke(HttpMethod.DELETE, "cars/123", null, null);
      assertEquals(HttpURLConnection.HTTP_ACCEPTED, response.getStatus());
      assertEquals("Deleted /api/cars/123", response.readEntity(String.class));

      // POST OK 1
      response = invoke(HttpMethod.POST, "cars", new Car("Audi", 10), null);
      assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
      assertEquals(Utils.object2Json(new Car(123, "Audi", 10)), response.readEntity(String.class));

      // POST OK 2
      response = invoke(HttpMethod.POST, "cars/222/parts/333/subparts/444", new Subpart("subpartX", 7), null);
      assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
      assertEquals(Utils.object2Json(new Subpart("subpartX", 7)), response.readEntity(String.class));
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      fail(ex.getMessage());
    }
  }

  private Response invoke(HttpMethod httpMethod, String path, TestableModel model,
                          Map<String, Object> queryParams)
    throws Exception
  {
    return RestServiceClientPortSingleton.getInstance().invoke(httpMethod, path, model, null, null,
        queryParams);
  }
}