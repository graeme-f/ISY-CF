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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.ErrorMessage;

/**
 *
 * @author gfoster
 */
public class FXMLNewCarController  implements Initializable {
    
    @FXML private TextField description;
    @FXML private TextField registration;
    @FXML private ComboBox vehicleTypes;
    @FXML private ComboBox fuelTypes;
    
    @FXML private Button btnCancel;
    @FXML private Button btnCreate;
    
    private TransportationDataCollector dc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = TransportationDataCollector.getInstance();
        
        description.clear();
        registration.clear();
        
        ArrayList<String> makes = dc.getCarTypeList();
        ObservableList<String> vehicleMakes = FXCollections.<String>observableArrayList(makes);
        vehicleTypes.getItems().addAll(vehicleMakes);
        vehicleTypes.setPromptText("Type of Vehicle");
        vehicleTypes.setEditable(true);
        
        ArrayList<String> fuels = dc.getFuelList();
        ObservableList<String> fuelType = FXCollections.<String>observableArrayList(fuels);
        fuelTypes.getItems().addAll(fuelType);
        fuelTypes.setPromptText("Type of Fuel");
        fuelTypes.setEditable(true);
        
    } // end of method initialize
    
    @FXML private void closeWindow(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    } // end of method CloseWindow

    @FXML private void saveAndCloseWindow(ActionEvent event) {
        if (!valid())
        	return;
        dc = TransportationDataCollector.getInstance();
        String result = dc.createNewVehicle(description.getText()
                , registration.getText()
                , vehicleTypes.getValue().toString()
                , fuelTypes.getValue().toString());
        if (null == result){
            ErrorMessage.display("An error occurred while trying to save the new vehicle data.");
            return;
        } else {
        	ErrorMessage.display("Information", result, "New Vehicle created.");
        }
        closeWindow(event);
    } // end of method CloseWindow
    
    private boolean valid() {
    	if (description.getText().isEmpty()) return false;
        if (vehicleTypes.getValue().toString().isEmpty()) return false;
        if (fuelTypes.getValue().toString().isEmpty()) return false;
    	return true;
    } // end of method valid
    public String getCarDesc() {
    	return description.getText();
    } // end of method getCarDesc
    
} // end of class FXMLNewCarController
