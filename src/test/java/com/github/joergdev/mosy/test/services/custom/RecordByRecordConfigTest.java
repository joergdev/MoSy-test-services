package com.github.joergdev.mosy.test.services.custom;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.List;
import com.github.joergdev.mosy.api.model.Record;
import com.github.joergdev.mosy.test.services.custom.core.AbstractCustomServiceClientTest;

public class RecordByRecordConfigTest extends AbstractCustomServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // testcases 1-3
    invokeCustomCall("1", "one");
    invokeCustomCall("2", "two");
    invokeCustomCall("3", "three");

    List<Record> records = checkRecordsSaved(ldtStart, 1);

    Record rec = mosyClient.loadRecord(records.get(0).getRecordId()).getRecord();
    assertTrue(rec.getRequestData().contains("2") && rec.getResponse().contains("two"));
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
    apiMethod.setRecord(false);
    apiMethod.setRoutingOnNoMockData(true);

    addRecordConfig("RC1", false, "1");
    addRecordConfig("RC2", true, "2");
  }
}