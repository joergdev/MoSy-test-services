package de.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class NoRecordTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-2
    invokeWsCall("1", "one");
    invokeWsCall("2", "two");

    checkRecordsSaved(ldtStart, 0);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(false);
    getApiBaseData().setMockActiveOnStartup(false);
    getApiBaseData().setRecord(false);
    getApiBaseData().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(true);
    getApiInterface().setMockActiveOnStartup(true);
    getApiInterface().setRecord(true);
    getApiInterface().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setMockActive(true);
    getApiInterfaceMethod().setMockActiveOnStartup(true);
    getApiInterfaceMethod().setRecord(false);
    getApiInterfaceMethod().setRoutingOnNoMockData(true);

    addRecordConfig("RC1", false, "1");
  }
}
