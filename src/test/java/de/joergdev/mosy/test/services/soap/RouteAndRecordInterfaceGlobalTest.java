package de.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

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
    apiInterface.setMockActive(null);
    apiInterface.setMockActiveOnStartup(null);
    apiInterface.setRecord(null);
    apiInterface.setRoutingOnNoMockData(null);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setRoutingOnNoMockData(true);
    apiMethod.setMockActive(false);
    apiMethod.setMockActiveOnStartup(false);
    apiMethod.setRecord(true);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    apiBaseData.setMockActive(null);
    apiBaseData.setMockActiveOnStartup(null);
    apiBaseData.setRecord(null);
    apiBaseData.setRoutingOnNoMockData(null);
  }
}