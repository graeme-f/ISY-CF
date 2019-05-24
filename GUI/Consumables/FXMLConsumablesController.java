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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author gfoster
 */
public class FXMLConsumablesController implements Initializable {
    
    @FXML private DatePicker StartDate;
    @FXML private DatePicker EndDate;
    @FXML private TextField A4;
    @FXML private TextField A3;
    @FXML private TextField Amount;
    
    private ConsumablesDataCollector dc;
    
    @FXML private void updatePaper(ActionEvent event) {
        String start  = StartDate.getValue().toString();
        String end    = EndDate.getValue().toString();
        String a4   = A4.getText();
        String a3 = A3.getText();
//        dc.update(start,end,a4,a3);
    } // end of method updatePaper()
    
    @FXML private void updateWaste(ActionEvent event) {
        String start  = StartDate.getValue().toString();
        String end    = EndDate.getValue().toString();
        String amount   = Amount.getText();
//        dc.update(start,end,a4,a3);
    } // end of method updateWaste()
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = ConsumablesDataCollector.getInstance();
        initializePaper();
        initializeWaste();
    } // end of method initialize()
    
    private void initializePaper() {
//        StartDate.setValue(dc.getPaperStartDate());
//        EndDate.setValue(LocalDate.now());
        A4.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        A3.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    } // end of method initializePaper()
    
    private void initializeWaste() {
//        StartDate.setValue(dc.getPaperStartDate());
//        EndDate.setValue(LocalDate.now());
//        Amount.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    } // end of method initializeWaste()
    
    UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([1-9][0-9]*)?")) {
            return change;
        }
        return null;
    }; // end of intergerFilter
}
