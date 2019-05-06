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
package PowerUsage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * FXML Controller class
 *
 * @author gfoster
 */
public class FXMLPowerUsageController implements Initializable {

    @FXML private HBox ContainerBox;

    @FXML private VBox Electricity;
    @FXML private TextArea ElectricityDetails;
    @FXML private Label ElectricityDescription;
    @FXML private DatePicker ElectricityStartDate;
    @FXML private DatePicker ElectricityEndDate;
    @FXML private TextField meterUnits;
    @FXML private Button btnUpdateElectricity;

    @FXML private VBox Generator;
    @FXML private TextArea GeneratorDetails;
    @FXML private Label GeneratorDescription;
    @FXML private DatePicker GeneratorStartDate;
    @FXML private DatePicker GeneratorEndDate;
    @FXML private ChoiceBox fuelBox;
    @FXML private TextField fuelAmount;
    @FXML private Button btnAddGenerator;
    @FXML private Button btnDeleteGenerator;
    @FXML private Button btnUpdateGenerator;

    @FXML private VBox AC;
    @FXML private ListView ACList;
    @FXML private ChoiceBox ACTypeList;
    @FXML private ChoiceBox RefrigerantList;
    @FXML private DatePicker ACStartDate;
    @FXML private DatePicker ACEndDate;
    @FXML private Button btnAddAC;
    @FXML private Button btnDeleteAC;
    @FXML private Button btnUpdateAC;

    //private PowerUsageDataCollector dc;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
