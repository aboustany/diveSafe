package ca.mcgill.ecse.divesafe.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.Assignment.AssignmentStatus;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import ca.mcgill.ecse.divesafe.model.Assignment.AssignmentStatus;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

public class AssignmentController {
  private static DiveSafe diveSafe = DiveSafeApplication.getDiveSafe();


  private AssignmentController() {}

  public static List<TOAssignment> getAssignments() {
    List<TOAssignment> assignments = new ArrayList<>();

    for (var assignment : diveSafe.getAssignments()) {
      var newTOAssignment = wrapAssignment(assignment);
      assignments.add(newTOAssignment);
    }

    return assignments;
  }



  /**
   * initiateAssignment: administrator initiates the assignment of guides and days to every member possible, as
   * requested
   * 
   * @Author Shidan Javaheri
   * @return error a string error message
   */

  public static String initiateAssignment() {
    // error message variable
    String error = "";
    // iterate over every diveGuide, and every member
    for (Guide diveGuide : diveSafe.getGuides()) {
      for (Member member : diveSafe.getMembers()) {
        if (member.hasAssignment()) {
          continue;
        }
        // get the days on which the current guide is available. Calls helper method
        ArrayList<Integer> availableDays = getLeftOverDays(diveGuide);
        if (availableDays.size() == 0) {
          break;
        }
        // get number of days the member wants to dive for
        int numberOfDaysRequested = member.getNumDays();

        // if the member would like a guide
        if (member.getGuideRequired()) {

          // algorithm to find if a guide can take a member based on how many days they want to go for

          // initiate start date and end date, and ableToTakeDiving
          int startDay = availableDays.get(0);
          int endDay = availableDays.get(0);
          boolean ableToTakeDiving = false;

          // checking for enough consecutive days in the guides available days list
          for (int i = 0; i < availableDays.size(); i++) {
            // if there are enough consective days between the current start and end, stop looking.
            // set ableToTakeDiving to true
            if ((endDay + 1 - startDay) >= numberOfDaysRequested) {
              ableToTakeDiving = true;
              break;
            }

            // otherwise, set the start and end days to be the same
            // move end day forward if days are consecutive. Otherwise, stop looking (break)
            startDay = availableDays.get(i);
            endDay = availableDays.get(i);
            for (int j = i; j < availableDays.size() - 1; j++) {
              if (availableDays.get(j + 1) - availableDays.get(j) == 1) {
                endDay = availableDays.get(j + 1);
              } else {
                break;
              }
            }
          }

          // if the guide can take this member
          if (ableToTakeDiving) {

            // create a new assignment for the member. Associate a guide with this assignment
            Assignment assignment = diveSafe.addAssignment(startDay, startDay + numberOfDaysRequested - 1, member);
            assignment.setGuide(diveGuide);
          }

          // else if the member would not like a guide
        } else {
          // if there are enough days in the season, give them the first days of the season
          if (numberOfDaysRequested <= diveSafe.getNumDays()) {
            diveSafe.addAssignment(1, numberOfDaysRequested, member);
            // if there aren't enough days, then this member cannot be assigned any days
          } else {
            error = "Assignments could not be completed for all members";
            continue;
          }
        }
      }
    }
    // check to make sure all members have assignments?
    if (diveSafe.getAssignments().size() != diveSafe.getMembers().size()) {
      error = "Assignments could not be completed for all members";
    }
    try {
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }

    return error;
  }

