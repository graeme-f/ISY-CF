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

package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author gfoster
 */
public class GUIController {
    protected final LogFile logger = ErrorMessage.logger;
    protected Properties prop = null;
    protected boolean defaultToToday = true;
    
    public GUIController(){
        try(FileInputStream f = new FileInputStream("db.properties")) {
            // load the properties file
            prop = new Properties();
            prop.load(f);
            String endDateDefault = prop.getProperty("endDateDefault");
            if (null != endDateDefault)
                defaultToToday = endDateDefault.equals("Today");
        } catch(IOException e) {
           String workingDir = "Current working directory: " + System.getProperty("user.dir");
           logger.log(Level.SEVERE, workingDir);
           logger.log(Level.SEVERE, e.getMessage());
        }
    }    
    
    protected void setBorder(ComboBoxBase c, String newValue){
        if (newValue.isEmpty()){
            Border red = new Border(new BorderStroke(Color.RED, 
                                        BorderStrokeStyle.SOLID,
                                        CornerRadii.EMPTY,
                                        BorderWidths.DEFAULT
                                    )); 
            c.setBorder(red);
        } else {
            c.setBorder(Border.EMPTY);
        }
    } // end of method setBorder

    protected void setBorder(TextInputControl c, String newValue){
        if (newValue.isEmpty()){
            Border red = new Border(new BorderStroke(Color.RED, 
                                        BorderStrokeStyle.SOLID,
                                        CornerRadii.EMPTY,
                                        BorderWidths.DEFAULT
                                    )); 
            c.setBorder(red);
        } else {
            c.setBorder(Border.EMPTY);
        }
    } // end of method setBorder
    
    protected void intFilter(TextField f) {
        // Add the integer filter to the TextFiled so that only whole numbers are accepted
        f.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));	
    } // end of ontFilter
    
    protected void intFilter(TextField f, boolean required) {
        // Add the integer filter to the TextFiled so that only whole numbers are accepted
        f.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        if (required) {
        	setRequired(f);
        }
    } // end of intFilter

    protected void intFilter(TextField f, Button btn) {
        // Add the integer filter to the TextFiled so that only whole numbers are accepted
        f.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        // Make the textfild a required filed and disable the button if the textField is empty
        setRequired(f, btn);
    } // end of intFilter

    UnaryOperator<Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("-?([1-9][0-9]*)?")) { 
            return change;
        }
        return null;
    };

    protected void setRequired(TextField f, Button btn) {
        f.textProperty().addListener((observable, oldvalue, newvalue)
        ->{
            if(newvalue.isEmpty() || newvalue.equals("-"))
            {
                setBorder(f, "");
                btn.setDisable(true);
            } else {
                setBorder(f, newvalue);
                btn.setDisable(false);
            }
        });
    } // end of method setRequired
	
    protected void setRequired(TextField f) {
        f.textProperty().addListener((observable, oldvalue, newvalue)
        ->{
            if(newvalue.isEmpty() || newvalue.equals("-"))
            {
                setBorder(f, "");
            } else {
                setBorder(f, newvalue);
            }
        });
    } // end of method setRequired

    protected String getNumber(TextField f) {
            String result = f.getText();
            if (result.isEmpty()) {
                    return "0";
            } else {
                    return result;
            }
    } // end of method getNumber
    
    protected void attachEndDateAction(Button btn, DatePicker source, DatePicker destination){
        if (defaultToToday){
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    destination.setValue(source.getValue().plusMonths(1).minusDays(1));
                }
            });
        } else {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    destination.setValue(LocalDate.now());
                }
            });
        }
    } // end of method attachEndDateAction
} // end of class GUIController
