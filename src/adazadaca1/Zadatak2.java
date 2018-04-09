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
public class Zadatak2 {

    //Write a program that asks the user for a number n and gives them the possibility 
    //to choose between computing the sum and computing the product of 1,…,n. pr
    private static int n;

    public static void main(String[] args) {
        while (true) {
            try {
                n = Integer.parseInt(JOptionPane.showInputDialog("Unesi broj"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Nisi unio broj, unesi broj");
            }
        }
        Object[] options = {"Zbroji", "Pomnoži"};
        int o = JOptionPane.showOptionDialog(null, "Izaberi radnju koju želiš obaviti", "Izaberi radnju",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        napraviRadnju(o);

    }

    private static void napraviRadnju(int o) {
        int sum = 0, prod = 1;
        switch (o) {
            case 0:
                for (int i = 1; i <= n; i++) {
                    sum += i;
                }
                System.out.println("Zbroj brojeva od 1 do " + n + " je " + sum);
                break;
            case 1:
                for (int i = 1; i <= n; i++) {
                    prod *= i;
                }
                System.out.println("Produkt brojeva od 1 do " + n + " je " + prod);
                break;
            default:
                System.exit(0);
        }
    }

}
