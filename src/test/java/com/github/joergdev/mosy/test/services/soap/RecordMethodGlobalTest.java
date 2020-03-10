package com.github.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

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
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
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
    apiMethod.setRecord(true);
    apiMethod.setRoutingOnNoMockData(true);
  }

}
