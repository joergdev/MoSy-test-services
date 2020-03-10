
package com.github.joergdev.mosy.test.services.soap.core;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "SoapService", targetNamespace = "http://services.test.mosy.joergdev.github.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SoapService
{
  @WebMethod
  @WebResult(partName = "return")
  public String testMethod(@WebParam(name = "action", partName = "action") String action);
}