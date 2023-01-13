package ca.mcgill.ecse.divesafe.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.TOAssignment;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class AssignmentPageController {
  @FXML
  private Button initiateAssignmentsButton;
  @FXML
  private Button payForTripButton;
  @FXML
  private Button cancelTripButton;
  @FXML
  private Button finishTripButton;
  @FXML
  private TextField startDayInput;
  @FXML
  private Button startTripsOnDayButton;
  @FXML
  private TextField authorizationCodeInput;
  @FXML
  private TextField memberEmailInput;
  @FXML
  private Button searchTripButton;
  @FXML
  private TableView<TOAssignment> assignmentTable;


  /**
   * Intialize method mirrors the structure of the BTMS initialize method. Setups of the drop down list and data table
   * 
   * @author Shidan Javaheri
   */

  @FXML
  public void initialize() {

    // initialize the assignment table by adding new columns, displaying the properties for the TO assignment objects
    assignmentTable.getColumns().add(createTableColumn("Member Email", "memberEmail"));
    assignmentTable.getColumns().add(createTableColumn("Guide Email", "guideEmail"));
    assignmentTable.getColumns().add(createTableColumn("Start Day", "startDay"));
    assignmentTable.getColumns().add(createTableColumn("End Day", "endDay"));
    assignmentTable.getColumns().add(createTableColumn("Total Cost for Equipment", "totalCostForEquipment"));
    assignmentTable.getColumns().add(createTableColumn("Total Cost for Guide", "totalCostForGuide"));
    assignmentTable.getColumns().add(createTableColumn("Authorization Code", "authorizationCode"));
    assignmentTable.getColumns().add(createTableColumn("Refund", "refund"));

    // need to access the status collumn seperately later, to give it colors
    var statusCollumn = createTableColumn("Status", "status");
    assignmentTable.getColumns().add(statusCollumn);

    // change the color of the cells based on the assignment status

    statusCollumn.setCellFactory(col -> new TableCell<>() {
      @Override
      public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        var row = getTableRow();
        setText(item);
        setTextFill(Color.BLACK);
        // default colors is black, and default style is bold
        setStyle("-fx-font-weight: bold");
        // set the colors based on the assignment status
        if (row.getItem() != null && row.getItem().getStatus() == "Banned") {
          setTextFill(Color.RED);
        } else if (row.getItem() != null && row.getItem().getStatus() == "Paid") {
          setTextFill(Color.GREEN);
        } else if (row.getItem() != null && row.getItem().getStatus() == "Started") {
          setTextFill(Color.DARKGREEN);
        } else if (row.getItem() != null && row.getItem().getStatus() == "Cancelled") {
          setTextFill(Color.ORANGE);
        } else if (row.getItem() != null && row.getItem().getStatus() == "Finished") {
          setTextFill(Color.BLUE);


        }
      }
    });

    // register refreshable nodes
    DiveSafeFxmlView.getInstance().registerRefreshEvent(assignmentTable);

    // overview table if a refreshable element
    assignmentTable.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT,
        e -> assignmentTable.setItems(getAssignmentItems()));



  }


  public ObservableList<TOAssignment> getAssignmentItems() {
    ObservableList<TOAssignment> assignments = ViewUtils.getAssignments();
    return assignments;
  }

  // the table column will automatically display the string value of the property for each instance
  // in the table
  public static TableColumn<TOAssignment, String> createTableColumn(String header, String propertyName) {
    TableColumn<TOAssignment, String> column = new TableColumn<>(header);
    column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
    return column;
  }

  /**
   * Initiate Assignments Button Calls the Assignment Controller Initiate Assignments method
   * 
   * @author Shidan Javaheri
   * @param event
   */

  // Event Listener on Button[#initiateAssignmentsButton].onAction
  @FXML
  public void initiateAssignmentsClicked(ActionEvent event) {
    // call initiate assignment controller method
    ViewUtils.successful(AssignmentController.initiateAssignment());
  }

  /**
   * Pay Trip Button calls the controller method confirmPayment(String email, String code) on selected assignment in the
   * table
   * 
   * @author Shidan Javaheri
   * @param event
   */


  // Event Listener on Button[#payForTripButton].onAction
  @FXML
  public void payTripClicked(ActionEvent event) {
    // get the authorization code
    String authorizationCode = authorizationCodeInput.getText();
    // if there is no input, show an error message
    if (authorizationCode.equals("") || authorizationCode == null) {
      ViewUtils.showError("Please enter a valid authorization code");
      DiveSafeFxmlView.getInstance().refresh();
      // otherwise, get selected item from the table. If it doesn't exist show error
    } else {
      TOAssignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
      if (selectedAssignment == null) {
        ViewUtils.showError("Please select or seach for a single member whose trip you wish to pay for");
        DiveSafeFxmlView.getInstance().refresh();
        // otherwise call the controller to confirm the payment of the selected assignment
      } else {
        ViewUtils.callController(
            AssignmentController.confirmPayment(selectedAssignment.getMemberEmail(), authorizationCode));
        authorizationCodeInput.setText("");
        DiveSafeFxmlView.getInstance().refresh();
      }
    }
  }

  /**
   * Cancel Trip Button calls the cancelTrip(String email) controller method on the selected assignment in the table
   * 
   * @author Shidan Javaheri
   * @param event
   */

  // Event Listener on Button[#cancelTripButton].onAction
  @FXML
  public void cancelTripClicked(ActionEvent event) {
    // get the selected item from the table
    TOAssignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
    // if there is nothing selected, create a popup window error message
    if (selectedAssignment == null) {
      ViewUtils.showError("Please select or seach for a member whose trip you wish to cancel");
      DiveSafeFxmlView.getInstance().refresh();
     // otherwise, call the controller to cancel this trip
    } else {
      ViewUtils.callController(AssignmentController.cancelTrip(selectedAssignment.getMemberEmail()));
      DiveSafeFxmlView.getInstance().refresh();

    }
  }

  /**
   * Finish Trip Button calls the finishTrip(String Email) controller method on the selected assignment in the table
   * 
   * @author Shidan Javaheri
   * @param event
   */

  // Event Listener on Button[#finishTripButton].onAction
  @FXML
  public void finishTripClicked(ActionEvent event) {
    // get the selected item from the table
    TOAssignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
    // if it is null, create a popup error message
    if (selectedAssignment == null) {
      ViewUtils.showError("Please select or seach for a member whose trip you wish to cancel");
      DiveSafeFxmlView.getInstance().refresh();
     // otherwise, call the controller to finish the trip for the selected item. 
    } else {
      ViewUtils.callController(AssignmentController.finishTrip(selectedAssignment.getMemberEmail()));
      DiveSafeFxmlView.getInstance().refresh();
    }
  }

  /**
   * Start Trips on Day button calls the startTripsForDay(int day) controller method if the input is an integer
   * 
   * @author Shidan Javaheri
   * @param event
   */

  // Event Listener on Button[#startTripsOnDayButton].onAction
  @FXML
  public void startTripsOnDayClicked(ActionEvent event) {
    // check that the user has entered an integer, create error message if not
    Integer startDay;

    try {
      startDay = Integer.parseInt(startDayInput.getText());
    } catch (NumberFormatException e) {
      startDay = null;
    }
    if (startDay == null) {
      ViewUtils.showError("Please enter an Integer");
      DiveSafeFxmlView.getInstance().refresh();
      // if input is fine, attempt to start trips on this day with the controller
    } else {
      if (ViewUtils.successful(AssignmentController.startTripsForDay(startDay))) {
        startDayInput.setText("");
        

      }
      DiveSafeFxmlView.getInstance().refresh();
    }
  }

  /**
   * Search button selects the appropriate row in the table based on the input email
   * 
   * @author Shidan Javaheri
   * @param event
   */

  // Event Listener on Button[#searchTripButton].onAction
  @FXML
  public void searchTripClicked(ActionEvent event) {
    // get the email that they would like to search for. Show error if it is empty
    String email = memberEmailInput.getText();
    if (email.equals("")) {
      ViewUtils.showError("Please enter a non empty email");
      DiveSafeFxmlView.getInstance().refresh();
      // otherwise, find the TOAssignment object that matches that member email. Count what row it is in
    } else {
      TOAssignment searchedFor = null;
      int counter = 0;
      ObservableList<TOAssignment> allAssignments = ViewUtils.getAssignments();
      for (TOAssignment assignment : allAssignments) {
        if (assignment.getMemberEmail().equals(email)) {
          searchedFor = assignment;
          break;
        }
        counter++;
      }
      // if the object is found, then select its row by selecting the first row and then iterating to the position
      if (searchedFor != null) {
        assignmentTable.getSelectionModel().selectFirst();
        for (int i = 0; i < counter; i++) {
          assignmentTable.getSelectionModel().selectNext();
        }
        // clear the input
        memberEmailInput.setText("");
        // otherwise the assignment with that email was not found
      } else {
        ViewUtils.showError("An assignment with this member email was not found");
        DiveSafeFxmlView.getInstance().refresh();
      }
    }
  }
}
