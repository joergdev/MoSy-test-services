package de.joergdev.mosy.test.services.soap;

import java.time.LocalDateTime;
import de.joergdev.mosy.api.model.RecordSession;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class RecordGlobalTest extends AbstractSoapServiceClientTest
{
  private RecordSession rs1;
  private RecordSession rs2;

  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-2
    invokeWsCall("1", "one");
    invokeWsCall("2", "two");

    invokeWsCall("3", "three", null, rs1.getRecordSessionID());

    invokeWsCall("4", "four", null, rs2.getRecordSessionID());
    invokeWsCall("5", "five", null, rs2.getRecordSessionID());
    invokeWsCall("6", "??", null, rs2.getRecordSessionID());

    checkRecordsSaved(ldtStart, 6);
    checkRecordsSaved(ldtStart, 1, rs1.getRecordSessionID());
    checkRecordsSaved(ldtStart, 3, rs2.getRecordSessionID());
  }

  @Override
  protected void setPropertiesBaseData()
  {
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
    apiBaseData.setRecord(true);
    apiBaseData.setRoutingOnNoMockData(true);

    rs1 = addRecordSession();
    rs2 = addRecordSession();
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(true);
    apiInterface.setMockActiveOnStartup(true);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    apiMethod.setMockActive(true);
    apiMethod.setMockActiveOnStartup(true);
    apiMethod.setRecord(false);
    apiMethod.setRoutingOnNoMockData(true);
  }
}