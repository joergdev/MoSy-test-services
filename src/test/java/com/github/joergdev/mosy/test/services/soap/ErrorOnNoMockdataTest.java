package com.github.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import com.github.joergdev.mosy.api.model.MockSession;
import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

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
    apiBaseData.setMockActive(null);
    apiBaseData.setMockActiveOnStartup(null);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(null);
    apiInterface.setMockActiveOnStartup(null);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setRoutingOnNoMockData(false);
    apiMethod.setMockActive(true);
    apiMethod.setMockActiveOnStartup(true);
    apiMethod.setRecord(false);

    MockSession apiMockSession = mosyClient.createMocksession().getMockSession();

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD2_MS", true, "2", "ms_two_ms", apiMockSession);
  }
}