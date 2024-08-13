package de.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class RecordMethodGlobalTest extends AbstractSoapServiceClientTest
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

    checkRecordsSaved(ldtStart, 3);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(true);
    getApiBaseData().setMockActiveOnStartup(true);
    getApiBaseData().setRecord(null);
    getApiBaseData().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(true);
    getApiInterface().setMockActiveOnStartup(true);
    getApiInterface().setRecord(null);
    getApiInterface().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setMockActive(true);
    getApiInterfaceMethod().setMockActiveOnStartup(true);
    getApiInterfaceMethod().setRecord(true);
    getApiInterfaceMethod().setRoutingOnNoMockData(true);
  }

}
