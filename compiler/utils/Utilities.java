/**
 * Description
 * @author Bobby Gabriel
 * @version 1.0
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
 */

package utils;

import java.io.*;

public class Utilities{

    public static void writeFile(byte[] bytearray, String fileName){

        try{
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(bytearray);
            out.close();
        }//end try
        
        catch(IOException e){
        System.out.println(e.getMessage());
        }//end catch
        
    }//end writeFile

}//end Utilities  