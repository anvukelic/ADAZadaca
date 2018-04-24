/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca1;

/**
 *
 * @author avukelic;
 */
public class Zadatak3 {
    
    //Write a program that prints a multiplication table for numbers up to 12.
    public static void main(String[] args) {
        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                System.out.printf("%3d ",(i+1)*(j+1));
            }
            System.out.println();
        }
    }
    
}
