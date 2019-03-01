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
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author gfoster
 */
public class FXMLTransportationController implements Initializable {
    
    @FXML
    private Label VehicleDescription;
    
    @FXML private ListView vehicleLists;
    @FXML private ChoiceBox fuelList;
    @FXML private TextField startDate;
    @FXML private TextField endDate;
    @FXML private TextField fuelTotal;
    
    
    @FXML
    private GridPane gpVehicleForm;
    
    private void vehicleChanged(ObservableValue<? extends String> observable,String oldValue,String newValue){
        VehicleDescription.setText(newValue);
        gpVehicleForm.setVisible(true);
        if (newValue.equals("Blue Car")){
            fuelList.setValue(fuelList.getItems().get(0));
            startDate.setText("1 January 2019");
            endDate.setText("1 March 2019");
            fuelTotal.clear();
        } else {
            fuelList.setValue(fuelList.getItems().get(1));
            startDate.setText("1 February 2019");
            endDate.setText("1 March 2019");
            fuelTotal.clear();
        }
        fuelTotal.requestFocus();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialiseVehicles();        
    } // end of method initialize    
    
    private void initialiseVehicles(){
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

        ObservableList<String> fuelType = FXCollections.<String>observableArrayList("Diesel", "Petrol");
        fuelList.setItems(fuelType);
    }
}
