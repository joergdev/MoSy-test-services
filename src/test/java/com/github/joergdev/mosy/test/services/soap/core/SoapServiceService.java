
package com.github.joergdev.mosy.test.services.soap.core;

import java.net.MalformedURLException;
import java.net.URL;
import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "SoapService", targetNamespace = "http://services.test.mosy.joergdev.github.com/")
@HandlerChain(file = "HandlerChain.xml")
public class SoapServiceService extends Service
{
  private static URL getURL(String url)
  {
    try
    {
      return new URL(url);
    }
    catch (MalformedURLException e)
    {
      throw new IllegalStateException(e);
    }
  }

  /**
   * constructor
   * @param url 
   */
  SoapServiceService(String url)
  {
    super(getURL(url), new QName("http://services.test.mosy.joergdev.github.com/", "SoapService"));
  }

  @WebEndpoint(name = "SoapServicePort")
  SoapService getSoapServicePort()
  {
    return getPort(new QName("http://services.test.mosy.joergdev.github.com/", "SoapServicePort"),
        SoapService.class);
  }

  @WebEndpoint(name = "SoapServicePort")
  SoapService getSoapServicePort(WebServiceFeature... features)
  {
    return getPort(new QName("http://services.test.mosy.joergdev.github.com/", "SoapServicePort"),
        SoapService.class, features);
  }
}