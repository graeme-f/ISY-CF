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

package server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import utility.DatabaseConnector;

/**
 *
 * @author gfoster
 */
public class FXMLServerController {

    @FXML private TextArea details;
    private DatabaseConnector dc;
    
    @FXML private void onCheckDatabase(Event event) {
        dc = DatabaseConnector.getInstance();
        String error = dc.connect();
        if (error.isEmpty()){
            details.setText("Connection to the database has been established.");
        }else {
            details.setText(error);
        }
    }

    @FXML private void onGeneratePassword(Event event) {
        
    }

    @FXML private void onClearPassword(Event event) {
        
    }

    @FXML private void onCheckTokenFile(Event event) {
        
    }

    @FXML private void onCheckLogFile(Event event) {
        
    }

    @FXML private void onCheckConnections(Event event) {
        
    }

    @FXML private void onSummaryReport(Event event) {
        
    }

    public void initialize(URL url, ResourceBundle rb) {
        
    }
} // end of class FXMLServerController
