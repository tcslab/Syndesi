package ero2.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ero2.core.ErO2Resource;
import ero2.core.ErO2Service;
import ero2.core.ErO2ServiceStatus;

public class ErO2JSON {

  @SuppressWarnings("unchecked")
  public static String getServicesJSONString(
      Hashtable<String, ErO2Service> serviceRegistry) {

    Enumeration<String> serviceKeys = serviceRegistry.keys();

    JSONObject finalJSON = new JSONObject();
    JSONArray servicesJSON = new JSONArray();
    while (serviceKeys.hasMoreElements()) {
      String serviceLocator = serviceKeys.nextElement();
      System.out.println(serviceLocator);
      ErO2Service service = serviceRegistry.get(serviceLocator);
      // []
      Vector<ErO2Resource> resources = service.getResources();
      JSONArray resourcesJSON = new JSONArray();

      JSONObject nodeJSON;
      for (ErO2Resource ero2Resource : resources) {
        if (ero2Resource.getName() != null
            && ero2Resource.getMethod() != null) {
          String luminance      = ero2Resource.getLuminanceValue();
          String temperature    = ero2Resource.getTemperatureValue();
          nodeJSON = new JSONObject();
          //TODO replace these hardcoded settings in the future
          String hostname = "node_"+ero2Resource.getNumber()+".unige";
          nodeJSON.put("hardware", "telosb");
          nodeJSON.put("node_id", serviceLocator);
          nodeJSON.put("protocol", "coap");
          nodeJSON.put("ip", "129.194.69.241");
          nodeJSON.put("hostname", hostname);
          nodeJSON.put("type", "unige-node");
          nodeJSON.put("port", "8111");
         /*  nodeJSON.put("method", ero2Resource.getMethod()); */
         /*  nodeJSON.put("uri", ero2Resource.getURI()); */
          /* nodeJSON.put("params", */
          /*     ero2Resource.getQueryParameters()); */
          JSONObject nodeResourceJSON = new JSONObject();
          nodeResourceJSON.put("data_type", "s");
          nodeResourceJSON.put("path", "dev0");
          nodeResourceJSON.put("type", "s");
          nodeResourceJSON.put("luminance", luminance);
          nodeResourceJSON.put("temperature", temperature);
          nodeResourceJSON.put("name", ero2Resource.getName());
          nodeResourceJSON.put("unit", "");
          nodeJSON.put("resourcesnode", nodeResourceJSON);
          resourcesJSON.add(nodeJSON);
        }
      }

      // {service
      JSONObject serviceJSON = new JSONObject();
     /*  serviceJSON.put("serviceID", serviceLocator); */
      serviceJSON.put("resources", resourcesJSON);

      servicesJSON.add(serviceJSON);
    }
    finalJSON.put("services", servicesJSON);

    StringWriter out = new StringWriter();
    try {
      finalJSON.writeJSONString(out);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String jsonText = out.toString();
    System.out.println(jsonText);
    return jsonText;
  }

  @SuppressWarnings("unchecked")
  public static String getTestbedInfoJSONString(){
    String name      = "unigetestbed";
    double longitude = 46.176685;
    double latitude  = 6.140571;
    String domain    = "iot.unige.ch:8111";
    JSONObject finalJSON = new JSONObject();
    finalJSON.put("name", name);
    finalJSON.put("longitude", longitude);
    finalJSON.put("latitude", latitude);
    finalJSON.put("domain", domain);
    StringWriter out = new StringWriter();
    try {
      finalJSON.writeJSONString(out);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String jsonText = out.toString();
    System.out.println(jsonText);
    return jsonText;
  }

  @SuppressWarnings("unchecked")
  public static String getUpdatesJSONString(String serviceLocator,
      ErO2ServiceStatus status) {
    JSONObject readings = new JSONObject();
    readings.put("service", serviceLocator);
    Enumeration<String> readingsKeys = status.getReadings().keys();
    while (readingsKeys.hasMoreElements()) {
      String key = readingsKeys.nextElement();
      readings.put(key, status.getReadings().get(key));
    }

    StringWriter out = new StringWriter();
    try {
      readings.writeJSONString(out);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String jsonText = out.toString();
    return jsonText;
  }

  @SuppressWarnings("unchecked")
  public static String getTestbedSensorValuesJSONString(Hashtable<String, ErO2Service> serviceRegistry) {

    Enumeration<String> serviceKeys = serviceRegistry.keys();

    JSONObject finalJSON   = new JSONObject();
    JSONArray servicesJSON = new JSONArray();
    while (serviceKeys.hasMoreElements()) {
      String serviceLocator = serviceKeys.nextElement();
      ErO2Service ero2ser   = serviceRegistry.get(serviceLocator);
      String luminance      = ero2ser.getLuminanceValue();
      String temperature    = ero2ser.getTemperatureValue();


      JSONObject nodeJSON   = new JSONObject();
      nodeJSON.put("node", serviceLocator);
      nodeJSON.put("luminance", luminance);
      nodeJSON.put("temperature", temperature);
      servicesJSON.add(nodeJSON);
    }
    finalJSON.put("services", servicesJSON);

    StringWriter out = new StringWriter();
    try {
      finalJSON.writeJSONString(out);
    } catch (IOException e) {
      e.printStackTrace();
    }
    String jsonText = out.toString();
    System.out.println(jsonText);
    return jsonText;
  }

}
