/* 
 * The MIT License
 *
 * Copyright 2019 SaiHtoo.
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
 * Author:  SaiHtoo
 * Created: May 14, 2019
 */

INSERT INTO AC_Type (AC_Type_ID, Description, Number)
Values (1 , 'Mitsubishi' , 26),
    (2 , 'Panasonic' , 88),
    (3 , 'York (Multi-system)' , 90),
    (4 , 'York (A/C Unit)' , 26);

INSERT INTO AC_CO2 (AC_Type_ID, Start_Date, Multiplier)
Values (1 , '2018-01-01' , 91),
    (2 , '2018-01-01' , 58),
    (3 , '2018-01-01' , 174),
    (4 , '2018-01-01' , 102);
