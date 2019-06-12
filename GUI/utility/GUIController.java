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

import java.util.function.UnaryOperator;

import javafx.scene.control.ComboBoxBase;
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
        f.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));	
    } // end of ontFilter
    
    protected void intFilter(TextField f, boolean required) {
        f.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, integerFilter));
        if (required) {
        	setRequired(f);
        }
    } // end of ontFilter

    UnaryOperator<Change> integerFilter = change -> {
	    String newText = change.getControlNewText();
	    if (newText.matches("-?([1-9][0-9]*)?")) { 
	        return change;
	    }
	    return null;
	};

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

	}
} // end of class GUIController