  /**
   * @Author Elsa Chelala
   * @param userEmail
   * @return
   */
  public static String cancelTrip(String userEmail) {
    String error = ""; // error message variable
    Member member = Member.getWithEmail(userEmail); // get member
    if (member != null) {
      Assignment assignment = member.getAssignment(); // get current assignment
      boolean isCancelled = assignment.cancelTrip(); // attempt to cancel the trip
      if (!isCancelled) { // if the trip got cancelled, check for potential errors
        if (assignment.getAssignmentStatus().equals(AssignmentStatus.Banned)) {
          error = "Cannot cancel the trip due to a ban";
        } else if (assignment.getAssignmentStatus().equals(AssignmentStatus.Finished)) {
          error = "Cannot cancel a trip which has finished";
        } else {
          error = "Cannot cancel a trip which is already cancelled";
        }
      } else {
        error = "";
      }
    } else {
      error = "Member with email address " + userEmail + " does not exist";
    }
    try {
      DiveSafePersistence.save(); // save changes
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return error;
  }

  /**
   * @author Anthony Boustany
   * @param userEmail
   * @return
   */
  public static String finishTrip(String userEmail) {

    Member currentMember = Member.getWithEmail(userEmail);
    String error = "";
    // check if input Member exists
    if (currentMember != null) {

      boolean isFinished = currentMember.getAssignment().finishTrip();

      if (!isFinished) {
        // check if Member has a ban and return appropriate error message
        if (currentMember.getAssignment().getAssignmentStatus().equals(AssignmentStatus.Banned)) {
          error = "Cannot finish the trip due to a ban";
        }
        // check if Assignment was cancelled and return appropritate error message
        else if (currentMember.getAssignment().getAssignmentStatus().equals(AssignmentStatus.Cancelled)) {
          error = "Cannot finish a trip which has been cancelled";
        }

        // check if Assignment is of status not "Started" and return appropriate error message
        else {
          error = "Cannot finish a trip which has not started";
        }
      } else {
        error = "";
      }
    }
    // Error message if input Member does not exist
    else {
      error = "Member with email address " + userEmail + " does not exist";
    }
    try {
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return error;
  }

  /**
   * 
   * @Author Sami Ferneini
   * @param day
   * @return
   */
  public static String startTripsForDay(int day) {
    String error = ""; // Error variable used to store error
    List<Assignment> allAssignments = diveSafe.getAssignments(); // Getting all assignments and storing them in a list
    for (Assignment assignment : allAssignments) { // Cycling through the assignments
      boolean isStarted = assignment.startTripForDay(day); // Starting all trips for the requested date
      if (!isStarted) { // If the trip isn't started, store the relevant error message:
        // error messages only appear if the start day of the assignment matches the start day of the method
        //error if assignment is banned
        if (assignment.getAssignmentStatus().equals(AssignmentStatus.Banned) && (assignment.getStartDay() == day)) { 
          error = "Cannot start the trip due to a ban"; // Return this error
          // error if assignment is finished
        } else if (assignment.getAssignmentStatus().equals(AssignmentStatus.Finished) && (assignment.getStartDay() == day)) {
          error = "Cannot start a trip which has finished"; // Return this error
          // error if assignment is cancelled
        } else if (assignment.getAssignmentStatus().equals(AssignmentStatus.Cancelled) && (assignment.getStartDay() == day)) {
          error = "Cannot start a trip which has been cancelled"; // Return this error
          // error if assingnment has already started
        } else if (assignment.getAssignmentStatus().equals(AssignmentStatus.Started) && (assignment.getStartDay() == day)) { 
          error = "Cannot start a trip which has already started"; // Return this error
        }
      }
    }
    try {
      DiveSafePersistence.save(); // Save persistence
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return error; // Return the stored error
  }

  /**
   * @Author Karim Kanafani
   * @param userEmail
   * @param authorizationCode
   * @return
   */
  public static String confirmPayment(String userEmail, String authorizationCode) {

    String error = ""; // Empty error string

    Member memberToConfirmPayment = Member.getWithEmail(userEmail); // Get curretn member using email
    if (memberToConfirmPayment != null) { // If member exists
      Assignment currentAssignment = memberToConfirmPayment.getAssignment(); // Get current member's assignment

      boolean isPaid = currentAssignment.confirmPayment(authorizationCode); // Call the model method to see if it has
                                                                            // already been through paid

      if (!isPaid) { // If not paid
        if (currentAssignment.getAssignmentStatus().equals(AssignmentStatus.Banned)) { // Check if status = Banned
          error = "Cannot pay for the trip due to a ban"; // error message
        } else if (currentAssignment.getAssignmentStatus().equals(AssignmentStatus.Paid)) { // Check if status = Paid
          error = "Trip has already been paid for"; // error message
        } else if (currentAssignment.getAssignmentStatus().equals(AssignmentStatus.Assigned)) { // Check if status =
                                                                                                // Assigned
          error = "Invalid authorization code"; // error message
        } else if (currentAssignment.getAssignmentStatus().equals(AssignmentStatus.Finished)) { // Check if status =
                                                                                                // Finished
          error = "Cannot pay for a trip which has finished"; // error message
        } else if (currentAssignment.getAssignmentStatus().equals(AssignmentStatus.Started)) { // Check if status =
                                                                                               // Started
          error = "Trip has already been paid for"; // error message
        } else {
          error = "Cannot pay for a trip which has been cancelled"; // error message for when trip is already cancelled
        }
      }
    } else {
      error = "Member with email address " + userEmail + " does not exist"; // Member does not exist
    }
    try {
      DiveSafePersistence.save(); // Save persistence
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return error; // return error message
  }



  /**
   * This controller method toggles an assignment status to banned and returns any errors handled by the input
   * validation. We first defined a String error variable to contain all the errors that will be thrown by the method.
   * We store the member object in a variable given their email, then check if it's null. If null, we throw the error
   * that the member doesn't exist. Otherwise, we toggle the ban and set the error to be nothing with an empty string.
   * We apply our Persistence mechanism before returning the error.
   * 
   * @Author - Shyam Desai
   */
  public static String toggleBan(String userEmail) {

    String error = "";

    // Input Validation
    Member member = Member.getWithEmail(userEmail);
    if (member == null) {
      error = "Member with email address " + userEmail + " does not exist";
    } else {
      member.getAssignment().toggleBan();
      error = "";
    }

    try {
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return error;
  }

  /**
   * Helper method used to wrap the information in an <code>Assignment</code> instance in an instance of
   * <code>TOAssignment</code>.
   *EDIT: Added two attributes to the TO Object
   * @author Harrison Wang Oct 19, 2021
   * @author Shidan Javaheri April 9, 2022
   * @param assignment - The <code>Assignment</code> instance to transfer the information from.
   * @return A <code>TOAssignment</code> instance containing the information in the <code>Assignment</code> parameter.
   */
  private static TOAssignment wrapAssignment(Assignment assignment) {
    var member = assignment.getMember();

    // Initialize values for all necessary parameters.
    String memberEmail = member.getEmail();
    String memberName = member.getName();
    String guideEmail = assignment.hasGuide() ? assignment.getGuide().getEmail() : "";
    String guideName = assignment.hasGuide() ? assignment.getGuide().getName() : "";
    String hotelName = assignment.hasHotel() ? assignment.getHotel().getName() : "";

    int numDays = member.getNumDays();
    int startDay = assignment.getStartDay();
    int endDay = assignment.getEndDay();
    int totalCostForGuide = assignment.hasGuide() ? numDays * diveSafe.getPriceOfGuidePerDay() : 0;
    
    // ADDED CODE HERE FOR THREE NEW TO PROPERTIES
    
    int refund = assignment.getRefund(); 
    String authorizationCode = assignment.getAuthenticationCode(); 
    String status = assignment.getAssignmentStatusFullName(); 
    
    /*
     * Calculate the totalCostForEquipment.
     *
     * Sum the costs of all booked items depending on if they are an Equipment or EquipmentBundle instance to get the
     * equipmentCostPerDay for this assignment.
     *
     * Multiply equipmentCostPerDay by nrOfDays to get totalCostForEquipment.
     */
    int equipmentCostPerDay = 0;
    for (var bookedItem : member.getItemBookings()) {
      Item item = bookedItem.getItem();
      if (item instanceof Equipment equipment) {
        equipmentCostPerDay += equipment.getPricePerDay() * bookedItem.getQuantity();
      } else if (item instanceof EquipmentBundle bundle) {
        int bundleCost = 0;
        for (var bundledItem : bundle.getBundleItems()) {
          bundleCost += bundledItem.getEquipment().getPricePerDay() * bundledItem.getQuantity();
        }
        // Discount only applicable if assignment includes guide, so check for that before applying discount
        if (assignment.hasGuide()) {
          bundleCost = (int) (bundleCost * ((100.0 - bundle.getDiscount()) / 100.0));
        }
        equipmentCostPerDay += bundleCost * bookedItem.getQuantity();
      }
    }
    int totalCostForEquipment = equipmentCostPerDay * numDays;

    return new TOAssignment(memberEmail, memberName, guideEmail, guideName, hotelName, startDay, endDay,
        totalCostForGuide, totalCostForEquipment, authorizationCode,refund,status);
  }

  /**
   * Helper Method: Finds a guide with a given email, and returns a list of the days they are available
   * 
   * @param email the email of the guide to check availability for
   * @return availableDays an array list of all the available days the guide has
   */
  private static ArrayList<Integer> getLeftOverDays(Guide diveGuide) {

    // get the number of days in the diving season
    int numOfDaysInSeason = diveSafe.getNumDays();

    // declare the list that will store the guides available days
    ArrayList<Integer> availableDays = new ArrayList<Integer>();
    // add available days for the length of the whole season
    for (int counter = 1; counter < numOfDaysInSeason + 1; counter++) {
      availableDays.add(counter);
    }

    // get all of the guides assignments
    List<Assignment> assignments = diveGuide.getAssignments();

    // if they are not null, remove the days they have been assigned from the "available days" list
    if (assignments != null) {
      for (Assignment assignment : assignments) {
        for (int day = assignment.getStartDay(); day <= assignment.getEndDay(); day++) {
          for (int k = 0; k < availableDays.size(); k++) {
            if (availableDays.get(k) == day) {
              availableDays.remove(k);
              break;
            } ;
          }

        }
      }
    }
    // return a list of the guides available days
    return availableDays;
  }


}
