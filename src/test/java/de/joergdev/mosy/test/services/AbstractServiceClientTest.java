package de.joergdev.mosy.test.services;

import static org.junit.Assert.*;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import de.joergdev.mosy.api.client.MosyApiClient;
import de.joergdev.mosy.api.model.BaseData;
import de.joergdev.mosy.api.model.Interface;
import de.joergdev.mosy.api.model.InterfaceMethod;
import de.joergdev.mosy.api.model.MockData;
import de.joergdev.mosy.api.model.MockProfile;
import de.joergdev.mosy.api.model.PathParam;
import de.joergdev.mosy.api.model.Record;
import de.joergdev.mosy.api.model.RecordConfig;
import de.joergdev.mosy.api.model.RecordSession;
import de.joergdev.mosy.api.model.Tenant;
import de.joergdev.mosy.api.model.UrlArgument;
import de.joergdev.mosy.backend.Config;
import de.joergdev.mosy.backend.standalone.ApplicationMain;
import de.joergdev.mosy.shared.Utils;

public abstract class AbstractServiceClientTest
{
  protected Integer activeTenantId;

  private final Map<Integer, MosyApiClient> mosyClientsForTenants = new HashMap<>();

  private final Map<Integer, BaseData> apiBaseDataForTenants = new HashMap<>();
  private final Map<Integer, Interface> apiInterfaceForTenants = new HashMap<>();
  private final Map<Integer, InterfaceMethod> apiMethodForTenants = new HashMap<>();

  @Before
  public void before()
  {
    System.setProperty(Config.SYSTEM_PROPERTY_MULTI_TENANCY_ENABLED, isMulitTanencyEnabled() ? "true" : "false");

    if (isMulitTanencyEnabled())
    {
      deleteAllTenantsBefore();
    }
    else
    {
      doLoginAndCreateBaseData();
    }

    _before();
  }

  protected void doLoginAndCreateBaseData()
  {
    if (isMulitTanencyEnabled() && activeTenantId == null)
    {
      throw new NullPointerException("activeTenantId may not be null if mulitTanency is enabled ");
    }

    MosyApiClient mosyClient = createApiClientAndLogin();

    // Set back default settings
    mosyClient.systemBoot();

    // assure basedata, interface and method exists correct for testcase
    apiBaseDataForTenants.put(activeTenantId, getDefaultBaseData());
    setPropertiesBaseData();
    assureBaseDataConfigurated();

    apiInterfaceForTenants.put(activeTenantId, getDefaultInterface());
    setPropertiesInterfaceForTest();
    assureInterfaceExists();

    apiMethodForTenants.put(activeTenantId, getDefaultInterfaceMethod());
    setPropertiesInterfaceMethodForTest();
    InterfaceMethod apiMethod = assureInterfaceMethodExists(getApiInterfaceMethod());
    apiMethodForTenants.put(activeTenantId, apiMethod);

    clearRecords();
  }

  private MosyApiClient createApiClientAndLogin()
  {
    // create MosyApiClient and login
    MosyApiClient mosyClient = new MosyApiClient();

    try
    {
      doLogin(mosyClient);
    }
    catch (RuntimeException ex)
    {
      // Start Backend and retry Login
      if (ex.getCause() instanceof ConnectException)
      {
        ApplicationMain.main(new String[0]);

        doLogin(mosyClient);
      }
      else
      {
        throw ex;
      }
    }

    return mosyClient;
  }

  /**
   * <pre>
   * Login and put the client into map <mosyClientsForTenants>.
   * 
   * If activeTenantId is set (!null), "pw_{tenantId}" is used as password.
   * Otherwise (activeTenantId==null) the default passwort ("m0sy") is used.
   * </pre>
   * 
   * @param mosyClient
   */
  private void doLogin(MosyApiClient mosyClient)
  {
    mosyClient.systemLogin(activeTenantId, (activeTenantId == null ? "m0sy" : getDefaultPasswordForTenant(activeTenantId)).hashCode());

    mosyClientsForTenants.put(activeTenantId, mosyClient);
  }

