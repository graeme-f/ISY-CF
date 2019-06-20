/*
 * The MIT License
 *
 * Copyright 2019 gfoster.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package transportation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import utility.ErrorMessage;
import utility.GUIController;

/**
 *
 * @author gfoster
 */
public class FXMLTransportationController extends GUIController implements Initializable {
    
    
    /*************************************************************************/
    /*   ACCORDION
    /*************************************************************************/
    @FXML private Accordion leftSidePanel;
    @FXML private TitledPane transportPane;
    @FXML private TitledPane tripPane;

    @FXML private TextArea details;

    /*************************************************************************/
    /*   VEHICLES
    /*************************************************************************/
    @FXML private Label VehicleDescription;
    
    @FXML private ListView<String> vehicleLists;
    @FXML private ChoiceBox<String> fuelList;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField fuelTotal;
    @FXML private Button btnSetVehicleEndDate;
    @FXML private Button btnDeleteVehicle;
    @FXML private Button btnUpdateFuel;
    
    @FXML private GridPane gpVehicleForm;
    private String vehicleName;
    private ObservableList<String> carList;
    
    /*************************************************************************/
    /*   TRIPS
    /*************************************************************************/
    @FXML private ChoiceBox<String> tripOptions;
    @FXML private GridPane tripDetails;
    @FXML private RowConstraints tripStudentRow;
    @FXML private DatePicker tripStartDate;
    @FXML private DatePicker tripEndDate;
    @FXML private TextField tripDescription;
    @FXML private TextField tripBusesNumber;
    @FXML private TextField tripBusDistance;
    @FXML private TextField tripAirDistance;
    @FXML private TextField tripStudentNumber;
    @FXML private Label tripSNLabel;
    @FXML private Label tripSNRequired;
    @FXML private TextField tripTeacherNumber;
    
    @FXML private Button btnAddTrip;
    
    
    private TransportationDataCollector dc;
    
