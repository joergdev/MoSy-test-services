package de.joergdev.mosy.test.services.soap;

import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockDisabledMethodGlobalTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    invokeWsCall("1", "one");
    invokeWsCall("2", "two");
    invokeWsCall("3", "three");
    invokeWsCall("4", "four");
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(null);
    getApiBaseData().setMockActiveOnStartup(null);
    getApiBaseData().setRecord(false);
    getApiBaseData().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(null);
    getApiInterface().setMockActiveOnStartup(null);
    getApiInterface().setRecord(false);
    getApiInterface().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setMockActive(false);
    getApiInterfaceMethod().setMockActiveOnStartup(false);
    getApiInterfaceMethod().setRecord(false);
    getApiInterfaceMethod().setRoutingOnNoMockData(true);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD4", true, "4", "m_four_m");
    addMockData("MD-Others", true, null, "m_others_m");
  }
}
