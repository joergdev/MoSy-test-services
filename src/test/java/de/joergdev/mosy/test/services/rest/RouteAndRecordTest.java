package de.joergdev.mosy.test.services.rest;

import java.time.LocalDateTime;
import java.util.List;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.shared.Utils;
import de.joergdev.mosy.test.services.rest.core.AbstractRestServiceClientTest;
import de.joergdev.mosy.test.services.rest.model.Car;
import de.joergdev.mosy.test.services.rest.model.Subpart;

public class RouteAndRecordTest extends AbstractRestServiceClientTest
{
  @Override
  public void _runTest()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    invokeWsCall(apiMethodGET, 200,
        "{\"cars\":[{\"id\":1,\"type\":\"Audi\",\"age\":5},{\"id\":2,\"type\":\"VW\",\"age\":1},{\"id\":3,\"type\":\"BMW\",\"age\":10}]}");

    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "666")), 410, null);
    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "777")), 410, "Already deleted");
    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "123")), 202,
        "Deleted /api/cars/123");

    invokeWsCall(apiMethodPOST, new Car("Audi", 10), 200, Utils.object2Json(new Car(123, "Audi", 10)));

    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries( //
            Utils.mapEntry("id", "222"), //
            Utils.mapEntry("partid", "333"), //
            Utils.mapEntry("subpartid", "444")),
        new Subpart("subpartX", 7), 200, Utils.object2Json(new Subpart("subpartX", 7)));

    // check Records
    List<Record> records = checkRecordsSaved(ldtStart, 6);

    checkRecord(records, 0, null, null, 200, Utils.formatJSON(
        "{\"cars\":[{\"id\":1,\"type\":\"Audi\",\"age\":5},{\"id\":2,\"type\":\"VW\",\"age\":1},{\"id\":3,\"type\":\"BMW\",\"age\":10}]}",
        true));

    checkRecord(records, 1, Utils.mapOfEntries(Utils.mapEntry("id", "666")), null, 410, null);
    checkRecord(records, 2, Utils.mapOfEntries(Utils.mapEntry("id", "777")), null, 410, "Already deleted");
    checkRecord(records, 3, Utils.mapOfEntries(Utils.mapEntry("id", "123")), null, 202,
        "Deleted /api/cars/123");

    checkRecord(records, 4, null, Utils.formatJSON(Utils.object2Json(new Car("Audi", 10), false), true), 200,
        Utils.formatJSON(Utils.object2Json(new Car(123, "Audi", 10)), true));

    checkRecord(records, 5, Utils.mapOfEntries( //
        Utils.mapEntry("id", "222"), //
        Utils.mapEntry("partid", "333"), //
        Utils.mapEntry("subpartid", "444")), //
        Utils.formatJSON(Utils.object2Json(new Subpart("subpartX", 7), false), true), 200,
        Utils.formatJSON(Utils.object2Json(new Subpart("subpartX", 7)), true));
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(false);
    apiInterface.setMockActiveOnStartup(false);
    apiInterface.setRecord(true);
    apiInterface.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    setPropertiesMethod(apiMethodGET);
    setPropertiesMethod(apiMethodDELETE);
    setPropertiesMethod(apiMethodPOST);
    setPropertiesMethod(apiMethodPOSTSubparts);
  }

  private void setPropertiesMethod(InterfaceMethod apiMethodToConfigure)
  {
    apiMethodToConfigure.setMockActive(false);
    apiMethodToConfigure.setMockActiveOnStartup(false);
    apiMethodToConfigure.setRecord(true);
    apiMethodToConfigure.setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    apiBaseData.setMockActive(false);
    apiBaseData.setMockActiveOnStartup(false);
    apiBaseData.setRecord(true);
    apiBaseData.setRoutingOnNoMockData(true);
  }
}