  protected String getDefaultPasswordForTenant(Integer tenantId)
  {
    return "pw_" + tenantId;
  }

  protected boolean isMulitTanencyEnabled()
  {
    return false;
  }

  protected MosyApiClient getApiClient()
  {
    return getApiClient(false);
  }

  protected MosyApiClient getApiClientForAccessWithoutToken()
  {
    return getApiClient(true);
  }

  private MosyApiClient getApiClient(boolean accessWithoutToken)
  {
    if (accessWithoutToken)
    {
      MosyApiClient mosyClient = new MosyApiClient();

      return mosyClient;
    }
    else if (isMulitTanencyEnabled())
    {
      return mosyClientsForTenants.get(Objects.requireNonNull(activeTenantId, "activeTenantId"));
    }
    else
    {
      return Utils.getFirstElementOfCollection(mosyClientsForTenants.values());
    }
  }

  protected Interface getApiInterface()
  {
    if (isMulitTanencyEnabled())
    {
      return apiInterfaceForTenants.get(Objects.requireNonNull(activeTenantId, "activeTenantId"));
    }
    else
    {
      return Utils.getFirstElementOfCollection(apiInterfaceForTenants.values());
    }
  }

  protected InterfaceMethod getApiInterfaceMethod()
  {
    if (isMulitTanencyEnabled())
    {
      return apiMethodForTenants.get(Objects.requireNonNull(activeTenantId, "activeTenantId"));
    }
    else
    {
      return Utils.getFirstElementOfCollection(apiMethodForTenants.values());
    }
  }

  protected BaseData getApiBaseData()
  {
    if (isMulitTanencyEnabled())
    {
      return apiBaseDataForTenants.get(Objects.requireNonNull(activeTenantId, "activeTenantId"));
    }
    else
    {
      return Utils.getFirstElementOfCollection(apiBaseDataForTenants.values());
    }
  }

  @Test
  public void runTest()
  {
    try
    {
      _runTest();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      fail(ex.getMessage());
    }
  }

  protected abstract void _before();

  public abstract void _runTest()
    throws Exception;

  protected abstract void setPropertiesBaseData();

  protected abstract void setPropertiesInterfaceForTest();

  protected abstract void setPropertiesInterfaceMethodForTest();

  private BaseData getDefaultBaseData()
  {
    BaseData apiBaseData = new BaseData();
    apiBaseData.setMockActive(true);
    apiBaseData.setMockActiveOnStartup(true);
    apiBaseData.setRecord(false);
    apiBaseData.setRoutingOnNoMockData(false);
    apiBaseData.setTtlMockProfile(86400);
    apiBaseData.setTtlRecordSession(86400);

    return apiBaseData;
  }

  protected Interface getDefaultInterface()
  {
    Interface apiInterface = new Interface();
    apiInterface.setMockActive(true);
    apiInterface.setMockActiveOnStartup(true);
    apiInterface.setRecord(false);
    apiInterface.setRoutingOnNoMockData(false);

    return apiInterface;
  }

  protected InterfaceMethod getDefaultInterfaceMethod()
  {
    InterfaceMethod apiMethod = new InterfaceMethod();
    apiMethod.setRoutingOnNoMockData(false);
    apiMethod.setMockActive(true);
    apiMethod.setMockActiveOnStartup(true);
    apiMethod.setRecord(false);

    return apiMethod;
  }

  private void assureBaseDataConfigurated()
  {
    getApiClient().globalConfigSave(getApiBaseData());
  }

