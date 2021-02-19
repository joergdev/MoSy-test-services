package com.github.joergdev.mosy.test.services.soap;

import static org.junit.Assert.*;
import javax.xml.ws.soap.SOAPFaultException;
import com.github.joergdev.mosy.api.model.MockProfile;
import com.github.joergdev.mosy.test.services.soap.core.AbstractSoapServiceClientTest;

public class MockByMockProfileTest extends AbstractSoapServiceClientTest
{
  private MockProfile apiMockProfileActiveCommonMockdataUsage;
  private MockProfile apiMockProfileActiveNoCommonMockdataUsage;
  private MockProfile apiMockProfileOther;

  @Override
  public void _runTest()
    throws Exception
  {
    // Mockdata active for mockSession matching
    invokeWsCall("1", "ms1_one_ms1", apiMockProfileActiveCommonMockdataUsage.getName(), null);

    /*
     * TODO
    
    org.junit.ComparisonFailure: expected:<m[s1_one_ms1]> but was:<m[_one_m]>
    
    
     */

    // second call has to fail, there are 2 mockdata 
    // but first is inactive, second is related to other mockSession

    try
    {
      invokeWsCall("2", null, apiMockProfileActiveCommonMockdataUsage.getName(), null);

      fail("no SOAPFaultException");
    }
    catch (SOAPFaultException ex)
    {
      assertTrue(ex.getMessage().contains(
          "[ERROR] - 6 - Unexpected error, please contact your Systemadmin [Operation failed [no mockdata for interface SoapService, method testMethod"));
    }

    // Mockdata active for mockSession matching
    invokeWsCall("3", "ms1_three_ms1", apiMockProfileActiveCommonMockdataUsage.getName(), null);

    // Mockdata inactive for mockSession matching, mockdata wihtout mockSession matching (common usage)
    invokeWsCall("4", "m_four_m", apiMockProfileActiveCommonMockdataUsage.getName(), null);

    // Mockdata incative for mocksession matching, mockdata wihtout mockSession matching - but non common usage
    try
    {
      invokeWsCall("5", "ms3_five_ms3", apiMockProfileActiveNoCommonMockdataUsage.getName(), null);

      fail("no SOAPFaultException");
    }
    catch (SOAPFaultException ex)
    {
      assertTrue(ex.getMessage().contains(
          "[ERROR] - 6 - Unexpected error, please contact your Systemadmin [Operation failed [no mockdata for interface SoapService, method testMethod"));
    }

    // Mockdata active for mocksession matching
    invokeWsCall("6", "ms3_six_ms3", apiMockProfileActiveNoCommonMockdataUsage.getName(), null);

    // TODO
    // mockdata common existring for mockProfile
    invokeWsCall("7", "ms2_seven_ms2", apiMockProfileActiveCommonMockdataUsage.getName(), null);

    // mockdata common (not for mockprofile) / mockprofile use common
    invokeWsCall("8", "ms2_eight_ms2", apiMockProfileActiveCommonMockdataUsage.getName(), null);

    // mockdata not common / mockprofile use common
    try
    {
      invokeWsCall("9", "ms2_nine_ms2", apiMockProfileActiveCommonMockdataUsage.getName(), null);

      fail("no SOAPFaultException");
    }
    catch (SOAPFaultException ex)
    {
      assertTrue(ex.getMessage().contains(
          "[ERROR] - 6 - Unexpected error, please contact your Systemadmin [Operation failed [no mockdata for interface SoapService, method testMethod"));
    }
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

    apiMockProfileActiveCommonMockdataUsage = createMockProfile("active", true);
    apiMockProfileActiveNoCommonMockdataUsage = createMockProfile("active_non_common", false);
    apiMockProfileOther = createMockProfile("inactive", false);

    addMockData("MD1", true, "1", "m_one_m");
    addMockData("MD1_MS1", true, "1", "ms1_one_ms1", apiMockProfileActiveCommonMockdataUsage);

    addMockData("MD2_MS1", false, "2", "ms1_two_ms1", apiMockProfileActiveCommonMockdataUsage);
    addMockData("MD2_MS2", true, "2", "ms2_two_ms2", apiMockProfileOther);

    addMockData("MD3_MS1", true, "3", "ms1_three_ms1", apiMockProfileActiveCommonMockdataUsage);
    addMockData("MD3", true, "3", "m_three_m");

    addMockData("MD4_MS1", false, "4", "ms1_four_ms1", apiMockProfileActiveCommonMockdataUsage);
    addMockData("MD4", true, "4", "m_four_m");

    addMockData("MD5_MS3", false, "5", "ms3_five_ms3", apiMockProfileActiveCommonMockdataUsage);

    addMockData("MD6_MS3", true, "6", "ms3_six_ms3", apiMockProfileActiveNoCommonMockdataUsage);

    addMockData("MD7_MS2", true, "7", "ms2_seven_ms2", apiMockProfileActiveCommonMockdataUsage, true);

    addMockData("MD8_MS2", true, "8", "ms2_eight_ms2", null, true);

    addMockData("MD9_MS2", true, "9", "ms2_nine_ms2", apiMockProfileOther, false);
  }
}