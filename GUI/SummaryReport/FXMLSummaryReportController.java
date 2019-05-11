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

package SummaryReport;

import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author gfoster
 */
public class FXMLSummaryReportController  implements Initializable {
    @FXML private StackPane Transport;
    @FXML private StackPane Trip;
    @FXML private StackPane Electricity;
    @FXML private StackPane AC;
    @FXML private StackPane Waste;
    @FXML private StackPane Meat;
    @FXML private StackPane Paper;
    @FXML private StackPane YearBook;
    @FXML private StackPane GTotal;
    
    
    @FXML private ImageView TransportTotal;
    @FXML private ImageView TripTotal;
    @FXML private ImageView ElectricityTotal;
    @FXML private ImageView ACTotal;
    @FXML private ImageView WasteTotal;
    @FXML private ImageView MeatTotal;
    @FXML private ImageView PaperTotal;
    @FXML private ImageView YearBookTotal;
    @FXML private ImageView GrandTotal;
    
    @FXML private Label TransportCF;
    @FXML private Label TripCF;
    @FXML private Label ElectricityCF;
    @FXML private Label ACCF;
    @FXML private Label WasteCF;
    @FXML private Label MeatCF;
    @FXML private Label PaperCF;
    @FXML private Label YearBookCF;
    @FXML private Label GTotalCF;
    
    @FXML private TextFlow detailPane;

    @FXML private void ShowDetails(Event event) {
        event.consume();

        String controlName;
        ImageView iv = (ImageView)event.getSource();
        controlName = iv.getId(); // this will return the name of the imageView
        int x = 200;
        int y = 0;
        String title = "";
        String details = "";
        String finalSummary = "";
        switch (controlName) {
            case "TransportTotal":
                x = 200;
                y = 0;
                title = "Transport Total";
                details = transportDetails();
                finalSummary = transportFinal();
                break;
            case "WasteTotal":
                x = 200;
                y = 50;
                title = "Waste Total";
                details = wasteDetails();
                finalSummary = wasteFinal();
                break;
            case "MeatTotal":            
                x = 200;
                y = 100;
                title = "Meat Total";
                details = meatDetails();
                finalSummary = meatFinal();
                break;
            case "TripTotal":            
                x = 100;
                y = 100;
                title = "Trip Total";
                details = tripDetails();
                finalSummary = tripFinal();
                break;
            case "PaperTotal":            
                x = 100;
                y = 0;
                title = "Paper Total";
                details = paperDetails();
                finalSummary = paperFinal();
                break;
            case "ElectricityTotal":            
                x = 0;
                y = 0;
                title = "Electricity Total";
                details = electricityDetails();
                finalSummary = electricityFinal();
                break;
            case "ACTotal":            
                x = 0;
                y = 50;
                title = "Air Conditioners Total";
                details = acDetails();
                finalSummary = acFinal();
                break;
            case "YearBookTotal":            
                x = 0;
                y = 100;
                title = "YearBook Total";
                details = yearbookDetails();
                finalSummary = yearbookFinal();
                break;
            default:
                break;
        }
 
        Text text1 = new Text(title + "\n");
        text1.setFill(Color.RED);
        text1.setFont(Font.font("Helvetica", FontPosture.ITALIC, 20));
        Text text2 = new Text(details);
        text2.setFill(Color.BLUE);
        text2.setFont(Font.font("Helvetica", FontWeight.NORMAL, 11));
        Text text3 = new Text(finalSummary);
        text3.setFill(Color.BLUE);
        text3.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        detailPane.getChildren().addAll(text1, text2, text3);
        detailPane.setLayoutX(x);
        detailPane.setLayoutY(y);
        detailPane.setVisible(true);
        String msg = "pref height: " + Double.toString(detailPane.getMinHeight());
        Logger.getLogger("ShowDetails").log(Level.WARNING, msg);
    }

    @FXML private void HideDetails(Event event) {
        event.consume();
        detailPane.getChildren().clear();
        detailPane.setVisible(false);
    }