  protected InterfaceMethod assureInterfaceMethodExists(InterfaceMethod target)
  {
    InterfaceMethod apiMethod = getApiInterface().getMethodByName(target.getName());

    if (apiMethod == null)
    {
      apiMethod = new InterfaceMethod();
      apiMethod.setName(target.getName());

      getApiInterface().getMethods().add(apiMethod);
    }
    // Delete recordconfigs and mockdata
    else
    {
      List<RecordConfig> recordConfigs = getApiClient().loadMethodRecordConfigs(getApiInterface().getInterfaceId(), apiMethod.getInterfaceMethodId())
          .getRecordConfigs();

      for (RecordConfig rc : recordConfigs)
      {
        getApiClient().deleteRecordConfig(rc.getRecordConfigId());
      }

      List<MockData> mockData = getApiClient().loadMethodMockData(getApiInterface().getInterfaceId(), apiMethod.getInterfaceMethodId()).getMockData();

      for (MockData md : mockData)
      {
        getApiClient().deleteMockData(md.getMockDataId());
      }
    }

    apiMethod.setServicePath(target.getServicePath());
    apiMethod.setHttpMethod(target.getHttpMethod());
    apiMethod.setRoutingOnNoMockData(target.getRoutingOnNoMockData());
    apiMethod.setMockActive(target.getMockActive());
    apiMethod.setMockActiveOnStartup(target.getMockActiveOnStartup());
    apiMethod.setRecord(target.getRecord());

    // save
    Interface apiInterfaceSaved = getApiClient().saveInterface(getApiInterface()).getInterface();

    apiMethod.setInterfaceMethodId(apiInterfaceSaved.getMethodByName(target.getName()).getInterfaceMethodId());

    // save mockData
    for (MockData targetMockData : target.getMockData())
    {
      targetMockData.setInterfaceMethod(new InterfaceMethod());
      targetMockData.getInterfaceMethod().setInterfaceMethodId(apiMethod.getInterfaceMethodId());

      Integer mockDataId = getApiClient().saveMockData(targetMockData).getMockData().getMockDataId();

      targetMockData.setMockDataId(mockDataId);
      apiMethod.getMockData().add(targetMockData);
    }

    // save recordconfigs
    for (RecordConfig targetRC : target.getRecordConfigs())
    {
      targetRC.setInterfaceMethod(new InterfaceMethod());
      targetRC.getInterfaceMethod().setInterfaceMethodId(apiMethod.getInterfaceMethodId());

      Integer rcID = getApiClient().saveRecordConfig(targetRC).getRecordConfigID();

      targetRC.setRecordConfigId(rcID);
      apiMethod.getRecordConfigs().add(targetRC);
    }

    return apiMethod;
  }

  private void assureInterfaceExists()
  {
    List<Interface> interfaces = getApiClient().systemLoadBasedata().getBaseData().getInterfaces();

    Interface target = getApiInterface();

    Interface apiInterface = interfaces.stream().filter(i -> target.getName().equals(i.getName())).findAny().orElse(null);

    if (apiInterface == null)
    {
      apiInterface = new Interface();
      apiInterface.setName(target.getName());
    }

    apiInterface.setType(target.getType());
    apiInterface.setMockActive(target.getMockActive());
    apiInterface.setMockActiveOnStartup(target.getMockActiveOnStartup());
    apiInterface.setRecord(target.getRecord());
    apiInterface.setRoutingOnNoMockData(target.getRoutingOnNoMockData());
    apiInterface.setRoutingUrl(target.getRoutingUrl());
    apiInterface.setServicePath(target.getServicePath());

    Interface apiInterfaceSaved = getApiClient().saveInterface(apiInterface).getInterface();

    apiInterface = getApiClient().loadInterface(apiInterfaceSaved.getInterfaceId()).getInterface();

    apiInterfaceForTenants.put(activeTenantId, apiInterface);
  }

  protected RecordSession addRecordSession()
  {
    return getApiClient().createRecordSession().getRecordSession();
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue)
  {
    addMockData(title, active, requestAction, returnValue, false);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, Integer httpReturnCode)
  {
    addMockData(apiMethodMd, title, active, requestAction, returnValue, false, httpReturnCode);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue, boolean common)
  {
    addMockData(title, active, requestAction, returnValue, null, common);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, boolean common,
                             Integer httpReturnCode)
  {
    addMockData(apiMethodMd, title, active, requestAction, returnValue, null, common, httpReturnCode);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile, boolean common)
  {
    addMockData(title, active, requestAction, returnValue, apiMockProfile, common, null);
  }

