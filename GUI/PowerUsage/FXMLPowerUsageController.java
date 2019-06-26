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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utility.ErrorMessage;
import utility.GUIController;

/**
 * FXML Controller class
 *
 * @author gfoster
 */
public class FXMLPowerUsageController extends GUIController implements Initializable {

    @FXML private HBox ContainerBox;
    @FXML private Accordion leftSidePanel;
    @FXML private TitledPane electricityPane;
    @FXML private TitledPane generatorPane;
    @FXML private TitledPane acPane;
    @FXML private TextArea details;
    
    private PowerUsageDataCollector dc;
    
    @FXML private VBox ElectricityBox;
    @FXML private ListView<Integer> ElectricityLists;
    @FXML private DatePicker ElectricityStartDate;
    @FXML private DatePicker ElectricityEndDate;
    @FXML private TextField meterUnits;
    @FXML private Button btnSetEndDate;
    @FXML private Button btnUpdateElectricity;

    @FXML private VBox GeneratorBox;
    @FXML private TextArea GeneratorDetails;
    @FXML private TextField GeneratorDescription;
    @FXML private DatePicker GeneratorStartDate;
    @FXML private DatePicker GeneratorEndDate;
    @FXML private TextField fuelAmount;
    @FXML private Button btnSetGenEndDate;
    @FXML private Button btnUpdateGenerator;

    @FXML private GridPane ACBox;
    @FXML private Button btnUpdateAC;
    private int AC_Count;

    @FXML private void updateElectricity(ActionEvent event) {
        String start  = ElectricityStartDate.getValue().toString();
        String end    = ElectricityEndDate.getValue().toString();
        String amount = meterUnits.getText();
        String result = dc.insertElectricityData(start, end, amount);
        // Display the result on the screen, this also delays the processing
        // enough so that the electricity summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the electricity details.");
        } else {
            ErrorMessage.display("Information", result, "Electricity details updated");
        }
        initializeElectricity();
    } // end of UpdateElectricity
    
    @FXML private void updateGenerator(ActionEvent event) {
        String start = GeneratorStartDate.getValue().toString();
        String end = GeneratorEndDate.getValue().toString();
        String amount = fuelAmount.getText();
        String result = dc.insertGeneratorData(start, end, amount);
        // Display the result on the screen, this also delays the processing
        // enough so that the electricity summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the generator details.");
        } else {
            ErrorMessage.display("Information", result, "Generator details updated");
        }
        initializeGenerator();
        details.setText(dc.generatorSummary());
    } // end of method updateGenerator

    @FXML private void updateACUnits(ActionEvent event) {
    	Node[][] gridPaneNodes = new Node[3][AC_Count+1] ;
    	for (Node child : ACBox.getChildren()) {
    	    Integer column = GridPane.getColumnIndex(child);
    	    Integer row = GridPane.getRowIndex(child);
    	    if (column != null && row != null) {
    	        gridPaneNodes[column][row] = child ;
    	    }
    	}
    	
    	String result = "";
    	boolean noChange = true;
    	for (int row = 1; row <= AC_Count; row++) {
    	        String name = ((Label)gridPaneNodes[0][row]).getText() ;
    	        int amount = Integer.parseInt(((TextField)gridPaneNodes[1][row]).getText());
    	        int multiplier = Integer.parseInt(((TextField)gridPaneNodes[2][row]).getText());
    	        if (dc.getACAmount(name) != amount) {
    	        	noChange = false;
    	        	result += dc.updateACAmount(dc.getAC_ID(name), amount);
    	        }
    	        if (dc.getACMultiplier(name) != multiplier) {
    	        	noChange = false;
    	        	result += dc.updateACMultiplier(dc.getAC_ID(name), multiplier);
    	        }
    	}
    	if (noChange) return;
    	
        if (result.isEmpty()){
            ErrorMessage.display("Unable to update the Air Conditioner details.");
        } else {
            ErrorMessage.display("Information", result, "Air Conditioner details updated");
        }
        details.setText(dc.acSummary());
    } // end of method updateGenerator

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = PowerUsageDataCollector.getInstance();
        leftSidePanel.setExpandedPane(electricityPane);
        leftSidePanel.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            public void changed(ObservableValue<? extends TitledPane> ov, TitledPane oldValue, TitledPane newValue) {
                if (newValue == electricityPane) {
                    details.setText(dc.electricitySummary());
                } else if (newValue == generatorPane) {
                    details.setText(dc.generatorSummary());
                } else if (newValue == acPane) {
                    details.setText(dc.acSummary());
                }
            } // end of accordion listener
        });

        attachEndDateAction(btnSetEndDate, ElectricityStartDate, ElectricityEndDate);
        intFilter(meterUnits, btnUpdateElectricity);
        initializeElectricity();
        
        attachEndDateAction(btnSetGenEndDate, GeneratorStartDate, GeneratorEndDate);
        intFilter(fuelAmount, btnUpdateGenerator);
        initializeGenerator();
        
        initializeAC();
    } // end of method initialize
    
    private void initializeElectricity() {
        ElectricityStartDate.setValue(dc.getLastDate("Electricity"));
        if (defaultToToday){
            btnSetEndDate.setText("One Month");
            ElectricityEndDate.setValue(LocalDate.now());
        } else {
            btnSetEndDate.setText("Today");
            ElectricityEndDate.setValue(ElectricityStartDate.getValue().plusMonths(1).minusDays(1));
        }
        btnUpdateElectricity.setDisable(true);
        meterUnits.clear();
        details.setText(dc.electricitySummary());
    } // end of method initializeElectricity()

    private void initializeGenerator() {
        GeneratorStartDate.setValue(dc.getLastDate("Generator"));
        if (defaultToToday){
            btnSetGenEndDate.setText("One Month");
            GeneratorEndDate.setValue(LocalDate.now());
        } else {
            btnSetGenEndDate.setText("Today");
            GeneratorEndDate.setValue(GeneratorStartDate.getValue().plusMonths(1).minusDays(1));
        }
        btnUpdateGenerator.setDisable(true);
        fuelAmount.clear();
    } // end of method initializeGenerator
    
    private void initializeAC(){
        ArrayList<String> ACTypes = dc.getAcTypeList();
        AC_Count = 0;
        Label lbl = new Label();
        lbl.setText("Description");
        ACBox.add(lbl, 0, AC_Count);
        lbl = new Label();
        lbl.setText("Total Units");
        ACBox.add(lbl, 1, AC_Count);
        lbl = new Label();
        lbl.setText("Multiplier");
        ACBox.add(lbl, 2, AC_Count);

        TextField txt;
        for (String name : ACTypes){
        	AC_Count++;

            lbl = new Label();
            lbl.setText(name);
            ACBox.add(lbl, 0, AC_Count);

            txt = new TextField();
            intFilter(txt);
            txt.setText(Integer.toString(dc.getACAmount(name)));
            ACBox.add(txt, 1, AC_Count);

            txt = new TextField();
            intFilter(txt);
            txt.setText(Integer.toString(dc.getACMultiplier(name)));
            ACBox.add(txt, 2, AC_Count);
        }
    } // end of method initializeAC
        
} // end of class FXMLPowerUsageController