    @FXML private void UpdateFuel(ActionEvent event) {
        String start  = startDate.getValue().toString();
        String end    = endDate.getValue().toString();
        String fuel   = fuelList.getSelectionModel().getSelectedItem().toString();
        String amount = fuelTotal.getText();
        String result = dc.updateFuel(vehicleName,start,end,fuel,amount);
        // Display the result on the screen, this also delays the processing
        // enough so that the vehicle summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the fuel details.");
        } else {
            ErrorMessage.display("Information", result, "Fuel details updated");
        }
        setVehicleDetails(vehicleName);
        setVehicleSummary(vehicleName);
    } // end of method UpdateFuel
    
    @FXML private void DeleteVehicle(ActionEvent event) {
        String result = dc.deleteVehicle(vehicleName);
        if (null != result){
            carList.remove(vehicleName);
        }
    }
    
    @FXML private void addOneMonth(ActionEvent event){
        endDate.setValue(startDate.getValue().plusMonths(1).minusDays(1));
    }
    
    @FXML private void AddVehicle(ActionEvent event) {
        System.out.println("Add Vehicle");
        event.consume();
        
        Scene newScene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNewCar.fxml"));
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            System.out.println("Fail");
            System.out.println(e);
            ErrorMessage.display("Unable to open window", e.getMessage());
            return;
        }
        
        Stage inputStage = new Stage();
        inputStage.initOwner(Transportation.primaryStage);
        inputStage.setTitle("Create a new vehicle");
        inputStage.setScene(newScene);
        inputStage.showAndWait();
        FXMLNewCarController ncc = loader.<FXMLNewCarController>getController();
        carList.add(ncc.getCarDesc());
    } // end of method AddVehicle
    
    @FXML private void AddTrip(ActionEvent event) {
    	String trip = tripOptions.getSelectionModel().getSelectedItem();
        String start  = tripStartDate.getValue().toString();
        String end    = tripEndDate.getValue().toString();
        String desc   = tripDescription.getText();
        String busNumber = getNumber(tripBusesNumber);
        String busDistance = getNumber(tripBusDistance);
        String airDistance = getNumber(tripAirDistance);
        String studentNumber = getNumber(tripStudentNumber);
        String teacherNumber = getNumber(tripTeacherNumber);
        String result = dc.addTrip(trip
        		                  ,start
        		                  ,end
        		                  ,desc
        		                  ,busNumber
        		                  ,busDistance
        		                  ,airDistance
        		                  ,studentNumber
        		                  ,teacherNumber
        		                  );
        // Display the result on the screen, this also delays the processing
        // enough so that the trip summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the trip details.");
        } else {
            ErrorMessage.display("Information", result, "Trip details updated");
        }
        details.setText(dc.tripSummary(trip));
    } // end of method AddTrip
 

    
    

    public void setVehicleSummary(String vehicleName){
        details.setText(dc.vehicleSummary(vehicleName));
    } // end of method setVehicleSummary()
    
    private void setVehicleDetails(String vehicleName){
        VehicleDescription.setText(vehicleName);
        fuelList.setValue(dc.getFuel(vehicleName));
        startDate.setValue(dc.getStartDate(vehicleName));
        if (defaultToToday){
            btnSetVehicleEndDate.setText("One Month");
            endDate.setValue(LocalDate.now());
        } else {
            btnSetVehicleEndDate.setText("Today");
            endDate.setValue(startDate.getValue().plusMonths(1).minusDays(1));
        }
        attachEndDateAction(btnSetVehicleEndDate, startDate, endDate);
        fuelTotal.clear();
        btnUpdateFuel.setDisable(true);
    } // end of method setVehicleDetails()
    
    private void vehicleChanged(ObservableValue<? extends String> observable,String oldValue,String newValue){
        vehicleName = newValue;
        gpVehicleForm.setVisible(true);
        setVehicleDetails(newValue);
        setVehicleSummary(newValue);
        
        btnDeleteVehicle.setDisable(false);
        fuelTotal.requestFocus(); // Doesn't seem to work :(
    } // end of method vehicleChanged
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        leftSidePanel.setExpandedPane(transportPane);
        dc = TransportationDataCollector.getInstance();
        details.setText(dc.vehicleDisplay());
        initialiseVehicles();
        initialiseTrips();
        leftSidePanel.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			public void changed(ObservableValue<? extends TitledPane> ov, TitledPane oldValue, TitledPane newValue) {
		        if (newValue == transportPane) {
		        	details.setText(dc.vehicleDisplay());
		        } else if (newValue == tripPane) {
		        	details.setText(dc.tripDisplay());
		        }
			}
        });
    } // end of method initialize    
    
    private void initialiseTrips() {
    	ObservableList<String> tripList = FXCollections.<String>observableArrayList(dc.getTripList());
    	tripOptions.setItems(tripList);
    	tripOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    final String oldValue, final String newValue) 
            {
            	tripDetails.setVisible(true);
            	if (dc.tripStaffOnly(newValue)) {
            		tripStudentRow.setPrefHeight(0);
            		tripStudentRow.setMaxHeight(0);
            		tripSNLabel.setVisible(false);
            		tripSNRequired.setVisible(false);
            		tripStudentNumber.setVisible(false);
                    tripStudentNumber.clear();
            	} else {
            		tripStudentRow.setPrefHeight(35);
            		tripStudentRow.setMaxHeight(35);
            		tripSNLabel.setVisible(true);
            		tripSNRequired.setVisible(true);
            		tripStudentNumber.setVisible(true);
            	}
            	details.setText(dc.tripSummary(newValue));
        }});
        tripStartDate.setValue(LocalDate.now());
        tripEndDate.setValue(LocalDate.now());
        setRequired(tripDescription);
        setBorder(tripDescription,"");
        intFilter(tripBusesNumber);
        intFilter(tripBusDistance);
        intFilter(tripAirDistance);
        intFilter(tripStudentNumber,true);
        intFilter(tripTeacherNumber,true);
        tripDescription.clear();
        tripBusesNumber.clear();
        tripBusDistance.clear();
        tripAirDistance.clear();
        tripStudentNumber.clear();
        tripTeacherNumber.clear();
        tripDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            enableAddTrip();
        });
        tripBusesNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            enableAddTrip();
        });
        tripBusDistance.textProperty().addListener((observable, oldValue, newValue) -> {
            enableAddTrip();
        });
        tripAirDistance.textProperty().addListener((observable, oldValue, newValue) -> {
            enableAddTrip();
        });
        tripStudentNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            setBorder(tripStudentNumber, newValue);
            enableAddTrip();
        });
        tripTeacherNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            setBorder(tripTeacherNumber, newValue);
            enableAddTrip();
        });
        enableAddTrip();
    }
    
    private boolean validTrip() {
    	if (tripDescription.getText().isEmpty()) return false;
    	if (tripStudentNumber.getText().isEmpty() && !dc.tripStaffOnly(tripOptions.getSelectionModel().getSelectedItem())) return false;
    	if (tripTeacherNumber.getText().isEmpty()) return false;
    	if (tripBusDistance.getText().isEmpty()
    	   &&tripAirDistance.getText().isEmpty()
    	   ) return false;
    	if (tripBusDistance.getText().isEmpty()
    	   && !tripBusesNumber.getText().isEmpty()
    	   ) return false;
    	if (!tripBusDistance.getText().isEmpty()
    	   && tripBusesNumber.getText().isEmpty()
    	   ) return false;
    		
    	return true;
    } // end of method validTrip
    
    private void enableAddTrip(){
        if (validTrip()) {
        	btnAddTrip.setDisable(false);
        } else {
        	btnAddTrip.setDisable(true);
        }
    } // end of method enableAddTrip
    
    private void initialiseVehicles(){
        // The following code will bind the managed property to the visibility of the container
        // If the container's visibility is turned off it will not be managed by the window
        // Which means that is will not take up any space in the window.
//        ArrayList<String> cars = dc.getCarList();
        carList = FXCollections.<String>observableArrayList(dc.getCarList());
        vehicleLists.setItems(carList);//.addAll(carList);
        gpVehicleForm.setVisible(false);
        btnUpdateFuel.setDisable(true);
        btnDeleteVehicle.setDisable(true);
        
        // Update and show the details when a vehicle is selected
        vehicleLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    final String oldvalue, final String newvalue) 
            {
                vehicleChanged(ov, oldvalue, newvalue);
        }});
        
        ArrayList<String> fuels = dc.getFuelList();
        ObservableList<String> fuelType = FXCollections.<String>observableArrayList(fuels);
        fuelList.setItems(fuelType);
        intFilter(fuelTotal);
        fuelTotal.textProperty().addListener((observable, oldvalue, newvalue)
        ->{
            if(newvalue.isEmpty() || newvalue.equals("-"))
            {
                setBorder(fuelTotal, "");
                btnUpdateFuel.setDisable(true);
            } else {
                setBorder(fuelTotal, newvalue);
                btnUpdateFuel.setDisable(false);
            }
        });

    } // end of method initialiseVehicles()
    
} // end of class FXMLTransportationController
