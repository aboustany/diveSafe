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

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// please push
public class RegisterMemberPageController {
  @FXML
  private TextField registerMemberPasswordInput;
  @FXML
  private TextField registerMemberEmailInput;
  @FXML
  private TextField registerMemberNameInput;
  @FXML
  private TextField registerMemberEmergencyContactInput;
  @FXML
  private Spinner<Integer> registerMemberNumberOfDaysInput;
  @FXML
  private CheckBox registerMemberGuideRequiredInput;
  @FXML
  private CheckBox registerMemberHotelRequiredInput;
  @FXML
  private Button registerMemberRegisterButton;

  @FXML
  private TableColumn<TOEquipment, Spinner<Integer>> registerMemberEquipmentQuantity;

  @FXML
  private TableColumn<TOBundle, Spinner<Integer>> registerMemberBundleQuantity;

  @FXML
  private TableView<TOBundle> registerMemberBundleTable;

  @FXML
  private TableView<TOEquipment> registerMemberEquipmentTable;

  private Boolean guideNeeded = false;
  private Boolean hotelNeeded = false;

  /**
   * fillTable is used to populate the Equipment and Bundle Table Columns
   * 
   * @author Karim, Anthony, Elsa
   * 
   */
  public void fillTable() {
    registerMemberEquipmentTable.setItems(getEquipmentItems());
    registerMemberBundleTable.setItems(getBundleItems());
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

    // Spinner initialization
    SpinnerValueFactory<Integer> valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    registerMemberNumberOfDaysInput.setValueFactory(valueFactory);


    registerMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Name", "name"));
    registerMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Weight", "weight"));
    registerMemberEquipmentTable.getColumns().add(createColumnForEquipmentTable("Price Per Day", "pricePerDay"));
    registerMemberEquipmentTable.getColumns()
        .add(createQuantityColumnForEquipmentTable("Quantity", "equipmentQuantity"));
    registerMemberEquipmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    registerMemberBundleTable.getColumns().add(createColumnForBundleTable("Name", "name"));
    registerMemberBundleTable.getColumns().add(createColumnForBundleTable("Discount", "discount"));
    registerMemberBundleTable.getColumns().add(createColumnForBundleTable("Price Per Day", "pricePerDay"));
    registerMemberBundleTable.getColumns().add(createQuantityColumnForBundleTable("Quantity", "bundleQuantity"));
    registerMemberBundleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    fillTable();
  }

  /**
   * toggleGuideRequired acts as a getter method to determine if the member needs a guide
   * 
   * @author Karim
   * @param event
   *
   */
  // Event Listener on CheckBox[#registerMemberGuideRequiredInput].onAction
  @FXML
  public void toggleGuideRequired(ActionEvent event) {
    // TODO Autogenerated
    boolean isGuideSelected = registerMemberGuideRequiredInput.isSelected();
    guideNeeded = isGuideSelected;
  }

  /**
   * toggleHotelRequired is a method that determines whether or not the member asked for a guide
   * 
   * @author Karim
   * @param event
   * 
   */
  // Event Listener on CheckBox[#registerMemberHotelRequiredInput].onAction
  @FXML
  public void toggleHotelRequired(ActionEvent event) {
    // TODO Autogenerated
    boolean isHotelSelected = registerMemberHotelRequiredInput.isSelected();
    hotelNeeded = isHotelSelected;
  }

  /**
   * addMemberToDiveSafe is used to add the registed member to the DiveSafe database
   * 
   * @author Karim, Anthony, Elsa
   * @param event
   * 
   */
  // Event Listener on Button[#registerMemberRegisterButton].onAction
  @FXML
  public void addMemberToDiveSafe(ActionEvent event) {

    String memberEmail = registerMemberEmailInput.getText();
    String memberPassword = registerMemberPasswordInput.getText();
    String memberName = registerMemberNameInput.getText();
    String memberEmergencyContact = registerMemberEmergencyContactInput.getText();
    int numberOfDays = registerMemberNumberOfDaysInput.getValue();
    List<Integer> itemQuantity = new ArrayList<>();
    List<String> itemList = new ArrayList<>();

    ArrayList<String> equipmentNames = getNameFromTable(registerMemberEquipmentTable);
    ArrayList<String> bundleNames = getNameFromTable(registerMemberBundleTable);

    ArrayList<Spinner> bundlesSpinners = getSpinnerFromTable(registerMemberBundleTable);
    ArrayList<Spinner> equipmentSpinners = getSpinnerFromTable(registerMemberEquipmentTable);

    for (var spinner : equipmentSpinners) {
      itemQuantity.add((Integer) spinner.getValue());
    }

    for (var spinner : bundlesSpinners) {
      itemQuantity.add((Integer) spinner.getValue());
    }
    itemList.addAll(equipmentNames);
    itemList.addAll(bundleNames);


    if (memberEmail == (null) || memberPassword == (null) || memberName == (null) || memberEmergencyContact == (null)
        || itemList == (null) || itemQuantity == (null)) {
      ViewUtils.showError("Please fill in all fields");
    } else {
      try {
        Boolean result = ViewUtils.callController(MemberController.registerMember(memberEmail, memberPassword,
            memberName, memberEmergencyContact, numberOfDays, guideNeeded, hotelNeeded, itemList, itemQuantity));
        
        if (result) {
          DiveSafeFxmlView.getInstance().refresh();
          registerMemberEmailInput.setText("");
          registerMemberPasswordInput.setText("");
          registerMemberNameInput.setText("");
          registerMemberEmergencyContactInput.setText("");
          registerMemberNumberOfDaysInput.getValueFactory().setValue(1);
          registerMemberGuideRequiredInput.setSelected(false);
          registerMemberHotelRequiredInput.setSelected(false);

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
