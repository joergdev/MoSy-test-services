package com.github.joergdev.mosy.test.services.soap.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import com.github.joergdev.mosy.api.APIConstants;

public class HttpHeaderExtensionSOAPHandler implements SOAPHandler<SOAPMessageContext>
{
  @Override
  public boolean handleMessage(SOAPMessageContext context)
  {
    String mockProfileName = SoapServiceClientPortSingleton.SOAP_MOCK_PROFILE_NAME.get();
    Integer recordSessionID = SoapServiceClientPortSingleton.SOAP_RECORD_SESSION_ID.get();

    if (Boolean.TRUE.equals(context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
        && (mockProfileName != null || recordSessionID != null))
    {
      @SuppressWarnings("unchecked")
      Map<String, List<String>> requestHeaders = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_REQUEST_HEADERS);
      if (requestHeaders == null)
      {
        requestHeaders = new HashMap<>();
        context.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
      }

      if (mockProfileName != null)
      {
        requestHeaders.put(APIConstants.HTTP_HEADER_MOCK_PROFILE_NAME,
            Collections.singletonList(mockProfileName));
      }

      if (recordSessionID != null)
      {
        requestHeaders.put(APIConstants.HTTP_HEADER_RECORD_SESSION_ID,
            Collections.singletonList(String.valueOf(recordSessionID)));
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