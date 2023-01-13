/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.model;

// line 1 "../../../../../DiveSafeStateMachine.ump"
// line 130 "../../../../../DiveSafe.ump"
// line 231 "../../../../../DiveSafe.ump"
public class Assignment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Assignment Attributes
  private int refund;
  private String authenticationCode;
  private int startDay;
  private int endDay;

  //Assignment State Machines
  public enum AssignmentStatus { Assigned, Paid, Banned, Cancelled, Started, Finished }
  private AssignmentStatus assignmentStatus;

  //Assignment Associations
  private Member member;
  private Guide guide;
  private Hotel hotel;
  private DiveSafe diveSafe;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Assignment(int aStartDay, int aEndDay, Member aMember, DiveSafe aDiveSafe)
  {
    refund = 0;
    authenticationCode = "";
    startDay = aStartDay;
    endDay = aEndDay;
    boolean didAddMember = setMember(aMember);
    if (!didAddMember)
    {
      throw new RuntimeException("Unable to create assignment due to member. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddDiveSafe = setDiveSafe(aDiveSafe);
    if (!didAddDiveSafe)
    {
      throw new RuntimeException("Unable to create assignment due to diveSafe. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setAssignmentStatus(AssignmentStatus.Assigned);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRefund(int aRefund)
  {
    boolean wasSet = false;
    refund = aRefund;
    wasSet = true;
    return wasSet;
  }

  public boolean setAuthenticationCode(String aAuthenticationCode)
  {
    boolean wasSet = false;
    authenticationCode = aAuthenticationCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDay(int aStartDay)
  {
    boolean wasSet = false;
    startDay = aStartDay;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDay(int aEndDay)
  {
    boolean wasSet = false;
    endDay = aEndDay;
    wasSet = true;
    return wasSet;
  }

  /**
   * authors: Shidan Javaheri, Karim Kanafani, Anthony Boustany, Sami Ferneini, Elsa Chelala, Shyam Desai
   */
  public int getRefund()
  {
    return refund;
  }

  public String getAuthenticationCode()
  {
    return authenticationCode;
  }

  public int getStartDay()
  {
    return startDay;
  }

  public int getEndDay()
  {
    return endDay;
  }

  public String getAssignmentStatusFullName()
  {
    String answer = assignmentStatus.toString();
    return answer;
  }

  public AssignmentStatus getAssignmentStatus()
  {
    return assignmentStatus;
  }

  public boolean toggleBan()
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        setAssignmentStatus(AssignmentStatus.Banned);
        wasEventProcessed = true;
        break;
      case Banned:
        setAssignmentStatus(AssignmentStatus.Assigned);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startTripForDay(int day)
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        if (this.getStartDay()==day)
        {
          setAssignmentStatus(AssignmentStatus.Banned);
          wasEventProcessed = true;
          break;
        }
        break;
      case Paid:
        if (this.getStartDay()==day)
        {
          setAssignmentStatus(AssignmentStatus.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean confirmPayment(String code)
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        if (!"".equals(code))
        {
        // line 12 "../../../../../DiveSafeStateMachine.ump"
          this.authenticationCode = code;
          setAssignmentStatus(AssignmentStatus.Paid);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelTrip()
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Assigned:
        // line 13 "../../../../../DiveSafeStateMachine.ump"
        this.refund = 100;
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Paid:
        // line 19 "../../../../../DiveSafeStateMachine.ump"
        this.refund = 50;
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      case Started:
        // line 33 "../../../../../DiveSafeStateMachine.ump"
        this.refund = 10;
        setAssignmentStatus(AssignmentStatus.Cancelled);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishTrip()
  {
    boolean wasEventProcessed = false;
    
    AssignmentStatus aAssignmentStatus = assignmentStatus;
    switch (aAssignmentStatus)
    {
      case Started:
        setAssignmentStatus(AssignmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setAssignmentStatus(AssignmentStatus aAssignmentStatus)
  {
    assignmentStatus = aAssignmentStatus;
  }
  /* Code from template association_GetOne */
  public Member getMember()
  {
    return member;
  }
  /* Code from template association_GetOne */
  public Guide getGuide()
  {
    return guide;
  }

  public boolean hasGuide()
  {
    boolean has = guide != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Hotel getHotel()
  {
    return hotel;
  }

  public boolean hasHotel()
  {
    boolean has = hotel != null;
    return has;
  }
  /* Code from template association_GetOne */
  public DiveSafe getDiveSafe()
  {
    return diveSafe;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setMember(Member aNewMember)
  {
    boolean wasSet = false;
    if (aNewMember == null)
    {
      //Unable to setMember to null, as assignment must always be associated to a member
      return wasSet;
    }
    
    Assignment existingAssignment = aNewMember.getAssignment();
    if (existingAssignment != null && !equals(existingAssignment))
    {
      //Unable to setMember, the current member already has a assignment, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Member anOldMember = member;
    member = aNewMember;
    member.setAssignment(this);

    if (anOldMember != null)
    {
      anOldMember.setAssignment(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setGuide(Guide aGuide)
  {
    boolean wasSet = false;
    Guide existingGuide = guide;
    guide = aGuide;
    if (existingGuide != null && !existingGuide.equals(aGuide))
    {
      existingGuide.removeAssignment(this);
    }
    if (aGuide != null)
    {
      aGuide.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setHotel(Hotel aHotel)
  {
    boolean wasSet = false;
    Hotel existingHotel = hotel;
    hotel = aHotel;
    if (existingHotel != null && !existingHotel.equals(aHotel))
    {
      existingHotel.removeAssignment(this);
    }
    if (aHotel != null)
    {
      aHotel.addAssignment(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setDiveSafe(DiveSafe aDiveSafe)
  {
    boolean wasSet = false;
    if (aDiveSafe == null)
    {
      return wasSet;
    }

    DiveSafe existingDiveSafe = diveSafe;
    diveSafe = aDiveSafe;
    if (existingDiveSafe != null && !existingDiveSafe.equals(aDiveSafe))
    {
      existingDiveSafe.removeAssignment(this);
    }
    diveSafe.addAssignment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Member existingMember = member;
    member = null;
    if (existingMember != null)
    {
      existingMember.setAssignment(null);
    }
    if (guide != null)
    {
      Guide placeholderGuide = guide;
      this.guide = null;
      placeholderGuide.removeAssignment(this);
    }
    if (hotel != null)
    {
      Hotel placeholderHotel = hotel;
      this.hotel = null;
      placeholderHotel.removeAssignment(this);
    }
    DiveSafe placeholderDiveSafe = diveSafe;
    this.diveSafe = null;
    if(placeholderDiveSafe != null)
    {
      placeholderDiveSafe.removeAssignment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "refund" + ":" + getRefund()+ "," +
            "authenticationCode" + ":" + getAuthenticationCode()+ "," +
            "startDay" + ":" + getStartDay()+ "," +
            "endDay" + ":" + getEndDay()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "member = "+(getMember()!=null?Integer.toHexString(System.identityHashCode(getMember())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "guide = "+(getGuide()!=null?Integer.toHexString(System.identityHashCode(getGuide())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "hotel = "+(getHotel()!=null?Integer.toHexString(System.identityHashCode(getHotel())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "diveSafe = "+(getDiveSafe()!=null?Integer.toHexString(System.identityHashCode(getDiveSafe())):"null");
  }
}