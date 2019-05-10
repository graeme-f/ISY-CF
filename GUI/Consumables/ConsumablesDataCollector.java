package Consumables;

/*
 * The MIT License
 *
 * Copyright 2019 mayankpandey.
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

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mayankpandey
 */


public class ConsumablesDataCollector {
    class Paper {
        int id;
        int reams;
        LocalDate Start_Date;
        LocalDate End_Date;
          
    }//end of paper class
    
     private static ConsumablesDataCollector singleInstance = null;
    Connection conn = null;
    HashMap<LocalDate, ConsumablesDataCollector.Paper> paperDetails;
    
    public static ConsumablesDataCollector getInstance() 
    { 
        if (singleInstance == null) 
            singleInstance = new ConsumablesDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
    
       
    // Creator is private to make this a singleton class
    private ConsumablesDataCollector(){
        connect();
        getAllPaper();
    } // end of constructor
    
    @Override
    public void finalize() throws Throwable{
        close();
        super.finalize();
    }
    
    private void connect() {
        try(FileInputStream f = new FileInputStream("db.properties")) {
            // load the properties file
            Properties prop = new Properties();
            prop.load(f);

            // assign db parameters
            String url       = prop.getProperty("url");
            String user      = prop.getProperty("user");
            String password  = prop.getProperty("password");
            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database has been established.");
        } catch(SQLException | IOException e) {
           System.out.println(e.getMessage());
        }
    } // end connect method
    
    public void close(){
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }       
    } // end of close method
    
    private void getAllPaper(){
        paperDetails = new HashMap(); 
        // TODO vehicle list needs to come from the database
        Paper paperOrder = new Paper();
        paperOrder.id = 1;
        paperOrder.reams = 2021;
        paperOrder.Start_Date = LocalDate.of(2019, 02, 28);;
        paperOrder.End_Date =/*equals equals*/ LocalDate.of(2019, 03, 28);;
        
       
        
        paperDetails.put(paperOrder.Start_Date, paperOrder);
    }//end of getAllPaper method
    
    //gets the amount of reams for a given order
    public int getReams(LocalDate startDate){
        return paperDetails.get(startDate).reams;
    } // end of method getReams

    //gets the end date of a given order
    public LocalDate getEndDate(LocalDate startDate){
        return paperDetails.get(startDate).End_Date;
    } // end of method getEndDate
    
    
    
}//end of ConsumablesDataCollector class
