package de.joergdev.mosy.test.services.custom;

import static org.junit.Assert.*;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

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
    getApiBaseData().setMockActive(true);
    getApiBaseData().setMockActiveOnStartup(true);
    getApiBaseData().setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(null);
    getApiInterface().setMockActiveOnStartup(null);
    getApiInterface().setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setMockActive(false);
    getApiInterfaceMethod().setMockActiveOnStartup(false);
    getApiInterfaceMethod().setRecord(false);

    MockProfile apiMockProfile = createMockProfile("test", false);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD2_MS", true, "2", "ms_two_ms", apiMockProfile);
  }
}
