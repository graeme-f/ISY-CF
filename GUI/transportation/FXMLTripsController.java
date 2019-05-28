/*
 * The MIT License
 *
 * Copyright 2019 shashankpandey.
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
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transportation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utility.ErrorMessage;

/**
 *
 * @author shashankpandey
 */
public class FXMLTripsController implements Initializable {
    @FXML private TextArea details;
    
    @FXML private DatePicker SEASACstartDate;
    @FXML private DatePicker SEASACendDate;
    @FXML private DatePicker YACstartDate;
    @FXML private DatePicker YACendDate;
    @FXML private DatePicker WWWstartDate;
    @FXML private DatePicker WWWendDate;
    @FXML private DatePicker FieldTripsstartDate;
    @FXML private DatePicker FieldTripsendDate;

    @FXML private TextField SEASACBusesNumber;
    @FXML private TextField SEASACBusDistance;
    @FXML private TextField SEASACAirDistance;
    @FXML private TextField SEASACPlayerNumber;
    @FXML private TextField SEASACTeacherNumber;

    @FXML private TextField YACBusesNumber;
    @FXML private TextField YACBusDistance;

    @FXML private TextField WWWBusesNumber;
    @FXML private TextField WWWBusDistance;
    @FXML private TextField WWWAirDistance;
    @FXML private TextField WWWPlayerNumber;
    @FXML private TextField WWWTeacherNumber;

    @FXML private TextField FieldTripsBusesNumber;
    @FXML private TextField FieldTripsBusDistance;
      
    private TransportationDataCollector dc;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = TransportationDataCollector.getInstance();
               
    } // end of method initialize    
    
    @FXML private void AddVehicle(ActionEvent event) {
        System.out.println("Add Trip");
        event.consume();

        Scene newScene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLNewTrip.fxml"));//not sure about this piece
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            System.out.println("Fail");
            System.out.println(e);
            ErrorMessage.display("Unable to open window", e.getMessage());
        }
    }
}