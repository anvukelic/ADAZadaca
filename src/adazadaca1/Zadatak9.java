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
public class Zadatak9 {

    //Write a program that prints Matrix code lookalike in console.
    public static void main(String[] args) {
        int n = 0;

        while (true) {
            if (n < 120) {
                Double i = Math.random();
                String tekst = i.toString().substring(8, 10);
                if ((Integer.parseInt(tekst) > 47 && Integer.parseInt(tekst) < 58) || 
                    (Integer.parseInt(tekst) > 64 && Integer.parseInt(tekst) < 91) ||
                    (Integer.parseInt(tekst) > 96 && Integer.parseInt(tekst) < 123) ) {
                    tekst = Character.toString((char) Integer.parseInt(tekst));
                } 
                System.out.print(tekst);
                n++;
            } else {
                System.out.println();
                n = 0;
            }
        }
    }

}
