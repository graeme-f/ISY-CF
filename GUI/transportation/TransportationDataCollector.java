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
    
    private static TransportationDataCollector singleInstance = null;
    HashMap<String, Car> carDetails;
    HashMap<Integer, String> fuelList;
    HashMap<Integer, String> vehicleList;
    
    public static TransportationDataCollector getInstance() 
    { 
        if (singleInstance == null) 
            singleInstance = new TransportationDataCollector(); 
  
        return singleInstance; 
    } // end of getInstance method
    
    // Creator is private to make this a singleton class
    private TransportationDataCollector(){
        super();
        getFuelTypes();
        getVehicleTypes();
        getAllCars();
    } // end of constructor
    
    private void getFuelTypes(){
        fuelList = new HashMap();
        String sql = "SELECT * FROM Fuel_Type";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                int fuelID = result.getInt("Fuel_Type_ID");
                String desc = result.getString("description");
                fuelList.put (fuelID, desc);}
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }

    }

    private void getVehicleTypes(){
        vehicleList = new HashMap();
        String sql = "SELECT * FROM Vehicle_Type";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                int vehicleID = result.getInt("Vehicle_Type_ID");
                String desc = result.getString("Description");
                vehicleList.put (vehicleID, desc);}
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }

    }

    private void getAllCars(){
        carDetails = new HashMap();
        Car car = new Car();
        String sql = "SELECT * FROM Vehicle";
        ResultSet result = doQuery(sql);
        try {
            while (result.next()) {
                car.id = result.getInt("Vehicle_ID");
                car.name = result.getString("Description");
                int fuelID = result.getInt("Type");
                car.fuel = fuelList.get(fuelID);
                carDetails.put(car.name, car);
            }
        } catch (SQLException error) {
                ErrorMessage.display(error.getMessage());
        }
        
    } // end method getAllCars

    public ArrayList<String> getCarList(){
        ArrayList<String> cars = new ArrayList();
        Set< HashMap.Entry< String, Car> > st = carDetails.entrySet();
        for (HashMap.Entry< String, Car> me:st) 
        {
            cars.add(me.getKey());
        }
         return cars;
    } // end method getCarList()
    
    public ArrayList<String> getFuelList(){
        ArrayList<String> fuels = new ArrayList();
        Set< HashMap.Entry<Integer, String> > st = fuelList.entrySet();
        for (HashMap.Entry<Integer, String> me:st) 
        {
            fuels.add(me.getValue());
        }
        return fuels;
    } // end method getFuelList()

    public ArrayList<String> getCarTypeList(){
        ArrayList<String> types = new ArrayList();
        Set< HashMap.Entry<Integer, String> > st = vehicleList.entrySet();
        for (HashMap.Entry<Integer, String> me:st) 
        {
            types.add(me.getValue());
        }
        return types;
    } // end method getCarTypeList() 

    public String getFuel(String carName){
        return carDetails.get(carName).fuel;
    } // end of method getFuel

    public LocalDate getStartDate(String carName){
        return carDetails.get(carName).lastRecordedDate;
    } // end of method getStartDate
    
    public String vehicleSummary(String carName){
        // TODO the vehicle summary needs to come from the database
        String vehicleSummary = "";
        vehicleSummary += carName + "\n\n";
        vehicleSummary += "January" + "\t" + "63 litres" + "\n";
        vehicleSummary += "February" + "\t" + "157 litres" + "\n";
        vehicleSummary += "March" + "\t" + "83 litres" + "\n";
        vehicleSummary += "\nTotal usage" + "\t" + "303 litres";
        return vehicleSummary;
    } // end of method vehicleSummary

    public String createNewVehicle(String [] values){
        for (String line : values){
            System.out.println(line);
        }
        return "Security controller not yet built, database not updated";
    } // end of method createNewVehicle
    
    public String deleteVehicle(String carName){
        return "Not yet implemented";
    }
    
    public String updateFuel(String carName, 
                             String startDate,
                             String endDate,
                             String fuelType,
                             String fuelAmount){
        String fuelID = "1"; // TODO get the number from the fuelType
        String vehicleID = "1"; // TODO get the number from the carName
        String sql = "INSERT INTO "
                + "Fuel (Start_Date, End_Date, Type, Amount, Vehicle_ID) "
                + "VALUES(\""
                + startDate + "\", \""
                + endDate + "\", "
                + fuelID + ", "
                + fuelAmount + ", "
                + vehicleID + ")";
        return insertDatabase(sql);
    }
    
} // end class TransportationDataCollector
