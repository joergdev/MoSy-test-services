package de.joergdev.mosy.test.services.soap.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import de.joergdev.mosy.api.APIConstants;

public class HttpHeaderExtensionSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
  @Override
  public boolean handleMessage(SOAPMessageContext context)
  {
    Integer tenantId = SoapServiceClientPortSingleton.SOAP_TENANT_ID.get();
    String mockProfileName = SoapServiceClientPortSingleton.SOAP_MOCK_PROFILE_NAME.get();
    Integer recordSessionID = SoapServiceClientPortSingleton.SOAP_RECORD_SESSION_ID.get();

    if (Boolean.TRUE.equals(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) && (tenantId != null || mockProfileName != null || recordSessionID != null))
    {
      @SuppressWarnings("unchecked")
      Map<String, List<String>> requestHeaders = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
      if (requestHeaders == null)
      {
        requestHeaders = new HashMap<>();
        context.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
      }

      if (tenantId != null)
      {
        requestHeaders.put(APIConstants.HTTP_HEADER_TENANT_ID, Collections.singletonList(String.valueOf(tenantId)));
      }

      if (mockProfileName != null)
      {
        requestHeaders.put(APIConstants.HTTP_HEADER_MOCK_PROFILE_NAME, Collections.singletonList(mockProfileName));
      }

      if (recordSessionID != null)
      {
        requestHeaders.put(APIConstants.HTTP_HEADER_RECORD_SESSION_ID, Collections.singletonList(String.valueOf(recordSessionID)));
      }
    }

    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context)
  {
    return true;
  }

  @Override
  public void close(MessageContext context)
  {}

  @Override
  public Set<QName> getHeaders()
  {
    return Collections.emptySet();
  }

}
