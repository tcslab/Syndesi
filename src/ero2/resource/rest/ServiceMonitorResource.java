package ero2.resource.rest;

import java.util.StringTokenizer;

import ch.ethz.inf.vs.californium.coap.POSTRequest;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.registries.CodeRegistry;
import ch.ethz.inf.vs.californium.coap.registries.MediaTypeRegistry;
import ch.ethz.inf.vs.californium.endpoint.resources.LocalResource;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import ero2.core.ErO2Resource;
import ero2.core.ErO2Service;
import ero2.core.ErO2Registry;
import ero2.core.ErO2ServiceStatus;
import ero2.transport.coap.ErO2Utils;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceMonitorResource extends LocalResource {

  private ErO2Registry ERO2REGISTRY;

  public ServiceMonitorResource(String custom, String title, String rt) {
    super(custom);
    setTitle(title);
    setResourceType(rt);
    ERO2REGISTRY = ErO2Registry.getInstance();
  }

  public ServiceMonitorResource() {
    this("monitor", "Monitoring Service", "MonitorServiceDisplayer");
  }

  public void performPOST(POSTRequest request) {
    Response response = new Response(CodeRegistry.RESP_CONTENT);

    String sqkey = request.sequenceKey();
    String ipaddr = request.sequenceKey().substring(0,
        sqkey.lastIndexOf(':'));
    String payloadString = request.getPayloadString();
    //payloadString => B1S1-bulb-lightcontrol|334|22|���RP�D�4[.`�
    String serviceLocator = payloadString.substring(0, payloadString.indexOf(ErO2Service.DELIMITER));

    String parameters = payloadString.substring(0);
    String[] params = parameters.split(ErO2Service.DELIMITER);
    String resourceID = params[0];

    // Parse sensor readings hack for now :)
    String sensorReadings = payloadString.substring(
        payloadString.indexOf(ErO2Service.DELIMITER) + 1,
        payloadString.lastIndexOf(ErO2Service.DELIMITER));
    // Luminance and Temperature separated by ErO2Service.DELIMITER
    System.out.println(sensorReadings);

    System.out.println("Hearbeat receving from " + serviceLocator
        + " with light" + " temp");

    if (ERO2REGISTRY.searchService(serviceLocator) != null) {

      // Prepare service status
      ErO2ServiceStatus status = new ErO2ServiceStatus();
      status.setIPAddr(ipaddr);

      int i = 1;
      String lum = "", temp = "";

      // updates the service monitor registry
      StringTokenizer tokenizer = new StringTokenizer(sensorReadings,
          ErO2Service.DELIMITER);
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextElement().toString();
        switch (i) {
        case 1:
          status.setReading("luminocity", token);
          System.out.println("lum" + token);
          lum = token;
          break;
        case 2:
          status.setReading("temp", token);
          temp = token;
          System.out.println("temp" + token);
          break;
        }
        i++;
      }

      ERO2REGISTRY.updateService(serviceLocator, lum, temp);

      ERO2REGISTRY.updateStatus(serviceLocator, status);
      System.out.println("Ero2 service updated " + serviceLocator + " "
          + ipaddr + " " + lum + " " + temp);
      //B1S2-bulb-lightcontrol|388|22|���[�e
      if(serviceLocator.contains("-")){
        serviceLocator = serviceLocator.substring(serviceLocator.indexOf('-'),-1);
      }
      this.updateSensors(serviceLocator,new Integer(lum),new Integer(temp));

      response.setContentType(MediaTypeRegistry.TEXT_PLAIN);
      request.respond(response);
    } else {
      response.setPayload(ErO2Utils.REREGISTRATION_REQUIRED);
      response.setContentType(MediaTypeRegistry.TEXT_PLAIN);
      request.respond(response);
    }
  }

  private void updateSensors(String incoming_locator, int luminance,int temperature){

    System.out.println("Salut maman; ServiceLocator = "+ incoming_locator+ "; Luminance= "+ luminance+"; Temperature= "+ temperature);
    Hashtable<String, ErO2Service> serviceRegistry=ERO2REGISTRY.allServices();
    Enumeration<String> serviceKeys = serviceRegistry.keys();

    while (serviceKeys.hasMoreElements()) {
      String serviceLocator = serviceKeys.nextElement();
      if(incoming_locator.equals(serviceLocator)){
        ErO2Service service = serviceRegistry.get(serviceLocator);
        Vector<ErO2Resource> resources = service.getResources();

      }
    }
  }

}

