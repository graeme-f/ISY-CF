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
import java.util.function.UnaryOperator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

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
    @FXML private TitledPane transportPane;
    
    @FXML private Label VehicleDescription;
    
    @FXML private ListView vehicleLists;
    @FXML private ChoiceBox fuelList;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField fuelTotal;
    
    
    @FXML private Button btnAddVehicle;
    @FXML private Button btnDeleteVehicle;
    @FXML private Button btnUpdateFuel;
    
    @FXML private GridPane gpVehicleForm;
    private String vehicleName;
    
    private TransportationDataCollector dc;
    @FXML private void UpdateFuel(ActionEvent event) {
        String start  = startDate.getValue().toString();
        String end    = endDate.getValue().toString();
        String fuel   = fuelList.getSelectionModel().getSelectedItem().toString();
        String amount = fuelTotal.getText();
        dc.updateFuel(vehicleName,start,end,fuel,amount);
        fuelTotal.textProperty().addListener((observable, oldvalue, newvalue)
        ->{
                if(newvalue.isEmpty())
                {
                    btnUpdateFuel.setDisable(true);
                }
                else
                {
                    btnUpdateFuel.setDisable(false);
                }
                
        });
    }
    
    @FXML private void DeleteVehicle(ActionEvent event) {
        dc.deleteVehicle(vehicleName);
    }
    @FXML private void AddVehicle(ActionEvent event) {
        System.out.println("Add Vehicle");
        event.consume();
        
        Scene newScene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNewCar.fxml"));
        try {
            newScene = new Scene(loader.load());
        } catch (IOException ex) {
            System.out.println("Fail");
            System.out.println(ex);
            //TODO: handle error
            return;
        }

        Stage inputStage = new Stage();
        inputStage.initOwner(Transportation.primaryStage);
        inputStage.setTitle("Create a new vehicle");
        inputStage.setScene(newScene);
        inputStage.showAndWait();
    }
    
    public void setVehicleSummary(String vehicleName){
        details.setText(dc.vehicleSummary(vehicleName));
    } // end of method setVehicleSummary()
    
    private void setVehicleDetails(String vehicleName){
        VehicleDescription.setText(vehicleName);
        fuelList.setValue(dc.getFuel(vehicleName));
        startDate.setValue(dc.getStartDate(vehicleName));
        endDate.setValue(LocalDate.now());
        fuelTotal.clear();
        btnUpdateFuel.setDisable(false);
    } // end of method setVehicleDetails()
    
    private void vehicleChanged(ObservableValue<? extends String> observable,String oldValue,String newValue){
        vehicleName = newValue;
        gpVehicleForm.setVisible(true);
        setVehicleDetails(newValue);
        setVehicleSummary(newValue);

        fuelTotal.requestFocus(); // Doesn't seem to work :(
    } // end of method vehicleChanged
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = TransportationDataCollector.getInstance();
        initialiseVehicles();        
    } // end of method initialize    
    
    private void initialiseVehicles(){
        // The following code will bind the managed property to the visibility of the container
        // If the container's visibility is turned off it will not be managed by the window
        // Which means that is will not take up any space in the window.
        ArrayList<String> cars = dc.getCarList();
        ObservableList<String> carList = FXCollections.<String>observableArrayList(cars);
        vehicleLists.getItems().addAll(carList);
        gpVehicleForm.setVisible(false);
        btnUpdateFuel.setDisable(true);
        //btnDeleteVehicle.setDisable(true);
        
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
        fuelTotal.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));

    } // end of method initialiseVehicles()
    
    UnaryOperator<Change> integerFilter = change -> {
    String newText = change.getControlNewText();
    if (newText.matches("-?([1-9][0-9]*)?")) { 
        return change;
    }
    return null;
};


} // end of class FXMLTransportationController
