package de.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class ErrorOnNoMockdataTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active matching
    invokeWsCall("1", "m_one_m");

    // second call has to fail, there are 2 mockdata but first ist inactive and second is related to mockSession
    Exception failOnNoMockData = null;

    try
    {
      invokeWsCall("2", null);
    }
    catch (Exception ex)
    {
      failOnNoMockData = ex;
    }

    assertNotNull(failOnNoMockData);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(null);
    getApiBaseData().setMockActiveOnStartup(null);
    getApiBaseData().setRecord(false);
    getApiBaseData().setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(null);
    getApiInterface().setMockActiveOnStartup(null);
    getApiInterface().setRecord(false);
    getApiInterface().setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setRoutingOnNoMockData(false);
    getApiInterfaceMethod().setMockActive(true);
    getApiInterfaceMethod().setMockActiveOnStartup(true);
    getApiInterfaceMethod().setRecord(false);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD2_MP", true, "2", "mp_two_mp", createMockProfile("test1", false));
  }
}
