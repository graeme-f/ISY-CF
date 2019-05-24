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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author gfoster
 */
public class FXMLPowerUsageController implements Initializable {

    @FXML private HBox ContainerBox;

    @FXML private VBox ElectricityBox;
    @FXML private ListView<Integer> ElectricityLists;
    @FXML private TextArea ElectricityDetails;
    @FXML private TextField ElectricityId;
    @FXML private Label ElectricityDescription;
    @FXML private DatePicker ElectricityStartDate;
    @FXML private DatePicker ElectricityEndDate;
    @FXML private TextField meterUnits;
    @FXML private Button btnUpdateElectricity;

    @FXML private VBox GeneratorBox;
    @FXML private TextArea GeneratorDetails;
    @FXML private TextField GeneratorDescription;
    @FXML private DatePicker GeneratorStartDate;
    @FXML private DatePicker GeneratorEndDate;
    @FXML private ChoiceBox fuelBox;
    @FXML private TextField fuelAmount;
    @FXML private Button btnAddGenerator;
    @FXML private Button btnDeleteGenerator;
    @FXML private Button btnUpdateGenerator;

    @FXML private VBox ACBox;
    @FXML private ListView ACList;
    @FXML private ChoiceBox ACTypeList;
    @FXML private ChoiceBox RefrigerantList;
    @FXML private DatePicker ACStartDate;
    @FXML private DatePicker ACEndDate;
    @FXML private Button btnAddAC;
    @FXML private Button btnDeleteAC;
    @FXML private Button btnUpdateAC;

    @FXML private void updateElectricity(ActionEvent event) {
        String start  = ElectricityStartDate.getValue().toString();
        String end    = ElectricityEndDate.getValue().toString();
        String amount = meterUnits.getText();
        dc.insertElectricityData(start, end, amount);
    } // end of UpdateElectricity
    
    @FXML private void updateGenerator(ActionEvent event) {
        String start = GeneratorStartDate.getValue().toString();
        String end = GeneratorEndDate.getValue().toString();
        String amount = fuelAmount.getText();
        dc.insertGeneratorData(start, end, amount);
    }

    private PowerUsageDataCollector dc;

    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    };

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        dc = PowerUsageDataCollector.getInstance();
        initializeAll();
    }
    
    private void initializeAll() {
        initializeElectricity();
        initializeGenerators();
        setAllDetails();
    }
    
    private void setAllDetails() {
        setElectricityDetails();
        setGeneratorDetails();
    }

    private void initializeElectricity() {
        btnUpdateElectricity.setDisable(false);
        meterUnits.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
    }

    private void setElectricityDetails() {
        ArrayList<Integer> electricity = dc.getElectricityList();
        ElectricityStartDate.setValue(dc.getLastDate());
        ElectricityEndDate.setValue(LocalDate.now());
        meterUnits.clear();
    }

    private void initializeGenerators() {
        btnUpdateGenerator.setDisable(false);
        fuelAmount.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
    }
    
    private void setGeneratorDetails() {
        ArrayList<Integer> generators = dc.getGeneratorList();
        GeneratorStartDate.setValue(dc.getLastDate());
        GeneratorEndDate.setValue(LocalDate.now());
        fuelAmount.clear();
    }
    
}
