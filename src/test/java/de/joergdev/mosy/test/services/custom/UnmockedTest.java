package de.joergdev.mosy.test.services.custom;

import static org.junit.Assert.*;
import java.rmi.RemoteException;
import org.junit.Test;
import de.joergdev.mosy.test.services.CustomRmiService;
import de.joergdev.mosy.test.services.custom.core.CustomRmiServiceStubSingleton;

public class UnmockedTest
{
  @Test
  public void test()
  {
    try
    {
      try
      { // Start the Service
        CustomRmiService.main(null);
      }
      catch (RemoteException ex)
      {
        if (!ex.getMessage().contains("internal error: ObjID already in use"))
        {
          throw ex;
        }
      }

      // testcase 1
      String response = CustomRmiServiceStubSingleton.invoke("<action>1</action>");

      assertEquals("<return>one</return>", response);

      // testcase 2
      response = CustomRmiServiceStubSingleton.invoke("<action>2</action>");

      assertEquals("<return>two</return>", response);

      // testcase 3
      response = CustomRmiServiceStubSingleton.invoke("<action>3</action>");

      assertEquals("<return>three</return>", response);

      // testcase 4
      response = CustomRmiServiceStubSingleton.invoke("<action>4</action>");

      assertEquals("<return>four</return>", response);

      // testcase 5
      response = CustomRmiServiceStubSingleton.invoke("<action>5</action>");

      assertEquals("<return>five</return>", response);

      // testcase 6
      response = CustomRmiServiceStubSingleton.invoke("<action>E</action>");

      assertEquals("<return>!ERROR!</return>", response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      fail(ex.getMessage());
    }
  }
}