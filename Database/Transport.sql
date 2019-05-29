/* 
 * The MIT License
 *
 * Copyright 2019 Swan.
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
/**
 * Author:  Swan
 * Created: May 3, 2019
 */
INSERT INTO Fuel_Type(Fuel_Type_ID, Description)
VALUE ( 1, Diesel)
(2, Gasoline_95)

INSERT INTO Vehicle_Type(Vehicle_ID, Description)
VALUE (1, Bus)
(2, Car/Truck)
INSERT INTO Vehicle (Vehicle_ID, Description, Type)
   
(1, Bus,1)
(2, Car/Truck, 2)
(3, Car/Truck, 2)
(4, Car/Truck, 2)
(5, Car/Truck, 2)
(6, Car/Truck, 2)
(7, Car/Truck, 2) 
(8, Car/Truck, 2) 
(9, Bus, 1)

INSERT INTO Fuel(Fuel_ID, Start_Date, End_Date, Type, Amount, Vehicle_ID)
VALUE (1, "Disel", "2018-7-1", "2019-5-1", 1, 552 )
(2, "Gasoline_95", "2018-7-1", "2019-5-1", 2, 629)
(3, "Diesel", 1, "2018-7-1", "2019-5-1", 333)
(4, "Gasoline_95", "2018-7-1", "2019-5-1", 2, 611)
(5, "Gasoline_95", "2018-7-1", "2019-5-1", 2, 1022)
(6, "Diesel", 1, "2018-7-1", "2019-5-1", 1264.8)
(7, "Gasoline_95", "2018-7-1", "2019-5-1", 2 682.9)
(8, "Gasoline_95", "2018-7-1", "2019-5-1", 2, 715)
(9, "Diesel", "2018-7-1", "2019-5-1", 1, 423.8)




