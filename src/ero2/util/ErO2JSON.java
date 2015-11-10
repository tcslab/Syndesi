package ero2.util;

import java.io.IOException;
import java.io.StringWriter;
import java.net.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL; 
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.http.client.utils.URIBuilder;

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
    String ip = "129.194.70.52";
    URIBuilder builder = null;
    URIBuilder uri_b = null;
    //uri_b.addParameter("t", "search");
    //builder.scheme("http")
	//.autority(ip+":8011")
        //.appendPath("ero2proxy")
        //.appendPath("monitor")
        //.appendQueryParam("name", "pune");
    URI urla = null;
    //uri_b.setScheme("http").setHost(ip).setPort(8011).setPath("/ero2proxy/monitor");
    URL myURL=null;

    JSONObject finalJSON = new JSONObject();
    JSONArray servicesJSON = new JSONArray();
    while (serviceKeys.hasMoreElements()) {
      String serviceLocator = serviceKeys.nextElement();
    try {
//	builder = new URIBuilder();
//	builder.setScheme("http").setHost("www.google.com").setPath("/search")
//            .setParameter("q", "httpclient")
//            .setParameter("btnG", "Google Search")
//            .setParameter("aq", "f")
//            .setParameter("oq", "");
    myURL = new URL("http://129.194.70.52:8011/");
    //uri_b = new URIBuilder("http://example.com");
    //urla = uri_b.build();
    //urla = builder.build();
    } 
    catch (MalformedURLException e) {
        // exception handler code here
        // ...
    }
      
//      try
//      {
        //uri_b.addParameter("service",serviceLocator);
//	uri_b.addParameter("service","puree");

//	uri = uri_b.build();
//      }
      //catch (URISyntaxException e)
//      catch (Exception e)
//      {
//      	System.out.println("Exception building URI [" + uri.toString() + "]");
	//out = respon
      	//e.printStackTrace(out);
      	// No point in continuing...
//      	return null;
//      }
      System.out.println(serviceLocator);
      ErO2Service service = serviceRegistry.get(serviceLocator);
      String luminance      = service.getLuminanceValue();
      String temperature    = service.getTemperatureValue();
      // []
      Vector<ErO2Resource> resources = service.getResources();
      JSONArray resourcesJSON = new JSONArray();

      JSONObject nodeJSON;
      for (ErO2Resource ero2Resource : resources) {
        if (ero2Resource.getName() != null
            && ero2Resource.getMethod() != null) {
          nodeJSON = new JSONObject();
          String hostname = "node_"+ero2Resource.getNumber()+".unige";
//!!!HACK to get either e.g. C1S2A1 or parse e.g D1S1-bulb-lightcontrol 
//!!!TO DO: CHANGE THE ACTUAL serviceLocator values and then restore the assignment variable in field node_id! 
          String node_id;
          if (serviceLocator.length() > 6) {
        	node_id = serviceLocator.substring(0,4);
          }
          else {
        	  node_id = serviceLocator;
          }
//!!! END OF HACK
       
          
          if (ero2Resource.getName().equals("curtain")) {
        	  
        	//actuation up
	    	   nodeJSON.put("hardware", "telosb");
	           nodeJSON.put("node_id", node_id);
	           nodeJSON.put("protocol", "httpUnige");
	           nodeJSON.put("ip", ip);
	           nodeJSON.put("uri", ip);
	           nodeJSON.put("hostname", hostname);
	           nodeJSON.put("type", "sensor-actuator");
	           nodeJSON.put("port", "8111");
        	
              JSONObject nodeResourceJSONactOn = new JSONObject();
              nodeResourceJSONactOn.put("data_type", "true");
              nodeResourceJSONactOn.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=up");
              nodeResourceJSONactOn.put("type", "ipso.gpio.dout");
              nodeResourceJSONactOn.put("luminance", luminance);
              nodeResourceJSONactOn.put("temperature", temperature);
              nodeResourceJSONactOn.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - ON");
              nodeResourceJSONactOn.put("unit", ero2Resource.getName()+ " control");
              nodeJSON.put("resourcesnode", nodeResourceJSONactOn);
              resourcesJSON.add(nodeJSON);
           
            //actuation down
              nodeJSON = new JSONObject();
              nodeJSON.put("hardware", "telosb");
              nodeJSON.put("node_id", node_id);
              nodeJSON.put("protocol", "httpUnige");
              nodeJSON.put("ip", ip);
              nodeJSON.put("uri", ip);
              nodeJSON.put("hostname", hostname);
              nodeJSON.put("type", "sensor-actuator");
              nodeJSON.put("port", "8111");
              
	          JSONObject nodeResourceJSONactOff = new JSONObject();
	          nodeResourceJSONactOff.put("data_type", "true");
	          nodeResourceJSONactOff.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=down");
	          nodeResourceJSONactOff.put("type", "ipso.gpio.dout");
	          nodeResourceJSONactOff.put("luminance", luminance);
	          nodeResourceJSONactOff.put("temperature", temperature);
	          nodeResourceJSONactOff.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - OFF");
	          nodeResourceJSONactOff.put("unit", ero2Resource.getName()+ " control");
	          nodeJSON.put("resourcesnode", nodeResourceJSONactOff);
	          resourcesJSON.add(nodeJSON);
	          
	        //actuation toggle
	          nodeJSON = new JSONObject();
              nodeJSON.put("hardware", "telosb");
              nodeJSON.put("node_id", node_id);
              nodeJSON.put("protocol", "httpUnige");
              nodeJSON.put("ip", ip);
              nodeJSON.put("uri", ip);
              nodeJSON.put("hostname", hostname);
              nodeJSON.put("type", "sensor-actuator");
              nodeJSON.put("port", "8111");
              
        	  JSONObject nodeResourceJSONactTog = new JSONObject();
	          nodeResourceJSONactTog.put("data_type", "true");
	          nodeResourceJSONactTog.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=toggle");
	          nodeResourceJSONactTog.put("type", "ipso.gpio.dout");
	          nodeResourceJSONactTog.put("luminance", luminance);
	          nodeResourceJSONactTog.put("temperature", temperature);
	          nodeResourceJSONactTog.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - TOGGLE");
	          nodeResourceJSONactTog.put("unit", ero2Resource.getName()+ " control");
	          nodeJSON.put("resourcesnode", nodeResourceJSONactTog);
	          resourcesJSON.add(nodeJSON);
              }
        
          	else if (ero2Resource.getName().equals("door")){
          
		        //actuation on
	        	nodeJSON = new JSONObject();
	            nodeJSON.put("hardware", "telosb");
	            nodeJSON.put("node_id", node_id);
	            nodeJSON.put("protocol", "httpUnige");
	            nodeJSON.put("ip", ip);
	            nodeJSON.put("uri", ip);
	            nodeJSON.put("hostname", hostname);
	            nodeJSON.put("type", "sensor-actuator");
	            nodeJSON.put("port", "8111");

		          JSONObject nodeResourceJSONactOn = new JSONObject();
		          nodeResourceJSONactOn.put("data_type", "true");
		          nodeResourceJSONactOn.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=on");
		          nodeResourceJSONactOn.put("type", "ipso.gpio.dout");
		          nodeResourceJSONactOn.put("luminance", luminance);
		          nodeResourceJSONactOn.put("temperature", temperature);
		          nodeResourceJSONactOn.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - ON");
		          nodeResourceJSONactOn.put("unit", ero2Resource.getName()+ " control");
		          nodeJSON.put("resourcesnode", nodeResourceJSONactOn);
		          resourcesJSON.add(nodeJSON);
          	}
	        else {
	        	//actuation on
	        	nodeJSON = new JSONObject();
	            nodeJSON.put("hardware", "telosb");
	            nodeJSON.put("node_id", node_id);
	            nodeJSON.put("protocol", "httpUnige");
	            nodeJSON.put("ip", ip);
	            nodeJSON.put("uri", ip);
	            nodeJSON.put("hostname", hostname);
	            nodeJSON.put("type", "sensor-actuator");
	            nodeJSON.put("port", "8111");

		          JSONObject nodeResourceJSONactOn = new JSONObject();
		          nodeResourceJSONactOn.put("data_type", "true");
		          nodeResourceJSONactOn.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=on");
		          nodeResourceJSONactOn.put("type", "ipso.gpio.dout");
		          nodeResourceJSONactOn.put("luminance", luminance);
		          nodeResourceJSONactOn.put("temperature", temperature);
		          nodeResourceJSONactOn.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - ON");
		          nodeResourceJSONactOn.put("unit", ero2Resource.getName()+ " control");
		          nodeJSON.put("resourcesnode", nodeResourceJSONactOn);
		          resourcesJSON.add(nodeJSON);
		          
		        //actuation off
		        	nodeJSON = new JSONObject();
		            nodeJSON.put("hardware", "telosb");
		            nodeJSON.put("node_id", node_id);
		            nodeJSON.put("protocol", "httpUnige");
		            nodeJSON.put("ip", ip);
		            nodeJSON.put("uri", ip);
		            nodeJSON.put("hostname", hostname);
		            nodeJSON.put("type", "sensor-actuator");
		            nodeJSON.put("port", "8111");

			          JSONObject nodeResourceJSONactOff = new JSONObject();
			          nodeResourceJSONactOff.put("data_type", "true");
			          nodeResourceJSONactOff.put("path", "/ero2proxy/mediate?service=" + node_id + "&resource=" + ero2Resource.getName() + "&status=off");
			          nodeResourceJSONactOff.put("type", "ipso.gpio.dout");
			          nodeResourceJSONactOff.put("luminance", luminance);
			          nodeResourceJSONactOff.put("temperature", temperature);
			          nodeResourceJSONactOff.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id + " - OFF");
			          nodeResourceJSONactOff.put("unit", ero2Resource.getName()+ " control");
			          nodeJSON.put("resourcesnode", nodeResourceJSONactOff);
			          resourcesJSON.add(nodeJSON);
		          
	        	}
    	  nodeJSON = new JSONObject();
		  nodeJSON.put("hardware", "telosb");
		  nodeJSON.put("node_id", node_id);
		  nodeJSON.put("protocol", "httpUnige");
		  nodeJSON.put("ip", ip);
		  nodeJSON.put("uri", ip); //put ip instead of myURL.tostring because getNodes() in sfawrap takes the ip from here..  
		  nodeJSON.put("hostname", hostname);
		  nodeJSON.put("type", "sensor-actuator");
		  nodeJSON.put("port", "8111");
		//luminance sensor
		  JSONObject nodeResourceJSONlumSen = new JSONObject();
		  nodeResourceJSONlumSen.put("data_type", "true");
		  nodeResourceJSONlumSen.put("path", "/ero2proxy/monitor?service=" + node_id);
		  nodeResourceJSONlumSen.put("type", "ipso.sen.il");
		  nodeResourceJSONlumSen.put("luminance", luminance);
		  nodeResourceJSONlumSen.put("temperature", temperature);
		  nodeResourceJSONlumSen.put("name", ero2Resource.getName() + " at UNIGE with NID: " + node_id);
		  nodeResourceJSONlumSen.put("unit", "sensor-value");
		  nodeJSON.put("resourcesnode",nodeResourceJSONlumSen);
		  resourcesJSON.add(nodeJSON);
          
          
        }
      }
      JSONObject serviceJSON = new JSONObject();
      /*  serviceJSON.put("serviceID", serviceLocator); */
       serviceJSON.put("resources", resourcesJSON);

       servicesJSON.add(serviceJSON);
      // {service
      
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
    return jsonText;
  }

  @SuppressWarnings("unchecked")
  public static String getTestbedInfoJSONString(){
    String name      = "unigetestbed";
    double longitude = 46.1767058;
    double latitude  = 6.1397209;
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
