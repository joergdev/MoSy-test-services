package com.github.joergdev.mosy.test.services.soap;

import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockDisabledGlobalTest extends AbstractSoapServiceClientTest
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
    apiBaseData.setMockActive(false);
    apiBaseData.setMockActiveOnStartup(false);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockDisabled(false);
    apiInterface.setMockDisabledOnStartup(false);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setMockDisabled(false);
    apiMethod.setMockDisabledOnStartup(false);
    apiMethod.setRecord(false);
    apiMethod.setRoutingOnNoMockData(true);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD4", true, "4", "m_four_m");
    addMockData("MD-Others", true, null, "m_others_m");
  }
}