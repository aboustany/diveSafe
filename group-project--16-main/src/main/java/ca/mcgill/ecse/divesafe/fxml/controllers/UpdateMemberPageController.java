package ca.mcgill.ecse.divesafe.fxml.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.controller.TOBundle;
import ca.mcgill.ecse.divesafe.controller.TOEquipment;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// please push
public class UpdateMemberPageController {
  @FXML
  private TextField updateMemberPassword;
  @FXML
  private ChoiceBox<String> updateMemberEmailDropDown; // might have to get the value
  @FXML
  private TextField updateMemberName;
  @FXML
  private TextField updateMemberEmergencyContact;
  @FXML
  private Spinner<Integer> updateMemberNumOfDaysSpinner;
  @FXML
  private CheckBox updateMemberIsGuideRequired;
  @FXML
  private CheckBox updateMemberIsHotelRequired;
  @FXML
  private Button updateMemberUpdateButton;

  @FXML
  private TableColumn<TOEquipment, Spinner<Integer>> updateMemberEquipmentQuantity;

  @FXML
  private TableColumn<TOBundle, Spinner<Integer>> updateMemberBundleQuantity;

  @FXML
  private TableView<TOBundle> updateMemberBundleTable;

  @FXML
  private TableView<TOEquipment> updateMemberEquipmentTable;

  private Boolean guideNeeded = false;
  private Boolean hotelNeeded = false;



  /**
   * fillTable is used to populate the Equipment and Bundle Table Columns
   * 
   * @author Karim, Anthony, Elsa
   * 
   */
  public void fillTable() {
    updateMemberEquipmentTable.setItems(getEquipmentItems());
    updateMemberBundleTable.setItems(getBundleItems());
  }

  /**
   * getSpinnerFromTable is used as a getter method for the different Table Columns spinners
   * 
   * @author Karim, Anthony, Elsa
   * @param table
   * 
   */
  public ArrayList<Spinner> getSpinnerFromTable(TableView table) {
    ArrayList<Spinner> quantitySpinner = new ArrayList<Spinner>();
    TableColumn quantityColumn = (TableColumn) table.getColumns().get(3);

    for (Object row : table.getItems()) {
      quantitySpinner.add((Spinner) quantityColumn.getCellObservableValue(row).getValue());
    }
    return quantitySpinner;
  }

