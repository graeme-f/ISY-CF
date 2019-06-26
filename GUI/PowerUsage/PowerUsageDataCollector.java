package PowerUsage;

import utility.DataCollector;
import utility.ErrorMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PowerUsageDataCollector extends DataCollector {

    class ACType {
        int id;
        String description;
        int number;
        int multiplier;
    }
    private HashMap<String, ACType> acTypeDetails;
        
    private static PowerUsageDataCollector singleInstance = null;
    // Singleton
    public static PowerUsageDataCollector getInstance()
    {
        if (singleInstance == null)
            singleInstance = new PowerUsageDataCollector();
        return singleInstance;
    } // end of method getInstance

    // This runs when the instance is created.
    private PowerUsageDataCollector() {
        super();
        acTypeDetails = new HashMap<>();
        getAllAcType();
    } // end of method PowerUsageDataCollector



    public String generatorSummary(){
        String sql = "SELECT SUM(Amount) as Total, "
        		+ "MONTH(Start_Date) as Month, "
        		+ "YEAR(Start_Date) as year "
        		+ "FROM Generator "
        		+ "WHERE Start_Date " + getBetweenSchoolYear()
        		+ "GROUP BY year, month";
        ResultSet result = doQuery(sql);
        String generatorSummary = "";
        int totalUsage = 0;
        if (null == result) return generatorSummary;
        try {
            while (result.next()) {
            	totalUsage += result.getInt("Total");
            	generatorSummary += pad(getMonthName(result.getInt("Month")),10) + "\t";
            	generatorSummary += format(result.getInt("Total"),"##,###") + "\n";
            }
            generatorSummary += "          \t======\n";
            generatorSummary += "Total usage\t"+format(totalUsage,"##,###") + "\n";
            generatorSummary += "          \t======\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return generatorSummary;
    } // end of method generatorSummary
    
    private void getAllAcType() {
        String query = "SELECT AC_Type_ID, Description FROM AC_Type";
        ACType acType;
        ResultSet rec = doQuery(query);
         try {
            while (rec.next()) {
                acType = new ACType();
                acType.id = rec.getInt("AC_TYPE_ID");
                acType.description = rec.getString("Description");
                acType.number = getACAmount(acType.id);
                acType.multiplier = getACMultiplier(acType.id);
                acTypeDetails.put(acType.description, acType);
            }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
    } // end method getAllAcType
    
    private int getACAmount(int id) {
    	String query = "SELECT Amount FROM ac_amount "
    			+ " WHERE AC_Type_ID = " + id
    			+ " ORDER BY Checked_Date DESC LIMIT 1";
        ResultSet rec = doQuery(query);
        try {
           while (rec.next()) {
               return rec.getInt("Amount");
           }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
        return 0;
    } // end of method getACAmount
      
    private int getACMultiplier(int id) {
    	String query = "SELECT Multiplier FROM ac_co2 "
    			+ " WHERE AC_Type_ID = " + id
    			+ " ORDER BY Start_Date DESC LIMIT 1";
        ResultSet rec = doQuery(query);
        try {
           while (rec.next()) {
               return rec.getInt("Multiplier");
           }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
        return 200;
    } // end of method getACMultiplier
    
    public ArrayList<String> getAcTypeList(){
        ArrayList<String> acType = new ArrayList<>();
        Set< HashMap.Entry< String, ACType> > st = acTypeDetails.entrySet();

        for (HashMap.Entry< String, ACType> me:st)
        {
            acType.add(me.getKey());
        }
        Collections.sort(acType);
        return acType;
    } // end of method getAcTypeList

    public int getAC_ID(String name) {
    	return acTypeDetails.get(name).id;
    } // end of method getACAmount
    
    public int getACAmount(String name) {
    	return acTypeDetails.get(name).number;
    } // end of method getACAmount
    
    public int getACMultiplier(String name) {
    	return acTypeDetails.get(name).multiplier;
    } // end of method getACMultiplier
    
    public String acSummary(){
//        String sql = "SELECT SUM(Amount) as Total, "
//        		+ "MONTH(Start_Date) as Month, "
//        		+ "YEAR(Start_Date) as year "
//        		+ "FROM Generator "
//        		+ "WHERE Start_Date " + getBetweenSchoolYear()
//        		+ "GROUP BY year, month";
//        ResultSet result = doQuery(sql);
        String acSummary = "";
//        int totalUsage = 0;
//        if (null == result) return acSummary;
//        try {
//            while (result.next()) {
//            	totalUsage += result.getInt("Total");
//            	acSummary += pad(getMonthName(result.getInt("Month")),10) + "\t";
//            	acSummary += result.getString("Total") + "\n";
//            }
//            acSummary += "Total usage\t"+totalUsage + "\n";
//        } catch (SQLException error) {
//                ErrorMessage.display(error.getMessage());
//        }
        return acSummary;
        
    } // end of method acSummary
    public String electricitySummary(){
        String sql = "SELECT SUM(Meter_Units) as Total, "
        		+ "MONTH(Start_Date) as Month, "
        		+ "YEAR(Start_Date) as year "
        		+ "FROM Electricity "
        		+ "WHERE Start_Date " + getBetweenSchoolYear()
        		+ "GROUP BY year, month";
        ResultSet result = doQuery(sql);
        String electricitySummary = "";
        int totalUsage = 0;
        if (null == result) return electricitySummary;
        try {
            while (result.next()) {
            	totalUsage += result.getInt("Total");
            	electricitySummary += pad(getMonthName(result.getInt("Month")),10) + "\t";
            	electricitySummary += format(result.getInt("Total"),"##,###,###") + "\n";
            }
            electricitySummary += "          \t==========\n";
            electricitySummary += "Total usage\t"+format(totalUsage,"##,###,###") + "\n";
            electricitySummary += "          \t==========\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return electricitySummary;
    } // end of method electricitySummary

    public String insertElectricityData(String startDate, String endDate, String meterUnits) {
        return insertDatabase("INSERT into Electricity (Start_Date, End_Date, Meter_Units) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', " + meterUnits + ")");
    } // end of method insertElectricityData

    public String insertGeneratorData(String startDate, String endDate, String amount) {
        return insertDatabase("INSERT into Generator (Start_Date, End_Date, Amount) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', " + amount + ")");
    } // end of method insertGeneratorData

    public String updateACAmount(int id, int number) {
        return insertDatabase("INSERT into AC_Amount (AC_Type_ID, Amount, Checked_Date) VALUES(" 
        					 + id + ", " + number 
        					 + ", \'" + LocalDate.now() + "\')");    	
    } // end of method updateACAmount
    
    public String updateACMultiplier(int id, int number) {
        return insertDatabase("INSERT into AC_CO2 (Start_Date, Multiplier, AC_Type_ID) VALUES(\'" 
        					 + LocalDate.now() + "\', " + number 
        					 + ", " + id + ")");    	
    } // end of method updateACAmount
    

    public String insertAcTypeData(String startDate, String endDate, String description, int number) {
        return insertDatabase("INSERT into AC_Type (Start_Date, End_Date, Description, Number) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + description + "\', " + number + ")");
    } // end of method insertAcTypeData
}
