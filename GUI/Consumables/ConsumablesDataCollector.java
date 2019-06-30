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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

//Unsure whether the data type should be localdate or date, will require testing
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import utility.DataCollector;
import utility.ErrorMessage;

/**
 *
 * @author mayankpandey
 */

public class ConsumablesDataCollector extends DataCollector {
    
    class Waste {
        int amount;
        int count;
    }
    class WasteType {
        int id;
        String description;
        int count;
        int capacity;
    }
    class WasteSummary {
    	 HashMap <String, Waste> details;
    }

    private static ConsumablesDataCollector singleInstance = null;
    HashMap <Integer, ConsumablesDataCollector.Waste> wasteDetails;
    HashMap <String, WasteType> wasteTypeDetails;
    TreeMap <Integer, WasteSummary> wasteSummaryDetails;
    LocalDate lastDate = null;
    public static ConsumablesDataCollector getInstance() { 

        if (singleInstance == null) 
            singleInstance = new ConsumablesDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
       
    // Creator is private to make this a singleton class
    private ConsumablesDataCollector() {
        super();
	    getWasteType();
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
    	String sql = "SELECT Description, "
    			+ "      SUM(Amount) as amount, "
    			+ "      MAX(Bin_Count) as count, "
 			   + "       MONTH(Start_Date) as Month, " 
 			   + "       YEAR(Start_Date) as Year "
 			   + " FROM Waste "
 			   + "    	inner join Waste_Type using (Waste_Type_ID)"
 			   + " WHERE Start_Date " + getBetweenSchoolYear()
 			   + " GROUP BY Description, Year, Month"
 			   + " ORDER BY Year, Month";
	    ResultSet result = doQuery(sql);
	    String wasteSummary = "";
	    if (null == result) return wasteSummary;
	    ArrayList<String> wtype = getWasteTypeList();
	    wasteSummaryDetails = new TreeMap<>();
	    try {
	    	while (result.next()) {
	    		int month = result.getInt("Month");
	    		int year = result.getInt("Year");
	    		int date = year * 100 + month;
	    		String desc  = result.getString("Description");
	    		int amount = result.getInt("amount");
	    		int count = result.getInt("count");
	    		if (wasteSummaryDetails.containsKey(date)) {
		    		// Case when the waste object exists (for this date) so needs to be updated
	    			Waste waste = wasteSummaryDetails.get(date).details.get(desc);
	    			waste.amount += amount;
	    			waste.count  = count;
	    			//wasteSummaryDetails.get(month).amount.replace(desc, waste);
	    		} else {
	    			// Case when the waste object doesn't exist so need to be created
	    			WasteSummary ws = new WasteSummary();
	    			ws.details = new HashMap<>();
	    			for (String type : wtype) {
		    			Waste waste = new Waste();
	    				if (type.equals(desc)) {
	    	    			waste.amount = amount;
	    	    			waste.count = count;
	    				}
    					ws.details.put(type,waste);
	    			}
	    			wasteSummaryDetails.put(date, ws);
	    		}
	    	}
	    } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
            return wasteSummary;
	    }
	    HashMap <String, Integer> wasteTotals = new HashMap<>();
    	wasteSummary =  "Month      ";
    	for (String type : wtype) {
    		wasteSummary +=  pad(type,5) + " Emptied ";
    	}
    	wasteSummary += "\n=====      ===== ======= ===== =======\n";
        for (int date  :  wasteSummaryDetails.keySet()) {
        	int month = date % 100;
        	wasteSummary += pad(getMonthName(month),10) + " ";
        	for (String type : wtype) {
        		int count = wasteSummaryDetails.get(date).details.get(type).count;
        		int amount = wasteSummaryDetails.get(date).details.get(type).amount;
        		if (wasteTotals.containsKey(type)) {
        			int total = wasteTotals.get(type);
        			total += count * amount;
        			wasteTotals.replace(type, total);
        		} else {
        			wasteTotals.put(type, count * amount);
        		}
        		wasteSummary += format(count,"#,###") + " ";
        		wasteSummary += format(amount,"#,###") + "   ";
        	}
        	wasteSummary += "\n";
        }
    	wasteSummary += "\nTotal        ";
    	for (String type : wtype) {
        	wasteSummary += format(wasteTotals.get(type),"#,###") + "         ";
    	}
	    return wasteSummary;
      } // end of method wasteSummary

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
    } // end of method updatePaper
    

    public String updateWaste(String startDate, String endDate, 
    						String largeTotal, String largeEmptied,
    						String smallTotal, String smallEmptied){
    	ArrayList<String> type = getWasteTypeList();
        int smallID = getWasteTypeID(type.get(0));
        int largeID = getWasteTypeID(type.get(1));
        String sql = "INSERT INTO " 
                + "Waste (Waste_Type_ID, Start_Date, End_Date, Bin_Count, Amount) "
                + "VALUES ("
                + largeID + ", \""
                + startDate + "\", \""
                + endDate + "\", "
                + largeTotal + ", "
                + largeEmptied + "),  ("
                + smallID + ", \"" 
                + startDate + "\", \"" 
                + endDate + "\", "
                + smallTotal + ", "
                + smallEmptied + ")";
        return insertDatabase (sql);
    } // end of method updateWaste

    public String updateYearbook(String date, String pages, String copies) {
        String sql = "INSERT INTO " 
                + "Year_Book (Year, Pages, Copies) "
                + "VALUES (\""
                + date + "\", "
                + pages + ","
                + copies + ") ";
        return insertDatabase (sql);	
    } // end of method updateYearbook
    
    
    private void getWasteType(){
        wasteTypeDetails = new HashMap<>();
        String query = "SELECT * FROM Waste_Type";

        ResultSet rs = doQuery(query);
        try {
            // iterate through the java resultset
            while (rs.next())
            {

                //Creates an instance of the paper class, to be usable in this static method.
                WasteType wasteType = new WasteType();

               //Find the tables with the same name located in the literal string and add them to paper's properties
              wasteType.id = rs.getInt("Waste_Type_ID"); 
              wasteType.description = rs.getString("Description");
              wasteType.count = rs.getInt("Total");
              wasteType.capacity = rs.getInt("Capacity");

              // Add that information into the hashmap
              wasteTypeDetails.put(wasteType.description, wasteType); 
            }//end of while loop
        }
        catch (Exception e)
        {
          System.err.println("Returned SQL exception e");
          System.err.println(e.getMessage());
        }//end of catch statement
    } // end of method getWasteType
    
    public ArrayList<String> getWasteTypeList(){
        ArrayList<String> wasteType = new ArrayList<>();
        Set <HashMap.Entry <String, WasteType>> st = wasteTypeDetails.entrySet();
        
        for (HashMap.Entry <String, WasteType> me:st){
            wasteType.add(me.getValue().description);
        }
        return wasteType;
    } // end of method getWasteTypeList
    
    public int getWasteTypeID(String type) {
    	return wasteTypeDetails.get(type).id;
    } // end of method getWasteTypeID

    public int getWasteTypeCount(String type) {
    	return wasteTypeDetails.get(type).count;
    } // end of method getWasteTypeID
    
}//end of ConsumablesDataCollector class