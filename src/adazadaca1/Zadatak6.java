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
public class Zadatak6 {
    //Write a method that reverses a array.
    public static void main(String[] args) {
        int n;
        while (true) {
            try {
                n = Integer.parseInt(JOptionPane.showInputDialog("Unesi broj"));
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Nisi unio broj, unesi broj");
            }
        }
        int niz[] = new int[n];

        for (int i = 0; i < n; i++) {
            while (true) {
                try {
                    niz[i] = Integer.parseInt(JOptionPane.showInputDialog("Unesi broj na mjesto " + (i + 1) + ". u nizu"));
                    break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Nisi unio broj, unesi broj");
                }
            }
        }
        System.out.println("Niz prije okretanja");
        ispisiNiz(niz);
        niz = okreniNiz(niz);
        System.out.println("Niz poslije okretanja");
        ispisiNiz(niz);
        
    }
    private static int[] okreniNiz(int niz[]){
        int x;
        int y = niz.length - 1;
        for(int i = 0; i < niz.length/2; i++){
            x = niz[i];
            niz[i] = niz[y];
            niz[y] = x;
            y--;
        }
        return niz;
    }

    private static void ispisiNiz(int[] niz) {
        for(int i = 0; i<niz.length; i++){
            System.out.print(niz[i] + " ");
        }
        System.out.println();
    }
    
}
