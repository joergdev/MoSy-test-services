package de.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.List;
import de.joergdev.mosy.api.APIConstants;
import de.joergdev.mosy.api.model.BaseData;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;
import de.joergdev.mosy.test.services.soap.core.SoapServiceClientPortSingleton;

public class MockMultiTanencyAlternativeUrlTest extends AbstractSoapServiceClientTest
{
  private Integer tenant1Id;

  @Override
  public void _runTest()
    throws Exception
  {
    tenant1Id = createTenant("T_1A");

    loginCreateBaseDataAndCheckForTenant(tenant1Id, "interfaceForTenant1", true);

    testCallInterfaceMethods_checkForResultAndRecordsForTenants();
  }

  private void testCallInterfaceMethods_checkForResultAndRecordsForTenants()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    // set explicit to null for test without tenantId in header and call with url teants/{id}
    activeTenantId = null;

    // Mockdata active matching
    invokeWsCall("1", "m_one_m");
    invokeWsCall("2", "m_two_m");

    activeTenantId = tenant1Id;

    // => no records
    checkRecordsSaved(ldtStart, 0);
  }

  @Override
  protected void setPropertiesBaseData()
  {
    getApiBaseData().setMockActive(true);
    getApiBaseData().setMockActiveOnStartup(true);
    getApiBaseData().setRecord(null);
    getApiBaseData().setRoutingOnNoMockData(true);
  }

  @Override
  protected void setPropertiesInterfaceForTest()
  {
    getApiInterface().setMockActive(true);
    getApiInterface().setMockActiveOnStartup(true);
    getApiInterface().setRecord(true);

    if (activeTenantId.equals(tenant1Id))
    {
      getApiInterface().setName("interfaceForTenant1");

      getApiInterface().setRoutingOnNoMockData(true);
    }
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setRoutingOnNoMockData(true);
    getApiInterfaceMethod().setMockActive(true);
    getApiInterfaceMethod().setMockActiveOnStartup(true);
    getApiInterfaceMethod().setRecord(true);

    if (activeTenantId.equals(tenant1Id))
    {
      addMockData("MD1", true, "1", "m_one_m");
      addMockData("MD2", true, "2", "m_two_m");
    }
  }

  private void loginCreateBaseDataAndCheckForTenant(Integer tenantId, String interfaceName, Boolean routingOnNoMokData)
  {
    activeTenantId = tenantId;

    doLoginAndCreateBaseData();

    BaseData baseData = getApiClient().systemLoadBasedata().getBaseData();

    List<Interface> interfaces = baseData.getInterfaces();
    assertEquals(1, interfaces.size());

    assertEquals(interfaceName, interfaces.get(0).getName());
    assertEquals(routingOnNoMokData, interfaces.get(0).getRoutingOnNoMockData());
  }

  @Override
  protected boolean isMulitTanencyEnabled()
  {
    return true;
  }

  @Override
  protected void setSoapServiceEndpointToMosyMockService()
  {
    SoapServiceClientPortSingleton.getInstance()
        .initService("http://localhost:3911/" + APIConstants.API_URL_BASE + "tenants/" + tenant1Id + "/mock-services/soap/SoapService");
  }
}
