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

package utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author gfoster
 */
public class Encrypt {
    /**
     * Returns a hexadecimal encoded MD5 hash for the input String.
     * @param data
     * @return a MD5 hash
     */
    public static String getMD5Hash(String data, boolean DEBUG) {
        String result = null;
        String algorithm = "MD5";
        String encoding = "UTF-8";
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(data.getBytes(encoding));
            return DatatypeConverter.printHexBinary(hash); // make it printable
        }catch(NoSuchAlgorithmException  ex) {
            System.out.println("Unknown algorithm " + algorithm);
            if (DEBUG) ex.printStackTrace();
        }catch (UnsupportedEncodingException ex) {
            System.out.println("Unknown Encoding " + encoding);
            if (DEBUG) ex.printStackTrace();
        }
        return result;
    } // end of method getMD5Hash()
    
    public static String salt(String token, String salt){
        return salt + token + salt;
    } // end of method salt()
} // end of class Encrypt
