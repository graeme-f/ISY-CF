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
package transportation;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import utility.DataCollector;
import utility.ErrorMessage;

public class TransportationDataCollector extends DataCollector {
    class Car {
        int id;
        String name;
        String fuel;
        LocalDate lastRecordedDate;    
    } // end of inner class Car
    
    class TripGroup {
        int id;
        String description;
        boolean teacher_only;    
    } // end of inner class TripGroup

    private static TransportationDataCollector singleInstance = null;
    HashMap<String, Car> carDetails;
    ArrayList<String> cars;
    HashMap<String, Integer> fuelList;
    HashMap<String, Integer> vehicleList;
    HashMap<String, TripGroup> groupDetails;
    ArrayList<String> trips;
    
    public static TransportationDataCollector getInstance() 
    { 
        if (singleInstance == null) 
            singleInstance = new TransportationDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
    
    // Creator is private to make this a singleton class
    private TransportationDataCollector(){
        super();
        cars = null;
        fuelList = new HashMap<String, Integer>();
        vehicleList = new HashMap<String, Integer>();
        carDetails = new HashMap<String, Car>();
        groupDetails = new HashMap<String, TripGroup>();

        if (conn != null){
            getFuelTypes();
            getVehicleTypes();
            getAllCars();
            getAllTripGroups();
        }
    } // end of constructor
    
    private void getFuelTypes(){
        String sql = "SELECT * FROM Fuel_Type";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                int fuelID = result.getInt("Fuel_Type_ID");
                String desc = result.getString("description");
                fuelList.put (desc, fuelID);}
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }

    }

    private void getVehicleTypes(){
        String sql = "SELECT * FROM Vehicle_Type";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                int vehicleID = result.getInt("Vehicle_Type_ID");
                String desc = result.getString("Description");
                vehicleList.put (desc, vehicleID);}
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }

    }

    private void getAllCars(){
        Car car;
        String sql = "SELECT Vehicle_ID, Vehicle.Description, Fuel_Type.Description, MAX(Fuel.End_Date) as lastDate "
                + "FROM Vehicle INNER JOIN Fuel_Type USING(Fuel_Type_ID) "
                + "LEFT JOIN Fuel USING(Vehicle_ID) "
                + "WHERE Decommissioned = 0 "
                + "GROUP BY Vehicle_ID";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                car = new Car();
                car.id = result.getInt("Vehicle_ID");
                car.name = result.getString("Vehicle.Description");
                car.fuel = result.getString("Fuel_Type.Description");
                String lastDate = result.getString("lastDate");
                if (lastDate != null) {
                	car.lastRecordedDate = result.getDate("lastDate").toLocalDate();
                } else {
                	car.lastRecordedDate = LocalDate.now().minusMonths(1);
                }
                carDetails.put(car.name, car);
            }
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        
    } // end method getAllCars

    public ArrayList<String> getCarList(){
    	if (cars == null) {
	        cars = new ArrayList<String>();
	        Set< HashMap.Entry< String, Car> > st = carDetails.entrySet();
	        for (HashMap.Entry< String, Car> me:st) 
	        {
	            cars.add(me.getKey());
	        }
    	}
         return cars;
    } // end method getCarList()
    
    public ArrayList<String> getFuelList(){
        ArrayList<String> fuels = new ArrayList<String>();
        Set< HashMap.Entry<String, Integer> > st = fuelList.entrySet();
        for (HashMap.Entry<String, Integer> me:st) 
        {
            fuels.add(me.getKey());
        }
        return fuels;
    } // end method getFuelList()

    public ArrayList<String> getCarTypeList(){
        ArrayList<String> types = new ArrayList<String>();
        Set< HashMap.Entry<String, Integer> > st = vehicleList.entrySet();
        for (HashMap.Entry<String, Integer> me:st) 
        {
            types.add(me.getKey());
        }
        return types;
    } // end method getCarTypeList() 

    public String getFuel(String carName){
        return carDetails.get(carName).fuel;
    } // end of method getFuel

    public LocalDate getStartDate(String carName){
        return carDetails.get(carName).lastRecordedDate.plusDays(1);
    } // end of method getStartDate
    
    public String vehicleDisplay(){
        if (conn != null){
            if (carDetails.isEmpty()){
                return "No vehicles found on the database";
            } else {
                return "Select a vehicle to start.";
            }
        } else {
            return "Unable to make a connection with the database.";
        }
    } // end of vehicleDisplay
    
    public String vehicleSummary(String carName){
        String sql = "SELECT SUM(Amount) as total, MONTH(Start_Date) as Month, YEAR(Start_Date) as year FROM Fuel "
                + " WHERE Vehicle_id = " + carDetails.get(carName).id 
                + " AND Start_Date " + getBetweenSchoolYear()
                + " GROUP BY year, month";
        ResultSet result = doQuery(sql);
        String vehicleSummary = "";
        vehicleSummary += carName + "\n\n";
        int totalFuel = 0;
        try {
            while (result.next()) {
                totalFuel += result.getInt("Total");
                vehicleSummary += getMonthName(result.getInt("Month")) + "\t";
                vehicleSummary += result.getString("Total") + "\n";
            }
            vehicleSummary += "Total fuel\t"+totalFuel + "\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return vehicleSummary;
    } // end of method vehicleSummary

    public String createNewVehicle(String description,
    		                       String registration,
    		                       String vehicleType,
    		                       String fuelType
    		                       ){
    	int vehicleTypeID = vehicleList.get(vehicleType);
    	int fuelTypeID = fuelList.get(fuelType);
    	String sql = "INSERT INTO "
    			    + "Vehicle (Description, Registration, Vehicle_Type_ID, Fuel_Type_ID) "
			        + "VALUES(\""
			        + description + "\", \""
			        + registration + "\", "
			        + vehicleTypeID + ", "
			        + fuelTypeID + ")";
    	String result = insertDatabase(sql);
    	if (result != null) {
            Car car = new Car();
            car.id = getLastInsertID();
            car.name = description;
            car.fuel = fuelType;
            car.lastRecordedDate = LocalDate.now().minusMonths(1);
            carDetails.put(description, car);
    		cars.add(description);
    	}
    	return result;
    } // end of method createNewVehicle
    
    public String deleteVehicle(String carName){
    	int vehicleID = carDetails.get(carName).id;
    	String sql = "UPDATE vehicle "
    				+ "SET Decommissioned = 1 "
    				+ "WHERE Vehicle_ID = "
    				+ vehicleID;
    	return insertDatabase(sql);
    } // end of method deleteVehicle
    
    public String updateFuel(String carName, 
                             String startDate,
                             String endDate,
                             String fuelType,
                             String fuelAmount){
        int vehicleID = carDetails.get(carName).id;
        LocalDate ed = LocalDate.parse(endDate);
        if (carDetails.get(carName).lastRecordedDate.compareTo(ed) < 0)
            carDetails.get(carName).lastRecordedDate = ed;
        String sql = "INSERT INTO "
                + "Fuel (Start_Date, End_Date, Amount, Vehicle_ID) "
                + "VALUES(\""
                + startDate + "\", \""
                + endDate + "\", "
                + fuelAmount + ", "
                + vehicleID + ")";
        return insertDatabase(sql);
    } // end of method updateFuel
    
    private void getAllTripGroups(){
    	TripGroup group;
        String sql = "SELECT Trip_Group_ID, Description, Teacher_Only FROM trip_group";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                group = new TripGroup();
                group.id = result.getInt("Trip_Group_ID");
                group.description = result.getString("Description");
                group.teacher_only = result.getBoolean("Teacher_Only");
                groupDetails.put(group.description, group);
            }
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        
    } // end method getAllCars

    public ArrayList<String> getTripList(){
    	if (trips == null) {
    		trips = new ArrayList<String>();
	        Set< HashMap.Entry< String, TripGroup> > st = groupDetails.entrySet();
	        for (HashMap.Entry< String, TripGroup> me:st) 
	        {
	        	trips.add(me.getKey());
	        }
    	}
        return trips;
    } // end method getTripList()
    
    public boolean tripStaffOnly(String tripOption) {
    	if (tripOption == null) {
    		return false;
    	}
    	return groupDetails.get(tripOption).teacher_only;
    } // end of method tripStaffOnly
    
    public int tripID(String tripOption) {
    	if (tripOption == null) {
    		return 0;
    	}
    	return groupDetails.get(tripOption).id;
    } // end of method tripStaffOnly
    
    public String tripDisplay() {
        if (conn != null){
            if (groupDetails.isEmpty()){
                return "No trips found on the database";
            } else {
                return "Select a trip to start.";
            }
        } else {
            return "Unable to make a connection with the database.";
        }
	} // end of method tripDisplay
    
    public String tripSummary(String GroupName){
        String sql = "SELECT YEAR(Start_Date) AS year, "
        		+ "MONTH(Start_Date) AS month, "
        		+ "Description, "
        		+ "Number_of_buses, "
        		+ "bus_distance, "
        		+ "flight_distance, "
        		+ "Number_of_students, "
        		+ "Number_of_Teachers "
        		+ "FROM trip WHERE Trip_GROUP_ID = " + tripID(GroupName) 
                + " AND Start_Date " + getBetweenSchoolYear()
                + " ORDER BY year, month, Description";
        ResultSet result = doQuery(sql);
        String tripSummary = "";
        tripSummary += GroupName + "\n\n                  #Bus  Dist    Flight  Student Teacher\n";
        int totalBus = 0;
        int totalFlight = 0;
        try {
            while (result.next()) {
                totalBus += result.getInt("bus_distance") * result.getInt("Number_of_buses") *2;
                totalFlight += result.getInt("flight_distance") *2 * (result.getInt("Number_of_students") + result.getInt("Number_of_Teachers"));
                tripSummary += pad(getMonthName(result.getInt("Month")),3) + " ";
                tripSummary += pad(result.getString("Description"),16) + " ";
                tripSummary += result.getString("Number_of_buses") + "\t";
                tripSummary += result.getString("bus_distance") + "\t";
                tripSummary += result.getString("flight_distance") + "\t";
                tripSummary += result.getString("Number_of_students") + "\t";
                tripSummary += result.getString("Number_of_Teachers") + "\n";
            }
            tripSummary += "\nTotal bus distance\t"+totalBus + "\nTotal flight distance\t" + totalFlight + "\n";
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        return tripSummary;
    } // end of method tripSummary

    public String addTrip(String trip
			             ,String start
			             ,String end
			             ,String description
			             ,String busNumber
			             ,String busDistance
			             ,String airDistance
			             ,String studentNumber
			             ,String teacherNumber) {
    	int group_ID = tripID(trip);
        String sql = "INSERT INTO "
                + "Trip (Start_Date, End_Date, Trip_Group_ID, Description, Number_of_Buses, Bus_Distance, Flight_Distance, Number_of_Students, Number_of_Teachers) "
                + "VALUES(\""
                + start + "\", \""
                + end + "\", "
                + group_ID + ", \""
                + description + "\", "
                + busNumber + ", "
                + busDistance + ", "
                + airDistance + ", "
                + studentNumber + ", "
                + teacherNumber + ")";
        return insertDatabase(sql);
    } // end of method addTrip
    
} // end class TransportationDataCollector
