package de.joergdev.mosy.test.services.soap;

import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

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
    addMockData("MD3", false, "3", "m_three_m");
    addMockData("MD4", true, "4", "m_four_m");
    addMockData("MD-Others", true, null, "m_others_m");
  }
}
