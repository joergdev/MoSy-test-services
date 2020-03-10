package com.github.joergdev.mosy.test.services.custom;

import static org.junit.Assert.*;
import com.github.joergdev.mosy.api.model.MockSession;
import com.github.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

public class ErrorOnNoMockdataTest extends AbstractCustomServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active matching
    invokeCustomCall("1", "m_one_m");

    // second call has to fail, there are 2 mockdata but first ist inactive and second is related to mockSession
    Exception failOnNoMockData = null;

    try
    {
      invokeCustomCall("2", null);
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
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
    apiBaseData.setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockDisabled(false);
    apiInterface.setMockDisabledOnStartup(false);
    apiInterface.setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setMockDisabled(false);
    apiMethod.setMockDisabledOnStartup(false);
    apiMethod.setRecord(false);

    MockSession apiMockSession = mosyClient.createMocksession().getMockSession();

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD2_MS", true, "2", "ms_two_ms", apiMockSession);
  }
}