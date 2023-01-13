package ca.mcgill.ecse.divesafe.fxml.controllers;

import java.util.List;
import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.controller.AssignmentController;
import ca.mcgill.ecse.divesafe.controller.BundleController;
import ca.mcgill.ecse.divesafe.controller.EquipmentController;
import ca.mcgill.ecse.divesafe.controller.TOAssignment;
import ca.mcgill.ecse.divesafe.controller.TOEquipment;
import ca.mcgill.ecse.divesafe.controller.TOMember;
import ca.mcgill.ecse.divesafe.controller.TOBundle;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewUtils {

  /** Calls the controller and shows an error, if applicable. */
  public static boolean callController(String result) {
    if (result.isEmpty()) {
      DiveSafeFxmlView.getInstance().refresh();
      return true;
    }
    showError(result);
    return false;
  }

  /** Calls the controller and returns true on success. This method is included for readability. */
  public static boolean successful(String controllerResult) {
    return callController(controllerResult);
  }

  /**
   * Creates a popup window.
   *
   * @param title: title of the popup window
   * @param message: message to display
   */
  public static void makePopupWindow(String title, String message) {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    VBox dialogPane = new VBox();

    // create UI elements
    Text text = new Text(message);
    Button okButton = new Button("OK");
    okButton.setOnAction(a -> dialog.close());

    // display the popup window
    int innerPadding = 10; // inner padding/spacing
    int outerPadding = 100; // outer padding
    dialogPane.setSpacing(innerPadding);
    dialogPane.setAlignment(Pos.CENTER);
    dialogPane.setPadding(new Insets(innerPadding, innerPadding, innerPadding, innerPadding));
    dialogPane.getChildren().addAll(text, okButton);
    Scene dialogScene = new Scene(dialogPane, outerPadding + 5 * message.length(), outerPadding);
    dialog.setScene(dialogScene);
    dialog.setTitle(title);
    dialog.show();
  }

  public static void showError(String message) {
    makePopupWindow("Error", message);
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of member emails
   */
  public static ObservableList<String> getMemberEmails() {
    return FXCollections.observableList(MemberController.getMemberEmails());
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of member emails who have an assignment object associated with them
   */
  public static ObservableList<String> getAssignedMemberEmails() {
    return FXCollections.observableList(MemberController.getAssignedMemberEmails());
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of Tranfer Objects for all members
   */
  public static ObservableList<TOMember> getMembers() {
    List<TOMember> members = MemberController.getMembers();
    return FXCollections.observableList(members);
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of Transfer Objects for all Assignments
   */
  public static ObservableList<TOAssignment> getAssignments() {
    List<TOAssignment> assignments = AssignmentController.getAssignments();
    return FXCollections.observableList(assignments);
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of Transfer Objects for all Equipment
   */
  public static ObservableList<TOEquipment> getEquipments() {
    List<TOEquipment> equipment = EquipmentController.getEquipments();
    return FXCollections.observableList(equipment);
  }

  /**
   * @Author Shidan Javaheri
   * @return an observable list of Transfer Objects for all Bundles
   */
  public static ObservableList<TOBundle> getBundles() {
    List<TOBundle> bundle = BundleController.getBundles();
    return FXCollections.observableList(bundle);
  }

}
