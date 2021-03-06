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
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
    apiBaseData.setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(null);
    apiInterface.setMockActiveOnStartup(null);
    apiInterface.setRecord(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setMockActive(false);
    apiMethod.setMockActiveOnStartup(false);
    apiMethod.setRecord(false);

    MockProfile apiMockProfile = createMockProfile("test", false);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD2_MS", true, "2", "ms_two_ms", apiMockProfile);
  }
}