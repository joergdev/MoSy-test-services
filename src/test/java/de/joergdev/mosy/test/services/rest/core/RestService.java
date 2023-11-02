package de.joergdev.mosy.test.services.rest.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import com.sun.net.httpserver.HttpExchange;
import de.joergdev.mosy.api.model.HttpMethod;
import de.joergdev.mosy.shared.Utils;
import de.joergdev.mosy.test.services.rest.model.Car;
import de.joergdev.mosy.test.services.rest.model.Cars;
import de.joergdev.mosy.test.services.rest.model.Subpart;

@SuppressWarnings("restriction")
public class RestService
{
  private static final Cars cars = initModel();

  public static void main(String[] args)
  {
    try
    {
      com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer
          .create(new InetSocketAddress(5432), 0);
      server.createContext("/", new com.sun.net.httpserver.HttpHandler()
      {
        @Override
        public void handle(HttpExchange exchange)
          throws IOException
        {
          URI uri = exchange.getRequestURI();
          String path = uri.getPath();

          String response = "??";
          int responseCode = HttpURLConnection.HTTP_OK;

          if (HttpMethod.GET.name().equals(exchange.getRequestMethod()))
          {
            // http://localhost:5432/api/cars
            if ("/api/cars".equals(path))
            {
              response = Utils.object2Json(cars);
            }
          }
          else if (HttpMethod.DELETE.name().equals(exchange.getRequestMethod()))
          {
            if (path.contains("cars/666"))
            {
              responseCode = HttpURLConnection.HTTP_GONE;
              response = "";
            }
            else if (path.contains("cars/777"))
            {
              responseCode = HttpURLConnection.HTTP_GONE;
              response = "Already deleted";
            }
            else
            {
              responseCode = HttpURLConnection.HTTP_ACCEPTED;
              response = "Deleted " + path;
            }
          }
          else if (HttpMethod.POST.name().equals(exchange.getRequestMethod()))
          {
            // /cars/{cid}/parts/{pid}/subparts{spid}
            if (path.contains("subparts"))
            {
              response = Utils.object2Json(new Subpart("subpartX", 7));
            }
            // /cars
            else
            {
              response = Utils.object2Json(new Car(123, "Audi", 10));
            }
          }

          exchange.sendResponseHeaders(responseCode, response.getBytes().length);

          OutputStream osResponseBody = exchange.getResponseBody();
          osResponseBody.write(response.getBytes());
          osResponseBody.close();
        }
      });

      server.setExecutor(null);
      server.start();
    }
    catch (Throwable th)
    {
      th.printStackTrace();
    }
  }

  private static Cars initModel()
  {
    Cars cars = new Cars();
    cars.getCars().add(new Car(1, "Audi", 5));
    cars.getCars().add(new Car(2, "VW", 1));
    cars.getCars().add(new Car(3, "BMW", 10));

    return cars;
  }
}