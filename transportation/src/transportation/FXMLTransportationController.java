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

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author gfoster
 */
public class FXMLTransportationController implements Initializable {
    
    @FXML private HBox ContainerBox;
    @FXML private VBox VehicleBox;
    @FXML private VBox DetailsBox;
    @FXML private VBox TripBox;
    @FXML private TextArea details;
    
    @FXML private Label VehicleDescription;
    
    @FXML private ListView vehicleLists;
    @FXML private ChoiceBox fuelList;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField fuelTotal;
    
    
    @FXML private GridPane gpVehicleForm;
    
    public void setVehicleSummary(String vehicleName){
        // TODO the vehicle summary needs to come from the database
        String vehicleSummary = "";
        vehicleSummary += vehicleName + "\n\n";
        vehicleSummary += "January" + "\t" + "63 litres" + "\n";
        vehicleSummary += "February" + "\t" + "157 litres" + "\n";
        vehicleSummary += "March" + "\t" + "83 litres" + "\n";
        vehicleSummary += "\nTotal usage" + "\t" + "303 litres";
        details.setText(vehicleSummary);
    } // end of method setVehicleSummary()
    
    private void setVehicleDetails(String vehicleName){
        VehicleDescription.setText(vehicleName);
        // TODO selected vehicle details need to be obtained from the database
        if (vehicleName.equals("Blue Car")){
            fuelList.setValue(fuelList.getItems().get(0));
            startDate.setValue(LocalDate.of(2019, 02, 28)); // This should default to the last recorded end date
            endDate.setValue(LocalDate.now()); // This should default to today's date
            fuelTotal.clear();
        } else {
            fuelList.setValue(fuelList.getItems().get(1));
            startDate.setValue(LocalDate.of(2019, 03, 14)); // This should default to the last recorded end date
            endDate.setValue(LocalDate.now()); // This should default to today's date
            fuelTotal.clear();
        }    
    } // end of method setVehicleDetails()
    
    private void vehicleChanged(ObservableValue<? extends String> observable,String oldValue,String newValue){
        gpVehicleForm.setVisible(true);
        setVehicleDetails(newValue);
        setVehicleSummary(newValue);
        // Hide the trip box but show the details box
        TripBox.setVisible(false);
        DetailsBox.setVisible(true);

        fuelTotal.requestFocus(); // Doesn't seem to work :(
    } // end of method vehicleChanged
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialiseVehicles();        
    } // end of method initialize    
    
    private void initialiseVehicles(){
        // The following code will bind the managed property to the visibility of the container
        // If the container's visibility is turned off it will not be managed by the window
        // Which means that is will not take up any space in the window.
        VehicleBox.managedProperty().bind(VehicleBox.visibleProperty());
        DetailsBox.managedProperty().bind(DetailsBox.visibleProperty());
        TripBox.managedProperty().bind(TripBox.visibleProperty());
        // Hide the DetailsBox
        DetailsBox.setVisible(false);
        // TODO vehicle list needs to come from the database
        ObservableList<String> carList = FXCollections.<String>observableArrayList("Red Car, the fast one. With a dent on the side", "Blue Car", "Orange Car", "Yellow Car");
        vehicleLists.getItems().addAll(carList);
        gpVehicleForm.setVisible(false);
        
        // Update and show the details when a vehicle is selected
        vehicleLists.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> ov,
                    final String oldvalue, final String newvalue) 
            {
                vehicleChanged(ov, oldvalue, newvalue);
        }});
        
        // TODO fuel types need to come from the database
        ObservableList<String> fuelType = FXCollections.<String>observableArrayList("Diesel", "Petrol");
        fuelList.setItems(fuelType);
        
        details.setEditable(false);

    } // end of method initialiseVehicles()
    
} // end of class FXMLTransportationController
