/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;
import javafx.scene.control.Spinner;

/**
 * @Author : Shidan Javaheri
 */
// line 27 "../../../../../DiveSafeTransferObjects.ump"
public class TOBundle
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBundle Attributes
  private String name;
  private int discount;
  private int pricePerDay;
  private Spinner bundleQuantity;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBundle(String aName, int aDiscount, int aPricePerDay, Spinner aBundleQuantity)
  {
    name = aName;
    discount = aDiscount;
    pricePerDay = aPricePerDay;
    bundleQuantity = aBundleQuantity;
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

  public boolean setDiscount(int aDiscount)
  {
    boolean wasSet = false;
    discount = aDiscount;
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

  public boolean setBundleQuantity(Spinner aBundleQuantity)
  {
    boolean wasSet = false;
    bundleQuantity = aBundleQuantity;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public int getDiscount()
  {
    return discount;
  }

  public int getPricePerDay()
  {
    return pricePerDay;
  }

  public Spinner getBundleQuantity()
  {
    return bundleQuantity;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "discount" + ":" + getDiscount()+ "," +
            "pricePerDay" + ":" + getPricePerDay()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "bundleQuantity" + "=" + (getBundleQuantity() != null ? !getBundleQuantity().equals(this)  ? getBundleQuantity().toString().replaceAll("  ","    ") : "this" : "null");
  }
}