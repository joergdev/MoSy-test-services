package de.joergdev.mosy.test.services.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.shared.Utils;
import de.joergdev.mosy.test.services.rest.core.AbstractRestServiceClientTest;
import de.joergdev.mosy.test.services.rest.model.Car;
import de.joergdev.mosy.test.services.rest.model.Subpart;

public class MockByMockdataTest extends AbstractRestServiceClientTest
{
  private static final String MOCK_RESULT_GET_CARS = "{\"cars\":[{\"type\":\"Audi\",\"age\":9},{\"type\":\"VW\",\"age\":8},{\"type\":\"BMW\",\"age\":7}]}";
  private static final String MOCK_RESULT_GET_CARS_DEV = "{\"cars\":[{\"type\":\"Audi\",\"age\":9},{\"type\":\"VW\",\"age\":8},{\"type\":\"BMW_dev\",\"age\":7}]}";
  private static final String MOCK_RESULT_GET_CARS_TEST = "{\"cars\":[{\"type\":\"Audi\",\"age\":9},{\"type\":\"VW\",\"age\":8},{\"type\":\"BMW_test\",\"age\":7}]}";
  private static final String MOCK_RESULT_GET_CARS_TEST_AGE5 = "{\"cars\":[{\"type\":\"Audi\",\"age\":5},{\"type\":\"VW\",\"age\":5},{\"type\":\"BMW_test\",\"age\":5}]}";

  private static final String MOCK_RESULT_PUT_CAR_123 = "{\"id\":123,\"type\":\"Fiat\",\"age\":5}";
  private static final String MOCK_RESULT_PUT_CAR_1234 = "{\"id\":1234,\"type\":\"Fiat\",\"age\":6}";

  private static final String MOCK_RESULT_POST_CAR_PORSCHE = "{\"id\":2224,\"type\":\"Porsche\",\"age\":20}";
  private static final String MOCK_RESULT_POST_CAR_OPEL = "{\"id\":2225,\"type\":\"Opel\",\"age\":18}";

  @Override
  public void _runTest()
    throws Exception
  {
    // GET 200
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS);

