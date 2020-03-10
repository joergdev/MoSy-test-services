package com.github.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class RouteAndRecordInterfaceGlobalTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-6
    invokeWsCall("1", "one");
    invokeWsCall("2", "two");
    invokeWsCall("3", "three");
    invokeWsCall("4", "four");
    invokeWsCall("5", "five");
    invokeWsCall("E", "!ERROR!");

    checkRecordsSaved(ldtStart, 6);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockDisabled(true);
    apiInterface.setMockDisabledOnStartup(true);
    apiInterface.setRecord(true);
    apiInterface.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setRoutingOnNoMockData(false);
    apiMethod.setMockDisabled(true);
    apiMethod.setMockDisabledOnStartup(true);
    apiMethod.setRecord(false);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(false);
  }
}