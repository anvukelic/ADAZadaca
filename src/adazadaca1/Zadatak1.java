/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca1;

import javax.swing.JOptionPane;

/**
 *
 * @author avukelic;
 */
public class Zadatak1 {

    //Write a program that asks the user for a number n and prints the sum of the numbers 1 to n
    public static void main(String[] args) {
        int n;
        while (true) {
            try {
                n = Integer.parseInt(JOptionPane.showInputDialog(null, "Unesi broj"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Nisi unio broj, unesi broj");
            }
        }
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        System.out.println("Zbroj brojeva od 1 do " + n + " je " + sum);
    }

}
