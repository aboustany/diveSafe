/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;

/**
 * @Author : Shidan Javaheri
 */
// line 36 "../../../../../DiveSafeTransferObjects.ump"
public class TOMember
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOMember Attributes
  private String email;
  private String name;
  private String emergencyContact;
  private int numberOfDays;
  private boolean guideRequired;
  private boolean hotelRequired;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOMember(String aEmail, String aName, String aEmergencyContact, int aNumberOfDays, boolean aGuideRequired, boolean aHotelRequired)
  {
    email = aEmail;
    name = aName;
    emergencyContact = aEmergencyContact;
    numberOfDays = aNumberOfDays;
    guideRequired = aGuideRequired;
    hotelRequired = aHotelRequired;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmergencyContact(String aEmergencyContact)
  {
    boolean wasSet = false;
    emergencyContact = aEmergencyContact;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumberOfDays(int aNumberOfDays)
  {
    boolean wasSet = false;
    numberOfDays = aNumberOfDays;
    wasSet = true;
    return wasSet;
  }

  public boolean setGuideRequired(boolean aGuideRequired)
  {
    boolean wasSet = false;
    guideRequired = aGuideRequired;
    wasSet = true;
    return wasSet;
  }

  public boolean setHotelRequired(boolean aHotelRequired)
  {
    boolean wasSet = false;
    hotelRequired = aHotelRequired;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getName()
  {
    return name;
  }

  public String getEmergencyContact()
  {
    return emergencyContact;
  }

  public int getNumberOfDays()
  {
    return numberOfDays;
  }

  public boolean getGuideRequired()
  {
    return guideRequired;
  }

  public boolean getHotelRequired()
  {
    return hotelRequired;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isGuideRequired()
  {
    return guideRequired;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isHotelRequired()
  {
    return hotelRequired;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "name" + ":" + getName()+ "," +
            "emergencyContact" + ":" + getEmergencyContact()+ "," +
            "numberOfDays" + ":" + getNumberOfDays()+ "," +
            "guideRequired" + ":" + getGuideRequired()+ "," +
            "hotelRequired" + ":" + getHotelRequired()+ "]";
  }
}