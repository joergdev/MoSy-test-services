package de.joergdev.mosy.test.services.custom;

import de.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

public class MockByMockdataTest extends AbstractCustomServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active matching
    invokeCustomCall("1", "m_one_m");

    // Mockdata matching but INACTIVE, method global mockdata matching
    invokeCustomCall("2", "m_others_m");
    invokeCustomCall("3", "m_others_m");

    // Mockdata active matching
    invokeCustomCall("4", "m_four_m", null, null, null, 300L);

    // Mockdata active matching with delay 1.000ms
    invokeCustomCall("4_delay", "m_four_m_delay", null, null, 1000L, 1300L);
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
    apiInterface.setMockActive(true);
    apiInterface.setMockActiveOnStartup(false);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setRoutingOnNoMockData(false);
    apiMethod.setMockActive(false);
    apiMethod.setMockActiveOnStartup(false);
    apiMethod.setRecord(false);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD3", false, "3", "m_three_m");
    addMockData("MD4", true, "4", "m_four_m");
    addMockData(apiMethod, "MD4_delay", true, "4_delay", "m_four_m_delay", null, false, null, null, null,
        1000L);
    addMockData("MD-Others", true, null, "m_others_m");
  }
}