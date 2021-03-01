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
    apiBaseData.setMockActive(false);
    apiBaseData.setMockActiveOnStartup(false);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(true);
    apiInterface.setMockActiveOnStartup(true);
    apiInterface.setRecord(true);
    apiInterface.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setMockActive(true);
    apiMethod.setMockActiveOnStartup(true);
    apiMethod.setRecord(false);
    apiMethod.setRoutingOnNoMockData(true);

    addRecordConfig("RC1", false, "1");
  }
}