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
        
    private static PowerUsageDataCollector singleInstance = null;
    private HashMap<String, ACType> acTypeDetails;
    
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
            	generatorSummary += result.getString("Total") + "\n";
            }
            generatorSummary += "Total usage\t"+totalUsage + "\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return generatorSummary;
    } // end of method generatorSummary
    
    private void getAllAcType() {
        String query = "SELECT AC_Type_ID, Description, Number FROM AC_Type INNER JOIN"
                + "( SELECT AC_Type_ID, MAX(Checked_Date) FROM AC_Type"
                + "  GROUP BY AC_Type_ID) alias "
                + "USING (AC_Type_ID)";
        ACType acType;
        ResultSet rec = doQuery(query);
         try {
            while (rec.next()) {
                acType = new ACType();
                acType.id = rec.getInt("AC_TYPE_ID");
                acType.description = rec.getString("Description");
                acType.number = rec.getInt("Number");
                acTypeDetails.put(acType.description, acType);
            }
        } catch (SQLException error) {
            ErrorMessage.display(error.getMessage());
        }
    } // end method getAllAcType
    
    public ArrayList<String> getAcTypeList(){
        ArrayList<String> acType = new ArrayList<>();
        Set< HashMap.Entry< String, ACType> > st = acTypeDetails.entrySet();

        for (HashMap.Entry< String, ACType> me:st)
        {
            acType.add(me.getKey());
        }
        return acType;
    } // end of method getAcTypeList
    
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
            	electricitySummary += result.getString("Total") + "\n";
            }
            electricitySummary += "Total usage\t"+totalUsage + "\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return electricitySummary;
    } // end of method electricitySummary

    public String getMonth(LocalDate date) {
        return date.getMonth().toString();
    } // end of method getMonth

    public LocalDate getLastDate(String table) {
        String query = "SELECT MAX(End_Date) AS last_Date FROM "
                        + table 
                        + " WHERE Start_Date " 
                        + getBetweenSchoolYear();
        ResultSet rec = doQuery(query);
        try {
            while (rec.next()) {        
                return rec.getDate("last_Date").toLocalDate().plusDays(1);
            }
        } catch(SQLException error){
            ErrorMessage.display(error.getMessage());
        }
        return null;
    } // end of method getLastDate


    public String insertElectricityData(String startDate, String endDate, String meterUnits) {
        return insertDatabase("INSERT into Electricity (Start_Date, End_Date, Meter_Units) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', " + meterUnits + ")");
    } // end of method insertElectricityData

    public String insertGeneratorData(String startDate, String endDate, String amount) {
        return insertDatabase("INSERT into Generator (Start_Date, End_Date, Amount) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', " + amount + ")");
    } // end of method insertGeneratorData

    public String insertAcTypeData(String startDate, String endDate, String description, int number) {
        return insertDatabase("INSERT into AC_Type (Start_Date, End_Date, Description, Number) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + description + "\', " + number + "')");
    } // end of method insertAcTypeData
}
