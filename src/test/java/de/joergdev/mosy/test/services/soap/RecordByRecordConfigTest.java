package de.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.List;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class RecordByRecordConfigTest extends AbstractSoapServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-3
    invokeWsCall("1", "one");
    invokeWsCall("2", "two");
    invokeWsCall("3", "three");

    List<Record> records = checkRecordsSaved(ldtStart, 1);

    Record rec = getApiClient().loadRecord(records.get(0).getRecordId()).getRecord();
    assertTrue(rec.getRequestData().contains("2") && rec.getResponse().contains("two"));
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(false);
    getApiBaseData().setMockActiveOnStartup(false);
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
    getApiInterfaceMethod().setRecord(null);
    getApiInterfaceMethod().setRoutingOnNoMockData(true);

    addRecordConfig("RC1", false, "1");
    addRecordConfig("RC2", true, "2");
  }
}
