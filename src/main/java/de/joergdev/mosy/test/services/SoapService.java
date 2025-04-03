package de.joergdev.mosy.test.services;

import jakarta.xml.ws.Endpoint;

public class SoapService
{
  public static void main(String[] args)
  {
    try
    {
      String url = "http://0.0.0.0:5432/soap";

      Endpoint.publish(url, new SoapServiceImpl());

      System.out.println("SoapService running, Endpoint: " + url);
    }
    catch (Throwable th)
    {
      th.printStackTrace();
    }
  }
}