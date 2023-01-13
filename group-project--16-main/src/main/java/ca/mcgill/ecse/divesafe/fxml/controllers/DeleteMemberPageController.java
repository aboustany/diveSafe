package ca.mcgill.ecse.divesafe.fxml.controllers;

import ca.mcgill.ecse.divesafe.controller.MemberController;
import ca.mcgill.ecse.divesafe.controller.TOMember;
import ca.mcgill.ecse.divesafe.javafx.fxml.DiveSafeFxmlView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DeleteMemberPageController {
	@FXML
	private VBox memberDisplayPage;
	@FXML
	private GridPane mainMemberDisplayGrid;
	@FXML
	private GridPane memberDisplayTableGrid;
	@FXML
	private Label memberDisplayTitle;
	@FXML
	private GridPane searchByEmailGrid;
	@FXML
	private Label enterEmailLabel;
	@FXML
	private TextField emailInput;
	@FXML
	private Button searchButton;
	@FXML
	private Button deleteBySearchButton;
	@FXML
	private Button deleteByTableButton;
	@FXML
	private TableView<TOMember> memberDisplayTable;
	
	/*
	 * Helper Methods *
	 				  */
	
	/**
	 * Displays the string value of the property for each instance in the table
	 * @author Shyam Desai
	 * @param header
	 * @param propertyName
	 * @return column
	 */
	public static TableColumn<TOMember, String> createTableColumn(String header, String propertyName) {
	    TableColumn<TOMember, String> column = new TableColumn<>(header);
	    column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
	    return column;
	}
	
	/**
	 * Gets all member items
	 * Function inspired by BTMS ViewAssignmentPageController 
	 * @author Shyam Desai
	 * @return assignments - email, name, Emergency Contact, Number Of Days, Guide Required, and Hotel Required for all members
	 */
	public ObservableList<TOMember> getMemberItems() {
		ObservableList<TOMember> assignments = ViewUtils.getMembers();
		return assignments;
	}
	
	/**
	 * Populates Member data table with member items
	 * @author Shyam Desai
	 */
	@FXML
	public void populateTable() {
		memberDisplayTable.getItems().clear();
		memberDisplayTable.setItems(getMemberItems());
	}
	
	/**
	 * Gets the email of the Member from the row clicked by user
	 * @author Shyam Desai
	 * @return email
	 */
	@FXML
	public String getEmailOfRowClicked() {
        return memberDisplayTable.getSelectionModel().getSelectedItem().getEmail();
	}
	
	/**
	 * Creates table columns and populates Member display table with member items
	 * @author Shyam Desai
	 */
	@FXML
	public void initialize() {
		memberDisplayTable.getColumns().add(createTableColumn("Email", "email"));
		memberDisplayTable.getColumns().add(createTableColumn("Name", "name"));
		memberDisplayTable.getColumns().add(createTableColumn("Emergency Contact", "emergencyContact"));
		memberDisplayTable.getColumns().add(createTableColumn("Number of Days", "numberOfDays"));
		memberDisplayTable.getColumns().add(createTableColumn("Guide Required", "guideRequired"));
		memberDisplayTable.getColumns().add(createTableColumn("Hotel Required", "hotelRequired"));
		memberDisplayTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		DiveSafeFxmlView.getInstance().registerRefreshEvent(memberDisplayTable); //register refreshable nodes
		memberDisplayTable.addEventHandler(DiveSafeFxmlView.REFRESH_EVENT, e -> populateTable()); 
	}
	
	// Event Listener on Button[#searchButton].onAction
	
	/**
	 * @author Sami Ferneini
	 * @param event
	 */
	@FXML
	public void searchMemberClicked(ActionEvent event) {
		memberDisplayTable.getSelectionModel().clearSelection();
		String email = emailInput.getText();

		if (email.equals("")) {
		      ViewUtils.showError("Please enter a non empty email");
		      DiveSafeFxmlView.getInstance().refresh();
		    } else {
		    	TOMember searchedFor = null;
		        int counter = 0;
		        ObservableList<TOMember> allMembers = ViewUtils.getMembers();
		        for (TOMember member : allMembers) {
		          if (member.getEmail().equals(email)) {
		            searchedFor = member;
		            break;
		          }
		          counter++;
		        }
		        if (searchedFor != null) {
		        	memberDisplayTable.getSelectionModel().selectFirst();
		          for (int i = 0; i < counter; i++) {
		        	  memberDisplayTable.getSelectionModel().selectNext();
		          }
		          emailInput.setText("");

		        } else {
		          ViewUtils.showError("A member with this member email was not found");
		          DiveSafeFxmlView.getInstance().refresh();
		        }

		    }
		
	}
		
	/**
	 * Deletes the Member data based on the row selected on the Member display table
	 * Then, updates member items on the display table
	 * @author Shyam Desai 
	 */
	@FXML
	public void deleteMemberByTableClicked() {
		
		String email = getEmailOfRowClicked();
		ViewUtils.callController(MemberController.deleteMember(email));

		populateTable();
	}
}
