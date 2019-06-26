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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Unsure whether the data type should be localdate or date, will require testing
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import utility.DataCollector;
import utility.ErrorMessage;

/**
 *
 * @author mayankpandey
 */

public class ConsumablesDataCollector extends DataCollector {
    
    class Waste {
        int id;
        int amount;
        LocalDate Start_Date;
        LocalDate End_Date;  
    }
    class WasteType {
        int id;
        String description;
        int capacity;
        
    }
    private static ConsumablesDataCollector singleInstance = null;
    HashMap <Integer, ConsumablesDataCollector.Waste> wasteDetails;
    HashMap <Integer, WasteType> wasteTypeDetails;
    LocalDate lastDate = null;
    public static ConsumablesDataCollector getInstance() { 

        if (singleInstance == null) 
            singleInstance = new ConsumablesDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
       
    // Creator is private to make this a singleton class
    private ConsumablesDataCollector() {
        super();
    
    } // end of constructor
    

    public String paperSummary() {
    	String sql = "SELECT SUM(A4_Reams) as A4, "
    			   + "       SUM(A3_Reams) as A3, "
    			   + "       MONTH(Start_Date) as Month, " 
    			   + "       YEAR(Start_Date) as year "
    			   + " FROM Paper "
    			   + " WHERE Start_Date " + getBetweenSchoolYear()
    			   + " GROUP BY year, month"
    			   + " ORDER BY year, month";
	    ResultSet result = doQuery(sql);
	    String paperSummary = "";
	    int totalA4 = 0;
	    int totalA3 = 0;
	    if (null == result) return paperSummary;
	    try {
	    	paperSummary = "Month     \t    A4\t    A3\n";
	    	paperSummary += "=====     \t    ==\t    ==\n";
	        while (result.next()) {
	        	totalA4 += result.getInt("A4");
	        	totalA3 += result.getInt("A3");
	        	paperSummary += pad(getMonthName(result.getInt("Month")),10) + "\t";
	        	paperSummary += format(result.getInt("A4"),"##,###") + "\t";
	        	paperSummary += format(result.getInt("A3"),"##,###") + "\n";
	        }
	        paperSummary += "          \t======\t======\n";
	        paperSummary += "Total usage\t"+format(totalA4,"##,###") + "\t";
	        paperSummary += format(totalA3,"##,###") + "\n";
	        paperSummary += "          \t======\t======\n";
	    } catch (SQLException error) {
	            ErrorMessage.display(error.getMessage());
	    }
	    return paperSummary;
    } // end of method paperSummary

    public String wasteSummary() {
    	  return "Waste";
      }

    public String yearBookSummary() {
    	String sql = "SELECT SUM(Pages) as pages, "
 			   + "       SUM(Copies) as copies, "
			   + "       YEAR(Year) as year "
 			   + " FROM Year_Book "
 			   + " WHERE Year " + getBetweenSchoolYear()
 			   + " GROUP BY YEAR(Year)";
	    ResultSet result = doQuery(sql);
	    String yearbookSummary = "";
	    int totalA4 = 0;
	    if (null == result) return yearbookSummary;
	    try {
	    	yearbookSummary =  "Year\t Pages\t Copies\n";
	    	yearbookSummary += "====\t =====\t ======\n";
	        while (result.next()) {
	        	yearbookSummary += result.getInt("year") + "\t";
	        	yearbookSummary += format(result.getInt("pages"),"##,###") + "\t";
	        	yearbookSummary += format(result.getInt("copies"),"##,###") + "\n";
	        	totalA4 += result.getInt("pages") * result.getInt("copies");
	        }
	        totalA4 /= 500;
	        yearbookSummary += "\nTotal reams of A4: "+format(totalA4,"##,###") + "\n";
	    } catch (SQLException error) {
	            ErrorMessage.display(error.getMessage());
	    }
	    return yearbookSummary;
      }

    public String updatePaper(String startDate, String endDate, String A4reams, String A3reams){
        String sql = "INSERT INTO " 
                + "Paper (Start_Date, End_Date, A4_Reams, A3_Reams) "
                + "VALUES (\""
                + startDate + "\", \""
                + endDate + "\", "
                + A4reams + ","
                + A3reams + ") ";
        return insertDatabase (sql);
    }
    
    public String updateYearbook(String date, String pages, String copies) {
        String sql = "INSERT INTO " 
                + "Year_Book (Year, Pages, Copies) "
                + "VALUES (\""
                + date + "\", "
                + pages + ","
                + copies + ") ";
        return insertDatabase (sql);	
    } // end of method updateYearbook
    
    private void getWaste(){
    	wasteDetails = new HashMap(); 
        // TODO vehicle list needs to come from the database
         // our SQL SELECT query. 
      String query = "SELECT * FROM Waste";

       ResultSet rs = doQuery(query);

      
      try {
      // iterate through the java resultset
      while (rs.next())
      {

          //Creates an instance of the paper class, to be usable in this static method.
          Waste waste = new Waste();
        
         //Find the tables with the same name located in the literal string and add them to paper's properties
        waste.id = rs.getInt("Waste_ID"); 
        Date startDate  = rs.getDate("Start_Date");
        Date endDate = rs.getDate("date_created");
        int amount = rs.getInt("Amount");
        
        // Add that information into the hashmap
        wasteDetails.put(waste.id, waste); // waste.id is the key to HashMap
      }//end of while loop
     
      } 
   
       catch (Exception e)
    {
      System.err.println("Returned SQL exception e");
      System.err.println(e.getMessage());
    }//end of catch statement
    }
    
    private void getWasteType(){
        wasteTypeDetails = new HashMap(); 
        // TODO vehicle list needs to come from the database
         // our SQL SELECT query. 
      String query = "SELECT * FROM Waste_Type";

       ResultSet rs = doQuery(query);

      
      try {
      // iterate through the java resultset
      while (rs.next())
      {

          //Creates an instance of the paper class, to be usable in this static method.
          WasteType wasteType = new WasteType();
        
         //Find the tables with the same name located in the literal string and add them to paper's properties
        wasteType.id = rs.getInt("WasteType_ID"); 
        String description = rs.getString("Description");
        int capacity = rs.getInt("capacity");

        
        // Add that information into the hashmap
        wasteTypeDetails.put(wasteType.id, wasteType); 
      }//end of while loop
     
      } 
   
       catch (Exception e)
    {
      System.err.println("Returned SQL exception e");
      System.err.println(e.getMessage());
    }//end of catch statement
    }
    
    public ArrayList<String> getWasteTypeList(){
        ArrayList<String> wasteType = new ArrayList();
        Set <HashMap.Entry <Integer, WasteType>> st = wasteTypeDetails.entrySet();
        
        for (HashMap.Entry <Integer, WasteType> me:st){
            
            wasteType.add(me.getValue().description);
        }
        return wasteType;
    }
    public String updateWaste(String wasteType, String startDate, String endDate, String amount){
        String wasteTypeID = "1"; //TO DO get information from wasteType
        String sql = " INSERT INTO " 
                + "waste (Type, Start_Date, End_Date, Amount) "
                + "VALUES ("
                + wasteTypeID + ", \""
                + startDate + "\", \""
                + endDate + "\", "
                + amount + ") ";
        return insertDatabase (sql);
        
    }

    
        
    
    
        
    
    
}//end of ConsumablesDataCollector class