  /**
   * createColumnForBundleTable is a method that creates each column for the Bundle Table Column
   * 
   * @author Karim, Anthony, Elsa
   * @param columnTitle, attribute
   * 
   */
  public TableColumn<TOBundle, String> createColumnForBundleTable(String columnTitle, String attribute) {
    TableColumn<TOBundle, String> createdColumn = new TableColumn<>(columnTitle);
    createdColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));
    return createdColumn;
  }

  /**
   * createColumnForEquipmentTable is a method that creates each column for the Equipment Table Column
   * 
   * @author Karim, Anthony, Elsa
   * @param columnTitle, attribute
   * 
   */
  public TableColumn<TOEquipment, String> createColumnForEquipmentTable(String columnTitle, String attribute) {
    TableColumn<TOEquipment, String> createdColumn = new TableColumn<>(columnTitle);
    createdColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));
    return createdColumn;
  }

  /**
   * createQuantityColumnForEquipmentTable is a method that creates quantity column for the Equipment Table Column with
   * the spinner
   * 
   * @author Karim, Anthony, Elsa
   * @param columnTitle, attribute
   * 
   */
  public TableColumn<TOEquipment, Spinner> createQuantityColumnForEquipmentTable(String columnTitle, String attribute) {
    TableColumn<TOEquipment, Spinner> createdColumn = new TableColumn<>(columnTitle);
    createdColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));
    return createdColumn;
  }

  /**
   * createQuantityColumnForBundleTable is a method that creates quantity column for the Bundle Table Column with the
   * spinner
   * 
   * @author Karim, Anthony, Elsa
   * @param columnTitle, attribute
   * 
   */
  public TableColumn<TOBundle, Spinner> createQuantityColumnForBundleTable(String columnTitle, String attribute) {
    TableColumn<TOBundle, Spinner> createdColumn = new TableColumn<>(columnTitle);
    createdColumn.setCellValueFactory(new PropertyValueFactory<>(attribute));
    return createdColumn;
  }

  /**
   * getEquipmentItems is a getter method for the Equipments
   * 
   * @author Karim, Anthony, Elsa
   * 
   */
  public ObservableList<TOEquipment> getEquipmentItems() {
    ObservableList<TOEquipment> equipment = ViewUtils.getEquipments();
    return equipment;
  }

  /**
   * getBundleItems is a getter method for the Bundles
   * 
   * @author Karim, Anthony, Elsa
   * 
   */
  public ObservableList<TOBundle> getBundleItems() {
    ObservableList<TOBundle> bundle = ViewUtils.getBundles();
    return bundle;
  }

  /**
   * getNameFromTable is a getter method for the names of the table items
   * 
   * @author Karim, Anthony, Elsa
   * @param table
   * 
   */
  public ArrayList<String> getNameFromTable(TableView table) {
    ArrayList<String> names = new ArrayList<String>();
    TableColumn nameColumn = (TableColumn) table.getColumns().get(0);

    for (Object row : table.getItems()) {
      names.add((String) nameColumn.getCellObservableValue(row).getValue());
    }
    return names;
  }


  /**
   * initialize is a method that initializes the items of the GUI
   * 
   * @author Karim, Anthony, Elsa
   * 
   */
  @FXML
  public void initialize() {

    DiveSafeFxmlView.getInstance().registerRefreshEvent(updateMemberEmailDropDown);
    updateMemberEmailDropDown.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT,
        e -> updateMemberEmailDropDown.setItems(ViewUtils.getMemberEmails()));
    updateMemberEmailDropDown.setItems(ViewUtils.getMemberEmails());

    // Spinner initialization
    SpinnerValueFactory<Integer> valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    updateMemberNumOfDaysSpinner.setValueFactory(valueFactory);


    updateMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Name", "name"));
    updateMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Weight", "weight"));
    updateMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Price Per Day", "pricePerDay"));
    updateMemberEquipmentTable.getColumns().add(createQuantityColumnForEquipmentTable("Quantity", "equipmentQuantity"));
    updateMemberEquipmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    updateMemberBundleTable.getColumns().add(createColumnForBundleTable("Name", "name"));
    updateMemberBundleTable.getColumns().add(createColumnForBundleTable("Discount", "discount"));
    updateMemberBundleTable.getColumns().add(createColumnForBundleTable("Price Per Day", "pricePerDay"));
    updateMemberBundleTable.getColumns().add(createQuantityColumnForBundleTable("Quantity", "bundleQuantity"));
    updateMemberBundleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    fillTable();
  }

  /**
   * toggleGuideRequiredClicked acts as a getter method to determine if the member needs a guide
   * 
   * @author Karim
   * @param event
   *
   */
  // Event Listener on CheckBox[#updateMemberIsGuideRequired].onAction
  @FXML
  public void toggleGuideRequiredClicked(ActionEvent event) {
    // TODO Autogenerated
    boolean isGuideSelected = updateMemberIsGuideRequired.isSelected();
    guideNeeded = isGuideSelected;
  }

  /**
   * toggleHotelRequiredClicked is a method that determines whether or not the member asked for a guide
   * 
   * @author Karim
   * @param event
   * 
   */
  // Event Listener on CheckBox[#updateMemberIsHotelRequired].onAction
  @FXML
  public void toggleHotelRequiredClicked(ActionEvent event) {
    // TODO Autogenerated
    boolean isHotelSelected = updateMemberIsHotelRequired.isSelected();
    hotelNeeded = isHotelSelected;
  }

  /**
   * updateMemberUpdateClicked is used to add the registed member to the DiveSafe database
   * 
   * @author Karim, Anthony, Elsa
   * @param event
   * 
   */
  // Event Listener on Button[#updateMemberUpdateButton].onAction
  @FXML
  public void updateMemberUpdateClicked(ActionEvent event) {
    // TODO Autogenerated
    String memberEmail = updateMemberEmailDropDown.getValue();
    String memberPassword = updateMemberPassword.getText();
    String memberName = updateMemberName.getText();
    String memberEmergencyContact = updateMemberEmergencyContact.getText();
    int numberOfDays = updateMemberNumOfDaysSpinner.getValue();
    List<Integer> itemQuantity = new ArrayList<>();
    List<String> itemList = new ArrayList<>();

    ArrayList<String> equipmentNames = getNameFromTable(updateMemberEquipmentTable);
    ArrayList<String> bundleNames = getNameFromTable(updateMemberBundleTable);

    ArrayList<Spinner> bundlesSpinners = getSpinnerFromTable(updateMemberBundleTable);
    ArrayList<Spinner> equipmentSpinners = getSpinnerFromTable(updateMemberEquipmentTable);

    for (var spinner : equipmentSpinners) {
      itemQuantity.add((Integer) spinner.getValue());
    }

    for (var spinner : bundlesSpinners) {
      itemQuantity.add((Integer) spinner.getValue());
    }
    itemList.addAll(equipmentNames);
    itemList.addAll(bundleNames);



    // System.out.println("Quantity " +itemQuantity);
    if (memberEmail == (null) || memberPassword == (null) || memberName == (null) || memberEmergencyContact == (null)
        || itemList == (null) || itemQuantity == (null)) {
      // String error = ViewUtils.callController(MemberController.registerMember(memberEmail, memberPassword,
      // memberName, memberEmergencyContact, numberOfDays, guideNeeded, hotelNeeded, itemList, itemQuantity));
      ViewUtils.showError("Please fill in all fields");
    } else {
      try {
        Boolean result = ViewUtils.callController(MemberController.updateMember(memberEmail, memberPassword, memberName,
            memberEmergencyContact, numberOfDays, guideNeeded, hotelNeeded, itemList, itemQuantity));
        
        if (result) {
          DiveSafeFxmlView.getInstance().refresh();
          updateMemberPassword.setText("");
          updateMemberName.setText("");
          updateMemberEmergencyContact.setText("");
          updateMemberNumOfDaysSpinner.getValueFactory().setValue(1);
          updateMemberIsHotelRequired.setSelected(false);
          updateMemberIsGuideRequired.setSelected(false);

          for (var spinner : equipmentSpinners) {
            spinner.getValueFactory().setValue(0);
          }

          for (var spinner : bundlesSpinners) {
            spinner.getValueFactory().setValue(0);
          }
        }
      } catch (Exception e) {
        System.out.println(e);
      }

    }

  }
}
