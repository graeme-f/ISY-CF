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


import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;

//Unsure whether the data type should be localdate or date, will require testing
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import utility.DataCollector;

/**
 *
 * @author mayankpandey
 */

public class ConsumablesDataCollector extends DataCollector {
    
    //Made this class static to be usable in the SQL
      class Paper {
        int id;
        int A3reams;
        int A4reams;
        LocalDate Start_Date;
        LocalDate End_Date;  
    }//end of paper class
  
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
    HashMap <Integer, ConsumablesDataCollector.Paper> paperDetails;
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
        getAllPaper();
    } // end of constructor
    
    private void getAllPaper() {
        paperDetails = new HashMap(); 
        // TODO vehicle list needs to come from the database
         // our SQL SELECT query. 
      String query = "SELECT * FROM Paper";

       ResultSet rs = doQuery(query);
      
      try {
      // iterate through the java resultset

      while (rs.next()) {

          //Creates an instance of the paper class, to be usable in this static method.

          Paper paper = new Paper();
        

        //Find the tables with the same name located in the literal string and add them to paper's properties
        paper.id = rs.getInt("Paper_ID");
        Date startDate  = rs.getDate("Start_Date");
        Date endDate = rs.getDate("End_Date");
        int A3reams = rs.getInt("A3");
        int A4reams = rs.getInt("A4");
        
        // Add that information into the hashmap

        paperDetails.put(paper.id, paper); //paper.id is the key to HashMap
        if (paper.End_Date == null || paper.End_Date.compareTo(lastDate) > 0){
            lastDate = paper.End_Date;
        }
      }//end of while loop
      
      
      
      } //end of try statement
   
       catch (Exception e)
    {
      System.err.println("Returned SQL exception e");
      System.err.println(e.getMessage());
    }//end of catch statement
        
    }//end of getAllPaper method  

    //gets the amount of A3 reams for a given order
    public int getA3Reams(LocalDate startDate) {
        return paperDetails.get(startDate).A3reams;
    } // end of method getA3Reams

    //gets the amount of A4 reams for a given order
    public int getA4Reams(LocalDate startDate) {
        return paperDetails.get(startDate).A4reams;
    } // end of method getA4Reams
    
    //gets the end date of a given order
    public LocalDate getEndDate(LocalDate startDate) {

        return paperDetails.get(startDate).End_Date;
    } // end of method getEndDate
   
    public String updatePaper(String startDate, String endDate, String A3reams, String A4reams){
        String paperid = "1"; //TO DO get information from wasteType
        String sql = " INSERT INTO " 
                + "paper (Start_Date, End_Date, A4_reams, A3_reams) "
                + "VALUES (,\""
                + startDate + "\", \""
                + endDate + "\", "
                + A4reams + ","
                + A3reams + ") ";
        return insertDatabase (sql);
    }
    
    private void getWaste(){
        paperDetails = new HashMap(); 
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
    } // end of method getWasteType
    
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