  protected void addMockData(String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile, boolean common,
                             Integer httpReturnCode)
  {
    addMockData(getApiInterfaceMethod(), title, active, requestAction, returnValue, apiMockProfile, common, httpReturnCode);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, Integer httpReturnCode,
                             Map<String, String> pathParams)
  {
    addMockData(apiMethodMd, title, active, requestAction, returnValue, null, false, httpReturnCode, pathParams);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile,
                             boolean common, Integer httpReturnCode)
  {
    addMockData(apiMethodMd, title, active, requestAction, returnValue, apiMockProfile, common, httpReturnCode, null);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile,
                             boolean common, Integer httpReturnCode, Map<String, String> pathParams)
  {
    addMockData(apiMethodMd, title, active, requestAction, returnValue, apiMockProfile, common, httpReturnCode, pathParams, null, null);
  }

  protected void addMockData(InterfaceMethod apiMethodMd, String title, boolean active, String requestAction, String returnValue, MockProfile apiMockProfile,
                             boolean common, Integer httpReturnCode, Map<String, String> pathParams, Map<String, String> urlArguments, Long delay)
  {
    MockData md1 = new MockData();
    md1.setActive(active);
    md1.setTitle(title);
    md1.setRequest(requestAction);
    md1.setResponse(returnValue);
    md1.setDelay(delay);
    md1.setCommon(common);
    md1.setHttpReturnCode(httpReturnCode);

    if (pathParams != null)
    {
      md1.getPathParams().addAll(pathParams.entrySet().stream().map(e -> new PathParam(e.getKey(), e.getValue())).collect(Collectors.toList()));
    }

    if (urlArguments != null)
    {
      md1.getUrlArguments().addAll(urlArguments.entrySet().stream().map(e -> new UrlArgument(e.getKey(), e.getValue())).collect(Collectors.toList()));
    }

    if (apiMockProfile != null)
    {
      md1.getMockProfiles().add(apiMockProfile);
    }

    apiMethodMd.getMockData().add(md1);
  }

  protected void addRecordConfig(String title, boolean enabled, String requestAction)
  {
    RecordConfig rc = new RecordConfig();
    rc.setTitle(title);
    rc.setEnabled(enabled);
    rc.setRequestData(requestAction);

    getApiInterfaceMethod().getRecordConfigs().add(rc);
  }

  protected List<Record> checkRecordsSaved(LocalDateTime ldtStart, long expectedCountRecords)
  {
    return checkRecordsSaved(ldtStart, expectedCountRecords, null);
  }

  protected List<Record> checkRecordsSaved(LocalDateTime ldtStart, long expectedCountRecords, Integer recordSessionID)
  {
    LocalDateTime ldtEnd = LocalDateTime.now().withNano(0).plusSeconds(1);

    // check records saved
    List<Record> records = getApiClient().loadRecords(null, null, recordSessionID).getRecords();

    List<Record> recordsInRange = records.stream().filter(r -> r.getCreatedAsLdt().compareTo(ldtStart) >= 0 && r.getCreatedAsLdt().compareTo(ldtEnd) <= 0)
        .collect(Collectors.toList());

    assertEquals(expectedCountRecords, recordsInRange.size());

    return recordsInRange;
  }

  protected void checkRecord(List<Record> records, int pos, Map<String, String> expectedPathParams, Map<String, String> expectedUrlArguments,
                             String expectedRequest, Integer expectedHttpReturnCode, String expectedResponse)
  {
    checkRecord(records, pos, expectedPathParams, expectedUrlArguments, expectedRequest, false, expectedHttpReturnCode, expectedResponse, false);
  }

