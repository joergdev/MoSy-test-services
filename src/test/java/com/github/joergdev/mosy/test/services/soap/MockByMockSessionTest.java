package com.github.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import com.github.joergdev.mosy.api.model.MockSession;
import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockByMockSessionTest extends AbstractSoapServiceClientTest
{
  private MockSession apiMockSessionActive;
  private MockSession apiMockSessionOther;

  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active for mockSession matching
    invokeWsCall("1", "ms1_one_ms1", apiMockSessionActive.getMockSessionID());

    // second call has to fail, there are 2 mockdata 
    // but first is inactive, second is related to other mockSession
    Exception failOnNoMockData = null;

    try
    {
      invokeWsCall("2", null, apiMockSessionActive.getMockSessionID());
    }
    catch (Exception ex)
    {
      failOnNoMockData = ex;
    }

    assertNotNull(failOnNoMockData);

    // Mockdata active for mockSession matching
    invokeWsCall("3", "ms1_three_ms1", apiMockSessionActive.getMockSessionID());

    // Mockdata inactive for mockSession matching, mockdata wihtout mockSession matching
    invokeWsCall("4", "m_four_m", apiMockSessionActive.getMockSessionID());
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
    apiMethod.setRoutingOnNoMockData(false);
    apiMethod.setMockActive(true);
    apiMethod.setMockActiveOnStartup(true);
    apiMethod.setRecord(false);

    apiMockSessionActive = mosyClient.createMocksession().getMockSession();
    apiMockSessionOther = mosyClient.createMocksession().getMockSession();

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD1_MS1", true, "1", "ms1_one_ms1", apiMockSessionActive);

    addMockData("MD2_MS1", false, "2", "ms1_two_ms1", apiMockSessionActive);
    addMockData("MD2_MS2", true, "2", "ms2_two_ms2", apiMockSessionOther);

    addMockData("MD3_MS1", true, "3", "ms1_three_ms1", apiMockSessionActive);
    addMockData("MD3", true, "3", "m_three_m");

    addMockData("MD4_MS1", false, "4", "ms1_four_ms1", apiMockSessionActive);
    addMockData("MD4", true, "4", "m_four_m");
  }
}