/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author avukelic;
 */
public class Zadatak4 {
    //Write a program that prints the next 20 leap years.
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        
        int counter = 0;
        while(counter <20){
            if(year%4!=0){
                year++;
                continue;
            } else {
                System.out.println(year);
                year += 4;
                counter++;
            }            
        }

    }
    
}
