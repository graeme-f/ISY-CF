/* 
 * The MIT License
 *
 * Copyright 2019 Valylie.
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
 * Author:  Valylie
 * Created: May 2, 2019
 */
INSERT into Paper(Paper_ID, Start_Date, End_Date, A4_Reams) 
VALUES (1, "2017-01-01", "2017-02-01",  150),
(2, "2017-02-01", "2017-03-01",  175),
(3, "2017-03-01", "2017-04-01",  160),
(4, "2017-04-01", "2017-05-01",  175),
(5, "2017-05-01", "2017-06-01",  130),
(6, "2017-06-01", "2017-07-01",  145),
(7, "2017-07-01", "2017-08-01",  150),
(8, "2017-08-01", "2017-09-01",  175),
(9, "2017-09-01", "2017-010-01",  100),
(10, "2017-10-01", "2017-11-01",  125),
(11, "2017-11-01", "2017-12-01",  115),
(12, "2017-12-01", "2018-1-01",   135), 
(13, "2019-05-29", "2019-05-29", 420);

INSERT into Waste_Type(Waste_Type_ID, Description, Capacity, Quantity)
(1 , "large bin", 1.863, 3),
(2, "smallbin" , 1.375, 5);

INSERT into Waste (Waste_ID,Type,Start_Date,End_Date,Amount) 
VALUES (1, 1, "2019-01-01", "2019-02-01", 8),
(2, 1, "2019-02-01", "2019-03-01", 8),
(3, 1, "2019-03-01", "2019-04-01", 8),
(4, 1, "2019-04-01", "2019-05-01", 8),
(5, 1, "2019-05-01", "2019-06-01", 8),
(6, 1, "2019-06-01", "2019-07-01", 8),
(7, 1, "2019-07-01", "2019-08-01", 8),
(8, 1, "2019-08-01", "2019-09-01", 8),
(9, 1, "2019-09-01", "2019-10-01", 8),
(10, 1, "2019-10-01", "2019-11-01", 8),
(11, 1, "2019-11-01", "2019-12-01", 8),
(12, 1, "2019-12-01", "2020-01-01", 8);

INSERT into Waste (Waste_ID,Type,Start_Date,End_Date,Amount) 
VALUES (13, 2, "2019-01-01", "2019-02-01", 8),
(14, 2, "2019-02-01", "2019-03-01", 8),
(15, 2, "2019-03-01", "2019-04-01", 8),
(16, 2, "2019-04-01", "2019-05-01", 8),
(17, 2, "2019-05-01", "2019-06-01", 8),
(18, 2, "2019-06-01", "2019-07-01", 8),
(19, 2, "2019-07-01", "2019-08-01", 8),
(20, 2, "2019-08-01", "2019-09-01", 8),
(21, 2, "2019-09-01", "2019-10-01", 8),
(22, 2, "2019-10-01", "2019-11-01", 8),
(23, 2, "2019-11-01", "2019-12-01", 8),
(24, 2, "2019-12-01", "2020-01-01", 8);

INSERT into Yearbook (Yearbook_ID, Date, Paper_Paper_ID )
Values (1, "2019-05-29", 13);