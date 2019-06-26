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
package Consumables;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.util.converter.IntegerStringConverter;
import utility.ErrorMessage;
import utility.GUIController;

/**
 * FXML Controller class
 *
 * @author gfoster
 */
public class FXMLConsumablesController extends GUIController implements Initializable {
    
    @FXML private Accordion leftSidePanel;
    @FXML private TitledPane wastePane;
    @FXML private TitledPane paperPane;
    @FXML private TitledPane yearBookPane;
    @FXML private TextArea details;

    @FXML private DatePicker paperStartDate;
    @FXML private DatePicker paperEndDate;
    @FXML private TextField  paperA4;
    @FXML private TextField  paperA3;
    @FXML private Button     btnPaperEndDate;
    @FXML private Button     btnUpdatePaper;

    @FXML private DatePicker yearbookDate;
    @FXML private TextField  yearbookPages;
    @FXML private TextField  yearbookCopies;
    @FXML private Button     btnUpdateYearbook;

    @FXML private DatePicker wasteStartDate;
    @FXML private DatePicker wasteEndDate;
    @FXML private TextField  wasteAmount;
    @FXML private TextField  wasteType;
    
    private ConsumablesDataCollector dc;
    
    @FXML private void updatePaper(ActionEvent event) {
        String start  = paperStartDate.getValue().toString();
        String end    = paperEndDate.getValue().toString();
        String a4     = paperA4.getText();
        if (a4.isEmpty()) { a4 = "0";}
        String a3     = paperA3.getText();
        if (a3.isEmpty()) { a3 = "0";}
        String result = dc.updatePaper(start,end,a4,a3);
        // Display the result on the screen, this also delays the processing
        // enough so that the electricity summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the paper details.");
        } else {
            ErrorMessage.display("Information", result, "Paper details updated");
            initializePaper();
        }
    } // end of method updatePaper()
    
    @FXML private void updateWaste(ActionEvent event) {
        String start  = wasteStartDate.getValue().toString();
        String end    = wasteEndDate.getValue().toString();
        String amount   = wasteAmount.getText();
        String type = wasteType.getText();
        dc.updateWaste(type,start,end,amount);
    } // end of method updateWaste()

    @FXML private void updateYearbook(ActionEvent event) {
        String start  = yearbookDate.getValue().toString();
        String pages   = yearbookPages.getText();
        String copies  = yearbookCopies.getText();
        String result = dc.updateYearbook(start,pages,copies);
        // Display the result on the screen, this also delays the processing
        // enough so that the electricity summary will be properly displayed.
        if (null == result){
            ErrorMessage.display("Unable to update the yearbook details.");
        } else {
            ErrorMessage.display("Information", result, "Yearbook details updated");
            initializeYearbook();
        }
    } // end of method updatePaper()

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = ConsumablesDataCollector.getInstance();
        leftSidePanel.setExpandedPane(paperPane);
        leftSidePanel.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            public void changed(ObservableValue<? extends TitledPane> ov, TitledPane oldValue, TitledPane newValue) {
                if (newValue == paperPane) {
                    details.setText(dc.paperSummary());
                } else if (newValue == wastePane) {
                    details.setText(dc.wasteSummary());
                } else if (newValue == yearBookPane) {
                    details.setText(dc.yearBookSummary());
                }
            } // end of accordion listener
        });

        intFilter(paperA3);
        intFilter(paperA4);
        initializePaper();

        intFilter(yearbookPages,true);
        intFilter(yearbookCopies,true);
        initializeYearbook();
        initializeWaste();

        details.setText(dc.paperSummary());
    } // end of method initialize()
    
    private void initializePaper() {
    	paperStartDate.setValue(dc.getLastDate("Paper"));
        attachEndDateAction(btnPaperEndDate, paperStartDate, paperEndDate);

        btnUpdatePaper.setDisable(true);
        paperA4.clear();
        paperA3.clear();
        
        paperA4.textProperty().addListener((observable, oldValue, newValue) -> {
        	btnUpdatePaper.setDisable(!validPaper());
        });
        paperA3.textProperty().addListener((observable, oldValue, newValue) -> {
        	btnUpdatePaper.setDisable(!validPaper());
        });
        details.setText(dc.paperSummary());
    } // end of method initializePaper()
    
    private boolean validPaper() {
    	if (paperA4.getText().isEmpty() 
    		&& paperA3.getText().isEmpty()
    		) return false;
    	return true;
    } // end of method validPaper

    private void initializeWaste() {
//        StartDate.setValue(dc.getPaperStartDate());
//        EndDate.setValue(LocalDate.now());
//        Amount.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    } // end of method initializeWaste()
    
    private void initializeYearbook() {
    	yearbookDate.setValue(LocalDate.now());
    	yearbookPages.clear();
        yearbookCopies.clear();
        yearbookPages.textProperty().addListener((observable, oldValue, newValue) -> {
        	btnUpdateYearbook.setDisable(!validYearbook());
        });
        yearbookCopies.textProperty().addListener((observable, oldValue, newValue) -> {
        	btnUpdateYearbook.setDisable(!validYearbook());
        });
        details.setText(dc.yearBookSummary());
    } // end of method initializeYearbook
    
    private boolean validYearbook() {
    	if (yearbookPages.getText().isEmpty() 
    		|| yearbookCopies.getText().isEmpty()
    		) return false;
    	return true;
    } // end of method validYearbook

} // end of class FXMLConsumablesController