  protected void checkRecord(List<Record> records, int pos, Map<String, String> expectedPathParams, Map<String, String> expectedUrlArguments,
                             String expectedRequest, boolean expectedRequestContains, Integer expectedHttpReturnCode, String expectedResponse,
                             boolean expectedResponseContains)
  {
    Record rec = getApiClient().loadRecord(records.get(pos).getRecordId()).getRecord();

    // pathParams
    expectedPathParams = Utils.nvlMap(expectedPathParams);
    assertEquals(expectedPathParams.size(), rec.getPathParams().size());
    assertTrue(Utils.mapContainsMap(expectedPathParams, rec.getPathParams().stream().collect(Collectors.toMap(pp -> pp.getKey(), pp -> pp.getValue()))));

    // urlArguments
    expectedUrlArguments = Utils.nvlMap(expectedUrlArguments);
    assertEquals(expectedUrlArguments.size(), rec.getUrlArguments().size());
    assertTrue(Utils.mapContainsMap(expectedUrlArguments, rec.getUrlArguments().stream().collect(Collectors.toMap(ua -> ua.getKey(), ua -> ua.getValue()))));

    expectedRequest = Utils.nvl(expectedRequest).trim();
    String request = Utils.nvl(rec.getRequestData()).trim();
    if (expectedRequestContains)
    {
      assertTrue(request.contains(expectedRequest));
    }
    else
    {
      assertEquals(expectedRequest, request);
    }

    if (expectedHttpReturnCode != null)
    {
      assertEquals(expectedHttpReturnCode, rec.getHttpReturnCode());
    }

    expectedResponse = Utils.nvl(expectedResponse).trim();
    String response = Utils.nvl(rec.getResponse()).trim();
    if (expectedResponseContains)
    {
      assertTrue(response.contains(expectedResponse));
    }
    else
    {
      assertEquals(expectedResponse, response);
    }
  }

  protected MockProfile createMockProfile(String name, boolean useCommonMocks)
  {
    MockProfile apiMockProfile = new MockProfile();
    apiMockProfile.setName(name);
    apiMockProfile.setUseCommonMocks(useCommonMocks);

    return getApiClient().saveMockProfile(apiMockProfile).getMockProfile();
  }

  private void clearRecords()
  {
    List<Record> records = getApiClient().loadRecords(null, null, null).getRecords();

    records.forEach(r -> getApiClient().deleteRecord(r.getRecordId()));
  }

  protected List<Tenant> getAllTenants()
  {
    return getApiClientForAccessWithoutToken().loadTenants().getTenants();
  }

  protected void deleteAllTenants()
  {
    List<Tenant> tenants = getAllTenants();

    for (Tenant tenant : tenants)
    {
      activeTenantId = tenant.getTenantId();

      createApiClientAndLogin();

      getApiClient().deleteTenant(tenant.getTenantId());
    }

    mosyClientsForTenants.clear();
  }

  private void deleteAllTenantsBefore()
  {
    try
    {
      deleteAllTenants();
    }
    catch (RuntimeException ex)
    {
      // Start Backend and retry Login
      if (ex.getCause() instanceof ConnectException)
      {
        ApplicationMain.main(new String[0]);

        deleteAllTenants();
      }
      else
      {
        throw ex;
      }
    }
  }

  protected Integer createTenant(String name)
  {
    Tenant tenant = new Tenant();
    tenant.setName(name);

    int initalPwHash = 123;

    tenant = getApiClientForAccessWithoutToken().saveTenant(tenant, initalPwHash) //
        .getTenant();
    Integer tenantId = tenant.getTenantId();

    // create MosyApiClient and login
    MosyApiClient mosyClient = new MosyApiClient();
    mosyClient.systemLogin(tenantId, initalPwHash);

    // change secret to default so that it can be reseted (deleted)
    mosyClient.saveTenant(tenant, getDefaultPasswordForTenant(tenantId).hashCode());

    return tenantId;
  }
}
