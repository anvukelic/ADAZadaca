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
public class ZadatakInchoo {
    public static void main(String[] args) {

		// Ucitavanje redaka i stupaca matrice
		int brojRedaka = Integer.parseInt(JOptionPane.showInputDialog("Unesite broj redaka"));
		int brojStupaca = Integer.parseInt(JOptionPane.showInputDialog("Unesite broj stupaca"));

		// Pomocne varijable koje ce se mijenjati za potrebu prolaženja kroz matricu
		int i = brojRedaka - 1;
		int j = brojStupaca - 1;

		
		int uvlacenje = 0;
		
		//prati broj zavoja
		int skretac = 1;		
		// 1 - [brojRedaka, 0], 2 - [0,brojStupaca], 3 - [0,0], 4 - [brojRedaka, brojStupaca]
		int zavoj = 0;
		// 1 - lijevo, 2 - desno, 3 - gore, 4 - dolje
		int smjerKretanja = 1;

		// Deklariranje matrice
		int matricaPuz[][] = new int[brojRedaka][brojStupaca];

		for (int x = 1; x <= brojRedaka * brojStupaca; x++) {
			//Nakon zavoja potrebno je pomjeriti se za jedno polje kako bi se spirala dobro nastavila
			switch (zavoj) {
			case 1:
				i--;
				zavoj = 0;
				break;
			case 2:
				i++;
				zavoj = 0;
				uvlacenje++;
				break;
			case 3:
				j++;
				zavoj = 0;
				break;
			case 4:
				j--;
				zavoj = 0;
				break;
			default:
				break;
			}
			
			//Upisivanje u matricu
			matricaPuz[i][j] = x;
			
			//Kretanje kroz matricu
			if (skretac % 2 != 0) {
				if (smjerKretanja == 1) {
					if (j == uvlacenje) {
						skretac++;
						smjerKretanja = 3;
						zavoj = 1;
						continue;
					} else {
						j--;
						continue;
					}
				}
				if (smjerKretanja == 2) {

					if (j == brojStupaca - 1 - uvlacenje) {
						skretac++;
						smjerKretanja = 4;
						zavoj = 2;
					} else {
						j++;
					}
				}
			} else {
				if (smjerKretanja == 3) {
					if (i == uvlacenje) {
						skretac++;
						smjerKretanja = 2;
						zavoj = 3;
						continue;
					} else {
						i--;
						continue;
					}
				}
				if (smjerKretanja == 4) {
					if (i == brojRedaka - 1 - uvlacenje) {
						skretac++;
						smjerKretanja = 1;
						zavoj = 4;
					} else {
						i++;
					}
				}
			}
		}
		
		//ispisivanje matrice
		for (i = 0; i < brojRedaka; i++) {
			for (j = 0; j < brojStupaca; j++) {
				System.out.printf("%3d ", matricaPuz[i][j]);
			}
			System.out.println();
		}

}
}
