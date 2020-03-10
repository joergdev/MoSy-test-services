package com.github.joergdev.mosy.test.services.custom;

import java.time.LocalDateTime;
import com.github.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

public class RouteAndRecordInterfaceGlobalTest extends AbstractCustomServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-6
    invokeCustomCall("1", "one");
    invokeCustomCall("2", "two");
    invokeCustomCall("3", "three");
    invokeCustomCall("4", "four");
    invokeCustomCall("5", "five");
    invokeCustomCall("E", "!ERROR!");

    checkRecordsSaved(ldtStart, 6);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockDisabled(true);
    apiInterface.setMockDisabledOnStartup(true);
    apiInterface.setRecord(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
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
  }
}