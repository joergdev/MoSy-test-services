package de.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import org.junit.Test;
import de.joergdev.mosy.test.services.SoapService;
import de.joergdev.mosy.test.services.soap.core.SoapServiceClientPortSingleton;

public class UnmockedTest
{
  @Test
  public void testUnmocked()
  {
    // Start the Service
    SoapService.main(null);

    try
    {
      SoapServiceClientPortSingleton.getInstance().setUrl("http://localhost:5432/soap");

      // testcase 1
      String response = SoapServiceClientPortSingleton.getInstance().invoke("1", null, null);

      assertEquals("one", response);

      // testcase 2
      response = SoapServiceClientPortSingleton.getInstance().invoke("2", null, null);

      assertEquals("two", response);

      // testcase 3
      response = SoapServiceClientPortSingleton.getInstance().invoke("3", null, null);

      assertEquals("three", response);

      // testcase 4
      response = SoapServiceClientPortSingleton.getInstance().invoke("4", null, null);

      assertEquals("four", response);

      // testcase 5
      response = SoapServiceClientPortSingleton.getInstance().invoke("5", null, null);

      assertEquals("five", response);

      // testcase 6
      response = SoapServiceClientPortSingleton.getInstance().invoke("E", null, null);

      assertEquals("!ERROR!", response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      fail(ex.getMessage());
    }
  }
}