package de.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.List;
import de.joergdev.mosy.api.client.MosyApiClient;
import de.joergdev.mosy.api.client.MosyApiClientException;
import de.joergdev.mosy.api.model.BaseData;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.api.model.RecordConfig;
import de.joergdev.mosy.api.model.RecordSession;
import de.joergdev.mosy.api.model.Tenant;
import de.joergdev.mosy.api.response.system.LoginResponse;
import de.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockRouteRecordMulitTanencyTest extends AbstractSoapServiceClientTest
{
  private Integer tenant1Id;
  private Integer tenant2Id;

  @Override
  public void _runTest()
    throws Exception
  {
    tenant1Id = createTenant("T_1");
    tenant2Id = createTenant("T_2");

    loginCreateBaseDataAndCheckForTenant(tenant1Id, "interfaceForTenant1", true);
    loginCreateBaseDataAndCheckForTenant(tenant2Id, "interfaceForTenant2", false);

    testChangeTenantPassword();
    testChangeTenantName();

    testCallInterfaceMethods_checkForResultAndRecordsForTenants();

    testCreateLoadRecordSessionsForTenants();
    testCreateLoadMockProfilesForTenants();
    testCreateLoadRecordConfigsForTenants();

    testDeleteTenants();
  }

  private void testCallInterfaceMethods_checkForResultAndRecordsForTenants()
    throws Exception
  {
    LocalDateTime ldtStart = LocalDateTime.now().withNano(0);

    activeTenantId = tenant1Id;

    // Mockdata active matching
    invokeWsCall("1", "m_one_m");
    invokeWsCall("2", "m_two_m");

    // => no records
    checkRecordsSaved(ldtStart, 0);

    // --------------
    // For second tenant return different value for "1" (MD1) and no mock for "2" -> route and record

    activeTenantId = tenant2Id;

    // Mockdata active matching
    invokeWsCall("1", "m_one_m_second_tenant");

    // Route and record
    invokeWsCall("2", "two");

    List<Record> records = checkRecordsSaved(ldtStart, 1);
    checkRecord(records, 0, null, null, "<action>2</action>", true, null, "<return>two</return>", true);
  }

  private void testCreateLoadRecordSessionsForTenants()
  {
    // create RecordSession for tenant1
    activeTenantId = tenant1Id;
    Integer recordSessionId = getApiClient().createRecordSession().getRecordSession().getRecordSessionID();

    // Load RecordSessions for tenant1 -> must contain only the created recordSession
    List<RecordSession> recordSessions = getApiClient().loadRecordSessions().getRecordSessions();
    assertEquals(1, recordSessions.size());
    assertTrue(recordSessions.stream().anyMatch(rs -> recordSessionId.equals(rs.getRecordSessionID())));

    // Load RecordSessions for tenant2 -> may NOT contain any recordSession
    activeTenantId = tenant2Id;
    recordSessions = getApiClient().loadRecordSessions().getRecordSessions();
    assertTrue(recordSessions.isEmpty());
  }

  private void testCreateLoadMockProfilesForTenants()
  {
    // create MockProfile for tenant1
    activeTenantId = tenant1Id;
    MockProfile mp = new MockProfile();
    mp.setName("Test123");
    Integer mpId = getApiClient().saveMockProfile(mp).getMockProfile().getMockProfileID();

    // Load MockProfiles for tenant1 -> must contain only the created MockProfile
    List<MockProfile> mockProfiles = getApiClient().loadMockProfiles().getMockProfiles();
    assertEquals(1, mockProfiles.size());
    assertTrue(mockProfiles.stream().anyMatch(mpLoaded -> mpId.equals(mpLoaded.getMockProfileID())));

    // Load MockProfile by id => response must be same id
    assertEquals(mpId, getApiClient().loadMockProfile(mpId).getMockProfile().getMockProfileID());

    // Load MockProfiles for tenant2 -> may NOT contain any MockProfile
    activeTenantId = tenant2Id;
    mockProfiles = getApiClient().loadMockProfiles().getMockProfiles();
    assertTrue(mockProfiles.isEmpty());

    // Load MockProfile by id => response must be error
    assertThrows(() -> getApiClient().loadMockProfile(mpId), Exception.class);
  }

  private void testCreateLoadRecordConfigsForTenants()
  {
    // create RecordConfig for tenant1
    activeTenantId = tenant1Id;
    RecordConfig rc = new RecordConfig();
    rc.setTitle("RC_T1");
    rc.setInterfaceMethod(getApiInterfaceMethod());
    rc.setRequestData("<req>HelloWorld</req>");
    rc.setEnabled(true);
    Integer rcId = getApiClient().saveRecordConfig(rc).getRecordConfigID();

    // Load RecordConfigs for tenant1 -> must contain only the created RecordConfig
    List<RecordConfig> recordConfigs = getApiClient()
        .loadMethodRecordConfigs(getApiInterface().getInterfaceId(), getApiInterfaceMethod().getInterfaceMethodId()).getRecordConfigs();
    assertEquals(1, recordConfigs.size());
    assertTrue(recordConfigs.stream().anyMatch(rcLoaded -> rcId.equals(rcLoaded.getRecordConfigId())));

    // Load recordConfig by id => response must be same id
    assertEquals(rcId, getApiClient().loadRecordConfig(rcId).getRecordConfig().getRecordConfigId());

    // Load RecordConfigs for tenant2 -> may NOT contain any RecordConfig
    activeTenantId = tenant2Id;
    recordConfigs = getApiClient().loadMethodRecordConfigs(getApiInterface().getInterfaceId(), getApiInterfaceMethod().getInterfaceMethodId())
        .getRecordConfigs();
    assertTrue(recordConfigs.isEmpty());

    // Load recordConfig by id => response must be error
    assertThrows(() -> getApiClient().loadRecordConfig(rcId), Exception.class);
  }

  private void testDeleteTenants()
  {
    // delete tenant1
    activeTenantId = tenant1Id;
    getApiClient().deleteTenant(tenant1Id);

    // load and check: tenant1 doesnt exist anymore | tenant2 exists
    List<Tenant> tenants = getApiClientForAccessWithoutToken().loadTenants().getTenants();
    assertTrue(tenants.stream().noneMatch(t -> tenant1Id.equals(t.getTenantId())));
    assertTrue(tenants.stream().anyMatch(t -> tenant2Id.equals(t.getTenantId())));

    // delete tenant2
    activeTenantId = tenant2Id;
    getApiClient().deleteTenant(tenant2Id);

    // load and check: no tenant exists anymore
    tenants = getApiClientForAccessWithoutToken().loadTenants().getTenants();
    assertTrue(tenants.isEmpty());
  }

  private void testChangeTenantName()
  {
    activeTenantId = tenant1Id;

    Tenant tenant = new Tenant();
    tenant.setTenantId(activeTenantId);
    tenant.setName("New-Name");

    // save with new name
    getApiClient().saveTenant(tenant);

    // load tenants -> check exists by new name + check NOT exists by old name
    List<Tenant> tenants = getApiClientForAccessWithoutToken().loadTenants().getTenants();
    assertTrue(tenants.stream().anyMatch(t -> "New-Name".equals(t.getName())));
    assertTrue(tenants.stream().noneMatch(t -> "T_1".equals(t.getName())));

    // ----------------------------------------------
    // change name back to T_1

    tenant = new Tenant();
    tenant.setTenantId(activeTenantId);
    tenant.setName("T_1");

    // save with old name
    getApiClient().saveTenant(tenant);

    // load tenants -> check NOT exists by new name + check exists by old name
    tenants = getApiClientForAccessWithoutToken().loadTenants().getTenants();
    assertTrue(tenants.stream().noneMatch(t -> "New-Name".equals(t.getName())));
    assertTrue(tenants.stream().anyMatch(t -> "T_1".equals(t.getName())));
  }

  private void testChangeTenantPassword()
  {
    activeTenantId = tenant1Id;

    Tenant tenant = new Tenant();
    tenant.setTenantId(activeTenantId);
    tenant.setName("T_1");

    // change password
    getApiClient().saveTenant(tenant, "newSecret".hashCode());

    // Login with old password => Fail
    final MosyApiClient mosyClientFail1 = new MosyApiClient();
    assertThrowsExactly(() -> mosyClientFail1.systemLogin(activeTenantId, getDefaultPasswordForTenant(activeTenantId).hashCode()), //
        MosyApiClientException.class, "Invalid credentials");

    // Login with new password => success
    MosyApiClient mosyClient = new MosyApiClient();
    LoginResponse response = mosyClient.systemLogin(activeTenantId, "newSecret".hashCode());
    assertNotNull(response.getToken());

    // ----------------------------------------------
    // change password back to default
    getApiClient().saveTenant(tenant, getDefaultPasswordForTenant(activeTenantId).hashCode());

    // Login with old password => success
    mosyClient = new MosyApiClient();
    mosyClient.systemLogin(activeTenantId, getDefaultPasswordForTenant(activeTenantId).hashCode());

    // Login with old password => Fail
    final MosyApiClient mosyClientFail2 = new MosyApiClient();
    assertThrowsExactly(() -> mosyClientFail2.systemLogin(activeTenantId, "newSecret".hashCode()), //
        MosyApiClientException.class, "Invalid credentials");
  }

  private void assertThrows(Runnable run, Class<? extends Exception> expectedExceptionClass)
  {
    try
    {
      run.run();

      fail("Should throw exception of type " + expectedExceptionClass);
    }
    catch (Exception ex)
    {
      if (!expectedExceptionClass.isAssignableFrom(ex.getClass()))
      {
        ex.printStackTrace();
        fail(ex.getMessage());
      }
    }
  }

  private void assertThrowsExactly(Runnable run, Class<? extends Exception> expectedExceptionClass, String expectedErrorMessage)
  {
    try
    {
      run.run();

      fail("Should throw exception of type " + expectedExceptionClass + " having message " + expectedErrorMessage);
    }
    catch (Exception ex)
    {
      if (!expectedExceptionClass.isAssignableFrom(ex.getClass()) || !expectedErrorMessage.equalsIgnoreCase(ex.getMessage()))
      {
        ex.printStackTrace();
        fail(ex.getMessage());
      }
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
    else if (activeTenantId.equals(tenant2Id))
    {
      getApiInterface().setName("interfaceForTenant2");

      getApiInterface().setRoutingOnNoMockData(false);
    }
  }

  @Override
  protected void setPropertiesInterfaceMethodForTest()
  {
    getApiInterfaceMethod().setRoutingOnNoMockData(true);
    getApiInterfaceMethod().setMockActive(true);
    getApiInterfaceMethod().setMockActiveOnStartup(true);
    getApiInterfaceMethod().setRecord(true);

    // For second tenant return different value for "1" (MD1) and nothing for "2" (MD2) -> route and record

    if (activeTenantId.equals(tenant1Id))
    {
      addMockData("MD1", true, "1", "m_one_m");
      addMockData("MD2", true, "2", "m_two_m");
    }
    else if (activeTenantId.equals(tenant2Id))
    {
      addMockData("MD1", true, "1", "m_one_m_second_tenant");
    }
  }

  @Override
  protected boolean isMulitTanencyEnabled()
  {
    return true;
  }
}
