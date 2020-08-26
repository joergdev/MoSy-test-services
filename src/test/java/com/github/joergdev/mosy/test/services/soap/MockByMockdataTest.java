package com.github.joergdev.mosy.test.services.soap;

import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockByMockdataTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active matching
    invokeWsCall("1", "m_one_m");

    // Mockdata matching but INACTIVE, method global mockdata matching
    invokeWsCall("2", "m_others_m");
    invokeWsCall("3", "m_others_m");

    // Mockdata active matching
    invokeWsCall("4", "m_four_m");
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

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD2", false, "2", "m_two_m");
    addMockData("MD3", false, "3", "m_three_m");
    addMockData("MD4", true, "4", "m_four_m");
    addMockData("MD-Others", true, null, "m_others_m");
  }
}