    private int CFTransport;
    private int CFTrip;
    private int CFElectricity;
    private int CFAC;
    private int CFWaste;
    private int CFMeat;
    private int CFPaper;
    private int CFYearBook;
    private int CFGTotal;
    private Image footprint;
    private Image footprintCentre;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String file = "data/Footprint.png";
        footprint = loadImage(file);
        file = "data/Footprint-recycle.jpg";
        footprintCentre = loadImage(file);
        if (footprintCentre != null){
            GrandTotal.setImage(footprintCentre);            
        }
        getSummaryData();
        displayTotals();
        detailPane.setVisible(false);
    } // end of method initialize 

    private void getSummaryData(){
        // These summary totals need to be calculated from details held on the database
        CFTransport = 16;
        CFTrip = 368;
        CFElectricity = 277;
        CFAC = 26;
        CFWaste = 227;
        CFMeat = 85;
        CFPaper = 8;
        CFYearBook = 5;
        CFGTotal =  CFTransport +
                    CFTrip +
                    CFElectricity +
                    CFAC +
                    CFWaste +
                    CFMeat +
                    CFPaper +
                    CFYearBook;
    }
    
    private String transportDetails(){
        return "The summary details need to be obtained from the database\n"
                + "Month\t\tDiesel\tGas 95\n"
                + "July\t\t???\t???\n"
                + "August\t\t???\t???\n"
                + "September\t???\t???\n"
                + "October\t\t???\t???\n"
                + "November\t???\t???\n"
                + "December\t???\t???\n"
                + "January\t\t???\t???\n"
                + "February\t\t???\t???\n"
                + "March\t\t???\t???\n"
                + "April\t\t???\t???\n"
                + "May\t\t???\t???\n"
                + "June\t\t???\t???\n"
                + "Total\t\t???\t???\n";
    }
    
    private String transportFinal(){
        return "The total Carbon Dioxide emmisions for the 2018-19 school year due to the school vehicles is 16 tonnes.";
    }
    
    private String wasteDetails(){
        return "TODO";
    }
    private String wasteFinal(){
        return "TODO";
    }
    
    private String meatDetails(){
        return "TODO";
    }
    
    private String meatFinal(){
        return "TODO";
    }
    
    private String tripDetails(){
        return "The summary details need to be obtained from the database\n"
                + "Month\t\tStudent\tStaff\n"
                + "July\t\t???\t???\n"
                + "August\t\t???\t???\n"
                + "September\t???\t???\n"
                + "October\t\t???\t???\n"
                + "November\t???\t???\n"
                + "December\t???\t???\n"
                + "January\t\t???\t???\n"
                + "February\t\t???\t???\n"
                + "March\t\t???\t???\n"
                + "April\t\t???\t???\n"
                + "May\t\t???\t???\n"
                + "June\t\t???\t???\n"
                + "Total\t\t???\t???\n";
    }
    
    private String tripFinal(){
        return "TODO";
    }
    
    private String paperDetails(){
        return "TODO";
    }
    
    private String paperFinal(){
        return "TODO";
    }
    
    private String electricityDetails(){
        return "The summary details need to be obtained from the database\n"
                + "Month\t\tKWatts\tDiesel\n"
                + "July\t\t???\t???\n"
                + "August\t\t???\t???\n"
                + "September\t???\t???\n"
                + "October\t\t???\t???\n"
                + "November\t???\t???\n"
                + "December\t???\t???\n"
                + "January\t\t???\t???\n"
                + "February\t\t???\t???\n"
                + "March\t\t???\t???\n"
                + "April\t\t???\t???\n"
                + "May\t\t???\t???\n"
                + "June\t\t???\t???\n";
    }
    
    private String electricityFinal(){
        return "Total\t\t???\t???\n\n"
                + "The total Carbon Dioxide emmisions for the 2018-19 school year due to the school vehicles is 16 tonnes.";
    }
    
    private String acDetails(){
        return "TODO";
    }
    
    private String acFinal(){
        return "TODO";
    }
    
    private String yearbookDetails(){
        return "TODO";
    }
    
    private String yearbookFinal(){
        return "TODO";
    }
                        
    private void displayTotals(){
        displayImage(TransportTotal, TransportCF, CFTransport);
        displayImage(TripTotal, TripCF, CFTrip);
        displayImage(ElectricityTotal, ElectricityCF, CFElectricity);
        displayImage(ACTotal, ACCF, CFAC);
        displayImage(WasteTotal, WasteCF, CFWaste);
        displayImage(MeatTotal, MeatCF, CFMeat);
        displayImage(PaperTotal, PaperCF, CFPaper);
        displayImage(YearBookTotal, YearBookCF, CFYearBook);
        GTotalCF.setText(Integer.toString(CFGTotal));
    }
    
    private void displayImage(ImageView view, Label lbl, int total){
        if (footprint != null){
            view.setImage(footprint);
            view.setRotate(45);
            view.setTranslateX(10);
            lbl.setText(Integer.toString(total));
        }
    }
    
    private Image loadImage(String fileName){
        FileInputStream input;
        try {
            // load the image
            input = new FileInputStream(fileName);
        } catch (FileNotFoundException ex) {
            String workingDir = "Cannot find file: " + System.getProperty("user.dir")+ "/"+fileName;
            Logger.getLogger("JavaFXApp01Image").log(Level.WARNING, workingDir);
            return null;
        }
        return new Image(input);
    } // end of method loadImage
        
    
} // end of class FXMLSummaryReportController
