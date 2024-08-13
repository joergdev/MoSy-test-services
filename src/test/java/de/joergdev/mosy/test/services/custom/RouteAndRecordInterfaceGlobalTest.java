package de.joergdev.mosy.test.services.custom;

import java.time.LocalDateTime;
import de.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

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
    getApiInterface().setMockActive(null);
    getApiInterface().setMockActiveOnStartup(null);
    getApiInterface().setRecord(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setMockActive(false);
    getApiInterfaceMethod().setMockActiveOnStartup(false);
    getApiInterfaceMethod().setRecord(false);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(null);
    getApiBaseData().setMockActiveOnStartup(null);
    getApiBaseData().setRecord(null);
  }
}
