package ero2.core;

import java.util.Vector;

public class ErO2Service {
  private String ipaddr;
  private String serviceLocator;
  private String luminance_value;
  private String temperature_value;
  private String humidity_value;
  Vector<ErO2Resource> resources = new Vector<ErO2Resource>();

  public static String DELIMITER = "|";
  public static String COMMA = ",";
  public static String NULL = "(null)";

  public ErO2Service() {
  }

  public ErO2Service(String ipaddr) {
    this.ipaddr = ipaddr;
    this.luminance_value = null;
    this.temperature_value = null;
  }

  public void addResource(ErO2Resource resource) {
    resources.add(resource);
  }

  public void removeResource(ErO2Resource resource) {
    resources.removeElement(resource);
  }

  public Vector<ErO2Resource> getResources() {
    return resources;
  }

  public ErO2Resource getResourceByName(String resourceName) {
    if (resourceName != null) {
      for (ErO2Resource resource : resources) {
        if (resourceName.equals(resource.getName())) {
          return resource;
        }
      }
    }
    return null;
  }

  public String getIPAddress() {
    return ipaddr;
  }

  public String getServiceLocator() {
    return serviceLocator;
  }

  public String getLuminanceValue() {
    return luminance_value;
  }

  public String getTemperatureValue() {
    return temperature_value;
  }

  public void setLuminance(String luminance_value){
    this.luminance_value = luminance_value;
  }

  public void setTemperature(String temperature_value){
    this.temperature_value = temperature_value;
  }

  public String getSerialization() {
    String serviceString = ipaddr;
    for (ErO2Resource resource : resources) {
      serviceString = serviceString + DELIMITER + resource.getName()
          + DELIMITER + resource.getURI() + DELIMITER
          + resource.getMethod() + DELIMITER + resource.getQueryParameters()
          + DELIMITER;
    }
    return serviceString;
  }

}
