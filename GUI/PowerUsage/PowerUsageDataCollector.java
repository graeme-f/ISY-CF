package PowerUsage;

import utility.DataCollector;
import utility.ErrorMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PowerUsageDataCollector extends DataCollector {

    class Electricity {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int meterUnits;
    } // end of inner class Electricity

    private LocalDate lastDate;

    class Generator {
        int id;
        LocalDate startDate;
        LocalDate endDate;
        int amount;
    } // end of inner class Generator

    private static PowerUsageDataCollector singleInstance = null;
    private HashMap<Integer, Electricity> electricityDetails;

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
        lastDate = null;
    } // end of method PowerUsageDataCollector

    private void getAllElecrity(Connection conn) {
        electricityDetails = new HashMap<>();
        String query = "SELECT Start_Date, End_Date FROM Electricity WHERE StartDate " + getBetweenSchoolYear() + "FROM electricity";
        ResultSet rec = doQuery(query);
        Electricity electricity = new Electricity();
        try {
            while (rec.next()) {
                electricity.id = rec.getInt("Electricity_ID");
                electricity.startDate = rec.getDate("Start_Date").toLocalDate();
                electricity.endDate = rec.getDate("End_Date").toLocalDate();
                electricity.meterUnits = rec.getInt("Meter_Units");
                electricityDetails.put(electricity.id, electricity);
                if (lastDate == null) {
                    lastDate = electricity.endDate;
                } else if (electricity.endDate.compareTo(lastDate) > 0){
                    lastDate = electricity.endDate;
                }
            }
        } catch(SQLException error){
                ErrorMessage.display(error.getMessage());
        }
    }

    public ArrayList<Integer> getElectricityList(){
        ArrayList<Integer> electricity = new ArrayList<>();
        Set< HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> > st = electricityDetails.entrySet();

        for (HashMap.Entry< Integer, PowerUsageDataCollector.Electricity> me:st)
        {
            electricity.add(me.getKey());
        }
        return electricity;
    } // end method getElectricityList()

    public LocalDate getLastDate() {
        return lastDate.plusDays(1);
    } // end method getStartDate()

    public String insertElectricityData(String startDate, String endDate, String meterUnits) {
        return insertDatabase("INSERT into electricity (Start_Date, End_Date, Meter_Units) VALUES(\'" + startDate + "\', " +
                "\'" + endDate + "\', \'" + meterUnits + ")");
    }
}
