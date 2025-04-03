package de.joergdev.mosy.test.services.common;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.InterfaceType;
import de.joergdev.mosy.api.response.system.LoadBaseDataResponse;
import de.joergdev.mosy.test.services.AbstractServiceClientTest;

public class ImportDataTest extends AbstractServiceClientTest
{

  @Override
  protected void _before()
  {
    // delete all interfaces
    List<Interface> interfaces = getApiClient().systemLoadBasedata().getBaseData().getInterfaces();
    interfaces.forEach(ifc -> getApiClient().deleteInterface(ifc.getInterfaceId()));
  }

  @Override
  public void _runTest()
    throws Exception
  {
    byte[] bytesImport = Files.readAllBytes(Paths.get(getClass().getResource("/import.json").toURI()));
    String jsonImport = new String(bytesImport, StandardCharsets.UTF_8);

    de.joergdev.mosy.api.model.BaseData apiBaseData = de.joergdev.mosy.shared.Utils.jsonToObject(jsonImport, de.joergdev.mosy.api.model.BaseData.class);

    // Import
    getApiClient().importData(apiBaseData);

    // Load data
    LoadBaseDataResponse response = getApiClient().systemLoadBasedata();

    apiBaseData = response.getBaseData();
    assertNotNull(apiBaseData);

    // evaluate interfaces and methods
    List<Interface> apiInterfaces = apiBaseData.getInterfaces();
    assertNotNull(apiInterfaces);
    assertEquals(3, apiInterfaces.size());

    Interface apiInterface1 = apiInterfaces.get(0);
    evaluateInterfaceData(apiInterface1, "Ifc1", InterfaceType.SOAP, "Ifc1SvcPath", "http://services/Ifc1Service", true, 0);

    Interface apiInterface2 = apiInterfaces.get(1);
    evaluateInterfaceData(apiInterface2, "Ifc2", InterfaceType.SOAP, "Ifc2SvcPath", "http://services/Ifc2Service", true, 1);
    evaluateInterfaceMethodData(apiInterface2.getMethods().get(0), "ifc2Operation1", "ifc2Operation1SvcPath", false, false, false);

    Interface apiInterface3 = apiInterfaces.get(2);
    evaluateInterfaceData(apiInterface3, "Ifc3", InterfaceType.CUSTOM_XML, "Ifc3", null, false, 2);
    evaluateInterfaceMethodData(apiInterface3.getMethods().get(0), "ifc3Operation1", "ifc3Operation1", true, true, true);
    evaluateInterfaceMethodData(apiInterface3.getMethods().get(1), "ifc3Operation2", "ifc3Operation2", true, true, false);
  }

  private void evaluateInterfaceData(Interface apiInterface, String name, InterfaceType type, String servicePath, String routingUrl,
                                     Boolean routingOnNoMockData, int methodCount)
  {
    assertEquals(name, apiInterface.getName());
    assertEquals(type, apiInterface.getType());
    assertEquals(servicePath, apiInterface.getServicePath());
    assertEquals(routingUrl, apiInterface.getRoutingUrl());
    assertEquals(routingOnNoMockData, apiInterface.getRoutingOnNoMockData());
    assertEquals(methodCount, apiInterface.getMethods().size());
  }

  private void evaluateInterfaceMethodData(InterfaceMethod apiMethod, String name, String servicePath, Boolean routingOnNoMockData, Boolean mockActive,
                                           Boolean mockActiveOnStartup)
  {
    assertEquals(name, apiMethod.getName());
    assertEquals(servicePath, apiMethod.getServicePath());
    assertEquals(routingOnNoMockData, apiMethod.getRoutingOnNoMockData());
    assertEquals(mockActive, apiMethod.getMockActive());
    assertEquals(mockActiveOnStartup, apiMethod.getMockActiveOnStartup());
  }

  @Override
  protected void setPropertiesBaseData()
  {
    // nothing to do
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setName("InterfaceThatWillBeDeleted");
    getApiInterface().setType(InterfaceType.CUSTOM_XML);
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setName("InterfaceMethodThatWillBeDeleted");
  }
}