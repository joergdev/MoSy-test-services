package de.joergdev.mosy.test.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService(name = "SoapService", serviceName = "SoapService")
@SOAPBinding(style = Style.RPC)
public class SoapServiceImpl
{
  @WebMethod
  public String testMethod(@WebParam(name = "action", partName = "action") String action)
  {
    try
    {
      if ("1".equals(action))
      {
        return "one";
      }
      else if ("2".equals(action))
      {
        return "two";
      }
      else if ("3".equals(action))
      {
        return "three";
      }
      else if ("4".equals(action))
      {
        return "four";
      }
      else if ("5".equals(action))
      {
        return "five";
      }
      else if ("E".equals(action))
      {
        throw new IllegalStateException("!ERROR!");
      }

      return "??";
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return ex.getMessage();
    }
  }
}