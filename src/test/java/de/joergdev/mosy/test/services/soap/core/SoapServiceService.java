
package de.joergdev.mosy.test.services.soap.core;

import java.net.MalformedURLException;
import java.net.URL;
import javax.jws.HandlerChain;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import de.joergdev.mosy.shared.Utils;

@WebServiceClient(name = "SoapService", targetNamespace = "http://services.test.mosy.joergdev.de/")
@HandlerChain(file = "HandlerChain.xml")
public class SoapServiceService extends Service
{
  private static URL getURL(String url)
  {
    // no url => use local wsdl
    // used for multi-tenancy to avoid ?wsdl request without tenantId in http header (this would throw an error)
    if (Utils.isEmpty(url))
    {
      URL wsdlLocation = SoapServiceService.class.getResource("/SoapService.wsdl");

      return wsdlLocation;
    }
    else
    {
      try
      {
        return new URL(url);
      }
      catch (MalformedURLException ex)
      {
        throw new IllegalStateException(ex);
      }
    }
  }

  /**
   * constructor
   * 
   * @param url 
   */
  SoapServiceService(String url)
  {
    super(getURL(url), new QName("http://services.test.mosy.joergdev.de/", "SoapService"));
  }

  @WebEndpoint(name = "SoapServicePort")
  SoapService getSoapServicePort()
  {
    return getPort(new QName("http://services.test.mosy.joergdev.de/", "SoapServicePort"), SoapService.class);
  }

  @WebEndpoint(name = "SoapServicePort")
  SoapService getSoapServicePort(WebServiceFeature... features)
  {
    return getPort(new QName("http://services.test.mosy.joergdev.de/", "SoapServicePort"), SoapService.class, features);
  }
}