    // GET 200 - urlArg stage=dev
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS_DEV,
        Utils.mapOfEntries(Utils.mapEntry("stage", "dev")));

    // GET 200 - urlArg stage=test
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS_TEST,
        Utils.mapOfEntries(Utils.mapEntry("stage", "test")));

    // GET 200 - urlArg stage=test&age=5
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS_TEST_AGE5,
        Utils.mapOfEntries(Utils.mapEntry("stage", "test"), Utils.mapEntry("age", "5")));

    // GET 200 - urlArg stage=nonexists => no match for urlArg, may return result for get common
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS,
        Utils.mapOfEntries(Utils.mapEntry("stage", "nonexists")));

    // GET 200 - urlArg stage=test&age=6 => no match for urlArg age=6, may return result for get with urlArg stage=test
    invokeWsCall(apiMethod, 200, MOCK_RESULT_GET_CARS_TEST,
        Utils.mapOfEntries(Utils.mapEntry("stage", "test"), Utils.mapEntry("age", "6")));

    // GET 404
    invokeWsCall(apiMethod, 404, "MP1", (String) null);

    // PUT 201
    invokeWsCall(apiMethodPUT, new Car(123, "Fiat alt", 5), 201, MOCK_RESULT_PUT_CAR_123);
    invokeWsCall(apiMethodPUT, new Car(1234, "Fiat alt", 6), 201, MOCK_RESULT_PUT_CAR_1234);

    // POST 201
    invokeWsCall(apiMethodPOST, new Car("Porsche", 20), 201, MOCK_RESULT_POST_CAR_PORSCHE);
    invokeWsCall(apiMethodPOST, new Car("Opel", 18), 201, MOCK_RESULT_POST_CAR_OPEL);

    // POST 204
    invokeWsCall(apiMethodPOST, new Car("NoContentCar", 11), 204, null);

    // DELETE
    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "3334")), 204, null);
    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "3335")), 200, null);
    // common mock (no path params)
    invokeWsCall(apiMethodDELETE, Utils.mapOfEntries(Utils.mapEntry("id", "3336")), 202, null);

    // POST - path params and request matching 1:1
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "1"), //
            Utils.mapEntry("partid", "100"), //
            Utils.mapEntry("subpartid", "1000")), //
        new Subpart(1000, "Doorwindow", 2), 200, //
        Utils.object2Json(new Subpart(1000, "Doorwindow", 3)));

    // negative test => no mockdata with id = 2
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "2"), //
            Utils.mapEntry("partid", "100"), //
            Utils.mapEntry("subpartid", "1000")), //
        new Subpart(1000, "Doorwindow", 2), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":1000,\"name\":\"Doorwindow\",\"version\":2}]");

    // negative test => no mockdata with subpart name = NonExists
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "1"), //
            Utils.mapEntry("partid", "100"), //
            Utils.mapEntry("subpartid", "1000")), //
        new Subpart(1000, "NonExists", 2), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":1000,\"name\":\"NonExists\",\"version\":2}]");

    // POST - path params match 1:1, request machting partiell (mockdata without version)
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "2"), //
            Utils.mapEntry("partid", "200"), //
            Utils.mapEntry("subpartid", "2000")), //
        new Subpart(2000, "Seatcushion", 999), 200, //
        Utils.object2Json(new Subpart(2000, "Seatcushion", 11)));

    // negative test => no mockdata for request SeatWhatEverNotExisting
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "2"), //
            Utils.mapEntry("partid", "200"), //
            Utils.mapEntry("subpartid", "2000")), //
        new Subpart(2000, "SeatWhatEverNotExisting", 999), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":2000,\"name\":\"SeatWhatEverNotExisting\",\"version\":999}]");

    // POST - path params match 1:1, no request defined in mockdata
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "3"), //
            Utils.mapEntry("partid", "300"), //
            Utils.mapEntry("subpartid", "3000")), //
        new Subpart(123, "WhatEver", 456), 200, //
        Utils.object2Json(new Subpart(3000, "steeringWheelAirbag", 5)));

    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "3"), //
            Utils.mapEntry("partid", "300"), //
            Utils.mapEntry("subpartid", "3000")), //
        null, 200, //
        Utils.object2Json(new Subpart(3000, "steeringWheelAirbag", 5)));

    // POST - path params in mockdata not defined, request match 1:1
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "8"), //
            Utils.mapEntry("partid", "9"), //
            Utils.mapEntry("subpartid", "10")), //
        new Subpart(4000, "WindowRegulator", 4), 200, //
        Utils.object2Json(new Subpart(4000, "WindowRegulator", 5)));

    // negative test => no mockdata for version 5
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "8"), //
            Utils.mapEntry("partid", "9"), //
            Utils.mapEntry("subpartid", "10")), //
        new Subpart(4000, "WindowRegulator", 5), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":4000,\"name\":\"WindowRegulator\",\"version\":5}]");

    // POST - path param match partiell (partid), request match 1:1
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "789"), //
            Utils.mapEntry("partid", "500"), //
            Utils.mapEntry("subpartid", "9999")), //
        new Subpart(5000, "BrakeLight", 123456), 200, //
        Utils.object2Json(new Subpart(5000, "BrakeLight", 1234567)));

    // negative test => no mockdata for partid 501
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "789"), //
            Utils.mapEntry("partid", "501"), //
            Utils.mapEntry("subpartid", "9999")), //
        new Subpart(5000, "BrakeLight", 123456), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":5000,\"name\":\"BrakeLight\",\"version\":123456}]");

    // negative test => no mockdata for subpart NonExisting
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "789"), //
            Utils.mapEntry("partid", "500"), //
            Utils.mapEntry("subpartid", "9999")), //
        new Subpart(5000, "NonExisting", 123456), 500, //
        "[ERROR] - 7 - Operation failed [no mockdata for interface RestAPI, method post car subpart, request {\"id\":5000,\"name\":\"NonExisting\",\"version\":123456}]");

    // POST - path param match partiell (id+partid), request match 1:1
    invokeWsCall(apiMethodPOSTSubparts, //
        Utils.mapOfEntries(Utils.mapEntry("id", "6"), //
            Utils.mapEntry("partid", "600"), //
            Utils.mapEntry("subpartid", "54321")), //
        new Subpart(6000, "V-belt", 112), 200, //
        Utils.object2Json(new Subpart(6000, "V-belt", 113)));

  }

  @Override
  protected void setPropertiesBaseData()
  {
    apiBaseData.setMockActive(null);
    apiBaseData.setMockActiveOnStartup(null);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    apiInterface.setMockActive(null);
    apiInterface.setMockActiveOnStartup(null);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(false);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    // mock is active for all methods

    addMockData(apiMethod, "MD1_get", true, null, MOCK_RESULT_GET_CARS, 200);

    addMockData(apiMethod, "MD1_get_urlArg_stageDev", true, null, MOCK_RESULT_GET_CARS_DEV, null, true, 200,
        null, Utils.mapOfEntries(Utils.mapEntry("stage", "dev")));

    addMockData(apiMethod, "MD1_get_urlArg_stageTest", true, null, MOCK_RESULT_GET_CARS_TEST, null, true, 200,
        null, Utils.mapOfEntries(Utils.mapEntry("stage", "test")));

    addMockData(apiMethod, "MD1_get_urlArg_stageTest_age5", true, null, MOCK_RESULT_GET_CARS_TEST_AGE5, null,
        true, 200, null, Utils.mapOfEntries(Utils.mapEntry("stage", "test"), Utils.mapEntry("age", "5")));

    MockProfile mp1 = createMockProfile("MP1", false);
    addMockData(apiMethod, "MD1_get_404", true, null, null, mp1, false, 404);

    addMockData(apiMethodPUT, "MD2_put", true, "{\"id\":123,\"type\":\"Fiat alt\",\"age\":5}",
        MOCK_RESULT_PUT_CAR_123, 201);
    addMockData(apiMethodPUT, "MD3_put", true,
        Utils.formatJSON("{\"id\":1234,\"type\":\"Fiat alt\",\"age\":6}", true),
        Utils.formatJSON(MOCK_RESULT_PUT_CAR_1234, true), 201);

    addMockData(apiMethodPOST, "MD4_post", true, "{\"id\":null,\"type\":\"Porsche\",\"age\":20}",
        MOCK_RESULT_POST_CAR_PORSCHE, 201);
    addMockData(apiMethodPOST, "MD5_post", true, "{\"type\":\"Opel\",\"age\":18}", MOCK_RESULT_POST_CAR_OPEL,
        201);

    addMockData(apiMethodPOST, "MD6_post", true, "{\"type\":\"NoContentCar\",\"age\":11}", null, 204);

    addMockData(apiMethodDELETE, "MD7_DELETE", true, null, null, 204,
        Utils.mapOfEntries(Utils.mapEntry("id", "3334")));
    addMockData(apiMethodDELETE, "MD8_DELETE", true, null, null, 200,
        Utils.mapOfEntries(Utils.mapEntry("id", "3335")));
    addMockData(apiMethodDELETE, "MD9_DELETE", true, null, null, 202, null);

    addMockData(apiMethodPOSTSubparts, "MD10_POST", true, obj2JSON(new Subpart(1000, "Doorwindow", 2)),
        obj2JSON(new Subpart(1000, "Doorwindow", 3)), 200, Utils.mapOfEntries(Utils.mapEntry("id", "1"), //
            Utils.mapEntry("partid", "100"), //
            Utils.mapEntry("subpartid", "1000")));

    addMockData(apiMethodPOSTSubparts, "MD11_POST", true, obj2JSON(new Subpart(2000, "Seatcushion", null)),
        obj2JSON(new Subpart(2000, "Seatcushion", 11)), 200, Utils.mapOfEntries(Utils.mapEntry("id", "2"), //
            Utils.mapEntry("partid", "200"), //
            Utils.mapEntry("subpartid", "2000")));

    addMockData(apiMethodPOSTSubparts, "MD12_POST", true, null,
        obj2JSON(new Subpart(3000, "steeringWheelAirbag", 5)), 200,
        Utils.mapOfEntries(Utils.mapEntry("id", "3"), //
            Utils.mapEntry("partid", "300"), //
            Utils.mapEntry("subpartid", "3000")));

    addMockData(apiMethodPOSTSubparts, "MD13_POST", true, obj2JSON(new Subpart(4000, "WindowRegulator", 4)),
        obj2JSON(new Subpart(4000, "WindowRegulator", 5)), 200, null);

    addMockData(apiMethodPOSTSubparts, "MD14_POST", true, obj2JSON(new Subpart(5000, "BrakeLight", 123456)),
        obj2JSON(new Subpart(5000, "BrakeLight", 1234567)), 200,
        Utils.mapOfEntries(Utils.mapEntry("partid", "500")));

    addMockData(apiMethodPOSTSubparts, "MD15_POST", true, obj2JSON(new Subpart(6000, "V-belt", 112)),
        obj2JSON(new Subpart(6000, "V-belt", 113)), 200,
        Utils.mapOfEntries(Utils.mapEntry("id", "6"), Utils.mapEntry("partid", "600")));

    // verifiy no requests for apiMethodPOSTSubparts cars/{id}/parts/{partid}/subparts/{subpartid} are catched by method cars/%
    addMockData(apiMethodPOSTCar, "MD16_POST", true, null,
        "This Mockdata may not be used as request!!! If then there is a bug with the wildcard search in the method path",
        500, null);
  }

  private String obj2JSON(Object o)
  {
    try
    {
      return Utils.object2Json(o);
    }
    catch (JsonProcessingException e)
    {
      throw new IllegalStateException(e);
    }
  }
}