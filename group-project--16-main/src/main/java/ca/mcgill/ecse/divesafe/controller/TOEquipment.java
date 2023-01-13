/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;
import javafx.scene.control.Spinner;

/**
 * @Author : Shidan Javaheri
 */
// line 18 "../../../../../DiveSafeTransferObjects.ump"
public class TOEquipment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOEquipment Attributes
  private String name;
  private int weight;
  private int pricePerDay;
  private Spinner equipmentQuantity;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOEquipment(String aName, int aWeight, int aPricePerDay, Spinner aEquipmentQuantity)
  {
    name = aName;
    weight = aWeight;
    pricePerDay = aPricePerDay;
    equipmentQuantity = aEquipmentQuantity;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setWeight(int aWeight)
  {
    boolean wasSet = false;
    weight = aWeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setPricePerDay(int aPricePerDay)
  {
    boolean wasSet = false;
    pricePerDay = aPricePerDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setEquipmentQuantity(Spinner aEquipmentQuantity)
  {
    boolean wasSet = false;
    equipmentQuantity = aEquipmentQuantity;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public int getWeight()
  {
    return weight;
  }

  public int getPricePerDay()
  {
    return pricePerDay;
  }

  public Spinner getEquipmentQuantity()
  {
    return equipmentQuantity;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "weight" + ":" + getWeight()+ "," +
            "pricePerDay" + ":" + getPricePerDay()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "equipmentQuantity" + "=" + (getEquipmentQuantity() != null ? !getEquipmentQuantity().equals(this)  ? getEquipmentQuantity().toString().replaceAll("  ","    ") : "this" : "null");
  }
}