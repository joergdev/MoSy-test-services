
package de.joergdev.mosy.test.services.soap.core;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "SoapService", targetNamespace = "http://services.test.mosy.joergdev.de/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SoapService
{
  @WebMethod
  @WebResult(partName = "return")
  public String testMethod(@WebParam(name = "action", partName = "action") String action);
}