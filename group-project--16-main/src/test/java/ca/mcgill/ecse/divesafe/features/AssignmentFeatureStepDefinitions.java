package ca.mcgill.ecse.divesafe.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.model.DiveSafe;
import ca.mcgill.ecse.divesafe.model.Equipment;
import ca.mcgill.ecse.divesafe.model.Assignment;
import ca.mcgill.ecse.divesafe.model.EquipmentBundle;
import ca.mcgill.ecse.divesafe.model.Guide;
import ca.mcgill.ecse.divesafe.model.Item;
import ca.mcgill.ecse.divesafe.model.Member;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class AssignmentFeatureStepDefinitions {

  private DiveSafe diveSafe;

  private String error;

  /**
   * Helper Method: Returns an equipment with a given name
   * 
   * @param name the name of the equipment object
   * @Author Shidan Javaheri
   */
  private Equipment returnEquipment(String name) {
    // get the list of all equipment in the system
    List<Equipment> equipmentList = diveSafe.getEquipments();
    int position = 0;
    // search the list for an equipment item with a matching name
    // find the index in the list of that equipment and return it
    for (Equipment equipment : equipmentList) {
      if (equipment.getName().equals(name)) {
        position = diveSafe.indexOfEquipment(equipment);
        return diveSafe.getEquipment(position);
      }
    }
    // otherwise if no equipment exists with this name, return null
    return null;
  }

  /**
   * Helper Method: Returns an equipment bundle with a given name
   * 
   * @param name the name of the EquipmentBundle object
   * @Author Shidan Javaheri
   */
  private EquipmentBundle returnBundle(String name) {
    // get a list of all bundles in the system
    List<EquipmentBundle> bundleList = diveSafe.getBundles();
    int position = 0;
    // find a bundle with a matching name and return it
    for (EquipmentBundle bundle : bundleList) {
      if (bundle.getName().equals(name)) {
        position = diveSafe.indexOfBundle(bundle);
        return diveSafe.getBundle(position);
      }
    }
    // otherwise if no bundle exists with this name, return null
    return null;
  }

  /**
   * Helper Method: Returns a member with a given email
   * 
   * @param email the email of the member
   * @Author Shidan Javaheri
   */
  private Member returnMember(String email) {
    // get a list of all members in the system
    List<Member> memberList = diveSafe.getMembers();
    int position = 0;
    // find the member with a matching email and return it
    for (Member member : memberList) {
      if (member.getEmail().equals(email)) {
        position = diveSafe.indexOfMember(member);
        return diveSafe.getMember(position);
      }
    }
    // otherwise if no member exists with this email, return null
    return null;

  }

  /**
   * Helper Method: Returns an item with a given name
   * 
   * @param name the name of the Item
   * @Author Shidan Javaheri
   */
  private Item returnItem(String name) {
    // try and find an Equipment object with this email
    // if it does not exist, then call returnBundle method
    Item returnableItem = returnEquipment(name);
    if (returnableItem != null) {
      return returnableItem;
    } else {
      returnableItem = returnBundle(name);
      return returnableItem;
    }
  }


  /**
   * @Author Shidan Javaheri
   */
  @Given("the following DiveSafe system exists:")
  public void the_following_dive_safe_system_exists(io.cucumber.datatable.DataTable dataTable) {
    // Get the diveSafe Application
    diveSafe = DiveSafeApplication.getDiveSafe();

    // Variables to get from the data table
    Date startDate;
    int numDays;
    int priceOfGuidePerDay;

    // Convert table to list, and parse it. Update diveSafe Application
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      // Parse Data Table
      startDate = Date.valueOf(row.get("startDate"));
      numDays = Integer.parseInt(row.get("numDays"));
      priceOfGuidePerDay = Integer.parseInt(row.get("priceOfGuidePerDay"));
      // add the retrieved paramerters to the diveSafe object
      diveSafe.setStartDate(startDate);
      diveSafe.setNumDays(numDays);
      diveSafe.setPriceOfGuidePerDay(priceOfGuidePerDay);
    }
  }

  /**
   * @Author Shidan Javaheri
   */
  @Given("the following pieces of equipment exist in the system:")
  public void the_following_pieces_of_equipment_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    // Convert the table to a list, parse the list and create equipment objects in diveSafe
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String name = row.get("name");
      int weight = Integer.parseInt(row.get("weight"));
      int pricePerDay = Integer.parseInt(row.get("pricePerDay"));
      diveSafe.addEquipment(name, weight, pricePerDay);
    }
  }

  /**
   * @Author Shidan Javaheri
   */
  @Given("the following equipment bundles exist in the system:")
  public void the_following_equipment_bundles_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    // Convert the Data table to a list, parse the list and add the bundles and bundle items to divesafe
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {

      // Get the name and discount from the table
      String name = row.get("name");
      int discount = Integer.parseInt(row.get("discount"));

      // Add the bundle to the diveSafe
      diveSafe.addBundle(name, discount);

      // Get the bundle instance we just created
      EquipmentBundle bundleInstance = returnBundle(name);

      // Transform the items and quantities into lists from the table
      String itemsString = row.get("items");
      String[] itemsList = itemsString.split(",");
      String quantityString = row.get("quantity");
      String[] quantityList = quantityString.split(",");

      // Add bundle items to the bundle we just created
      for (int number = 0; number < itemsList.length; number++) {
        diveSafe.addBundleItem(Integer.parseInt(quantityList[number]), bundleInstance,
            returnEquipment(itemsList[number]));
      }
    }
  }

  /**
   * @Author Shidan Javaheri
   */
  @Given("the following guides exist in the system:")
  public void the_following_guides_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    // Convert the Data Table to a list. Parse the lsit and add the guides
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {

      // Get string, boolean and int values from table
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      diveSafe.addGuide(email, password, name, emergencyContact);
    }
  }

  /**
   * @Author Shidan Javaheri
   */
  @Given("the following members exist in the system:")
  public void the_following_members_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    // Convert the data table to a list, and then parse its elements
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {

      // Get string, boolean and int values from table
      String email = row.get("email");
      String password = row.get("password");
      String name = row.get("name");
      String emergencyContact = row.get("emergencyContact");
      int numDays = Integer.parseInt(row.get("numDays"));
      Boolean guideRequired = Boolean.parseBoolean(row.get("guideRequired"));
      Boolean hotelRequired = Boolean.parseBoolean(row.get("hotelRequired"));

      // Create Member with these details
      diveSafe.addMember(email, password, name, emergencyContact, numDays, guideRequired, hotelRequired);

      // Get values from table that could be comma seperated strings, and convert them to lists
      String itemsString = row.get("items");
      String quantityString = row.get("requestedQuantities");
      if (itemsString != null && quantityString != null) {
        String[] itemsList = itemsString.split(",");
        String[] quantityList = quantityString.split(",");
        // Add the item bookings that the member has requested
        for (int number = 0; number < itemsList.length; number++) {

          diveSafe.addItemBooking(Integer.parseInt(quantityList[number]), returnMember(email),
              returnItem(itemsList[number]));
        }
      }
    }
  }

  /**
   * @author Karim Kanafani
   */
  @When("the administrator attempts to initiate the assignment process")
  public void the_administrator_attempts_to_initiate_the_assignment_process() {
    // Call AssignmentController to initiate the assignment and pass the returned string to error
    error = AssignmentController.initiateAssignment();
  }

  /**
   * @author Karim Kanafani
   * @param dataTable
   */
  @Then("the following assignments shall exist in the system:")
  public void the_following_assignments_shall_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    // Get the information out of the given dataTable
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      String emailOfMember = row.get("memberEmail"); // Get the user's email
      String emailOfGuide = row.get("guideEmail"); // Get the guide's email
      String startDay = row.get("startDay"); // Get the start day
      String endDay = row.get("endDay"); // Get the end day
      Member currentMember = Member.getWithEmail(emailOfMember); // Get the current member using the email
      Assignment currentAssignment = currentMember.getAssignment(); // Get the current member's assignment
      String assignedGuideEmail; // Empty string place holder for guide's email
      if (currentAssignment.getGuide() != null) { // Check if member was assigned a guide
        assignedGuideEmail = currentAssignment.getGuide().getEmail(); // Get the email of the guide
      } else {
        assignedGuideEmail = null; // Guide email is null
      }

      String assignedStartDay = String.valueOf(currentAssignment.getStartDay()); // Get the assignment start day
      String assignedEndDay = String.valueOf(currentAssignment.getEndDay()); // Get the assignment end day
      // Check if information matches
      assertEquals(emailOfGuide, assignedGuideEmail);
      assertEquals(startDay, assignedStartDay);
      assertEquals(endDay, assignedEndDay);
    }
  }

  /**
   * @author Karim Kanafani
   * @param string
   * @param string2
   */
  @Then("the assignment for {string} shall be marked as {string}")
  public void the_assignment_for_shall_be_marked_as(String string, String string2) {

    Member currentMember = Member.getWithEmail(string); // Get current member using email
    if (currentMember != null) { // If member exists
      Assignment currentAssignment = currentMember.getAssignment(); // Get the current member's assignment
      String assignmentStatus = String.valueOf(currentAssignment.getAssignmentStatus());// Get the assignment status
      assertEquals(string2, assignmentStatus); // Check if the assignment status matches
    }
  }

  /**
   * @author Karim Kanafani
   * @param string
   */
  @Then("the number of assignments in the system shall be {string}")
  public void the_number_of_assignments_in_the_system_shall_be(String string) {
    // Check if the string povided matches with the number of assignments in the diveSafe system
    assertEquals(string, String.valueOf(diveSafe.numberOfAssignments()));
  }

  /**
   * @Author Sami Ferneini
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String string) {
    assertEquals(string, error); // Verify the equality of the error and the given error

  }

  /**
   * @Author Sami Ferneini
   */
  @Given("the following assignments exist in the system:")
  public void the_following_assignments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

    List<Map<String, String>> assignmentsInfo = dataTable.asMaps(); // Turn the given into a workable map
    for (Map<String, String> assignment : assignmentsInfo) { // Cycling through the map
      int aStartDay = Integer.parseInt(assignment.get("startDay")); // Get the start day
      int aEndDay = Integer.parseInt(assignment.get("endDay")); // Get the end day
      Member aMember = Member.getWithEmail(assignment.get("memberEmail")); // Get the member through the email
      if (assignment.get("guideEmail") != null) { // If the assignment requires a guide
        Guide guide = Guide.getWithEmail(assignment.get("guideEmail")); // Get the guide through the email
        guide.addAssignment(diveSafe.addAssignment(aStartDay, aEndDay, aMember)); // Add the assignment to diveSafe and
                                                                                  // the guide
      } else {
        diveSafe.addAssignment(aStartDay, aEndDay, aMember); // Add the assignment without a guide
      }
    }
  }

  /**
   * @Author Sami Ferneini
   */
  @When("the administrator attempts to confirm payment for {string} using authorization code {string}")
  public void the_administrator_attempts_to_confirm_payment_for_using_authorization_code(String string,
      String string2) {


    error = AssignmentController.confirmPayment(string, string2); // Store the error from confirming payment using
                                                                  // authorization code

  }

  /**
   * @Author Sami Ferneini
   */
  @Then("the assignment for {string} shall record the authorization code {string}")
  public void the_assignment_for_shall_record_the_authorization_code(String string, String string2) {


    assertEquals(string2, Member.getWithEmail(string).getAssignment().getAuthenticationCode()); // Verifying equality
                                                                                                // between the
                                                                                                // authorization code
                                                                                                // and the stored member
                                                                                                // code


  }

  /**
   * @Author Anthony Boustany
   */
  @Then("the member account with the email {string} does not exist")
  public void the_member_account_with_the_email_does_not_exist(String string) {
    assertTrue((returnMember(string)) == null);
  }

  /**
   * @Author Anthony Boustany
   */
  @Then("there are {string} members in the system")
  public void there_are_members_in_the_system(String string) {
    assertEquals(string, String.valueOf(diveSafe.numberOfMembers()));
  }

  /**
   * @Author Anthony Boustany
   */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String string) {
    assertEquals(string, error);
  }

  /**
   * @Author Anthony Boustany
   */
  @When("the administrator attempts to cancel the trip for {string}")
  public void the_administrator_attempts_to_cancel_the_trip_for(String string) {
    error = AssignmentController.cancelTrip(string);
  }

  /**
   * @author Elsa Chelala
   * @param string
   */
  @Given("the member with {string} has paid for their trip")
  public void the_member_with_has_paid_for_their_trip(String string) {
    error = AssignmentController.confirmPayment(string, "Authorized");
  }

  /**
   * @author Elsa Chelala
   * @param string
   * @param string2
   */
  @Then("the member with email address {string} shall receive a refund of {string} percent")
  public void the_member_with_email_address_shall_receive_a_refund_of_percent(String string, String string2) {
    Member member = Member.getWithEmail(string);
    Assignment currentAssignment = member.getAssignment();
    int refundPercentage = currentAssignment.getRefund();
    assertEquals(Integer.parseInt(string2), refundPercentage);

  }

  /**
   * @author Elsa Chelala
   * @param string
   */
  @Given("the member with {string} has started their trip")
  public void the_member_with_has_started_their_trip(String string) {
    Member member = returnMember(string);
    int startDay = member.getAssignment().getStartDay();
    member.getAssignment().confirmPayment("Authorized");
    member.getAssignment().startTripForDay(startDay);
  }

  /**
   * @author Elsa Chelala
   * @param string
   */
  @When("the administrator attempts to finish the trip for the member with email {string}")
  public void the_administrator_attempts_to_finish_the_trip_for_the_member_with_email(String string) {
    error = AssignmentController.finishTrip(string);
  }

  /**
   * This method toggles the assignment of a user to banned by calling AssignmentController and using the user's email
   * as an argument for our user-defined toggleBan() method. We store the error in a String variable for later use in
   * each scenario.
   * 
   * @Author - Shyam Desai
   */
  @Given("the member with {string} is banned")
  public void the_member_with_is_banned(String string) {
    // Write code here that turns the phrase above into concrete actions

    error = AssignmentController.toggleBan(string);

  }

  /**
   * This method asserts whether the expected assignment status 'string2' of a member with email 'string' matches the
   * actual assignment status. We first getting the member with the given email, then get their assignment status. We
   * use String.valueOf() to extract the String value given an object so that we can compare a String with String
   * 
   * @Author - Shyam Desai
   */
  @Then("the member with email {string} shall be {string}")
  public void the_member_with_email_shall_be(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions

    assertEquals(string2, String.valueOf(Member.getWithEmail(string).getAssignment().getAssignmentStatus()));

  }

  /**
   * This method starts the trip for the day for a given day. We use our AssignmentController's startTripsForDay()
   * method, and parse the 'string' day parameter into an integer. We store the error in a String variable for later use
   * in each scenario.
   * 
   * @Author - Shyam Desai
   */
  @When("the administrator attempts to start the trips for day {string}")
  public void the_administrator_attempts_to_start_the_trips_for_day(String string) {
    // Write code here that turns the phrase above into concrete actions

    error = AssignmentController.startTripsForDay(Integer.parseInt(string));

  }

  /**
   * This method cancels the trip of a user. We use AssignmentController on the given member's email as an argument for
   * our user-defined cancelTrip() method. We store the error in a String variable for later use in each scenario.
   * 
   * @Author - Shyam Desai
   */
  @Given("the member with {string} has cancelled their trip")
  public void the_member_with_has_cancelled_their_trip(String string) {
    // Write code here that turns the phrase above into concrete actions

    error = AssignmentController.cancelTrip(string);

  }

  /**
   * This method finishes the trip of a user. We first use AssignmentController on the given member email as an argument
   * for our user-defined confirmPayment() method. Then, we start the trip for the member with the startTripForDay()
   * method. Finally, using once again the AssignmentController we finish the trip for the member using our user-defined
   * finishTrip() method. We store the error in a String variable for later use in each scenario.
   * 
   * @Author - Shyam Desai
   */
  @Given("the member with {string} has finished their trip")
  public void the_member_with_has_finished_their_trip(String string) {
    // Write code here that turns the phrase above into concrete actions

    AssignmentController.confirmPayment(string, "Authorized");
    Member member = returnMember(string);
    int startDay = member.getAssignment().getStartDay();
    member.getAssignment().startTripForDay(startDay);
    error = AssignmentController.finishTrip(string);

  }
}
