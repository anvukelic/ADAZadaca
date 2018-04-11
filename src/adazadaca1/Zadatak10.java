/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author avukelic;
 */
public class Zadatak10 {

    private static String[] english = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
        "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x",
        "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
        ",", ".", "?"};

    private static String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
        ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
        "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
        "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
        "-----", "--..--", ".-.-.-", "..--.."};

    //.-/-./-./.-./---/../-.. .-/
    public Zadatak10() {
    }
    private static Map<String, String> e2m = new HashMap<>();

    private static String input;

    public static void main(String[] args) {

        Object[] options = {"Engleski u morzeov kod", "Morzeov kod u engleski"};
        int o = JOptionPane.showOptionDialog(null, "Izaberi prijevod koji želiš", "Prevoditelj",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        napraviRadnju(o);

    }

    private static void napraviRadnju(int o) {

        switch (o) {
            case 0:
                //TEST PRIMJERI ZA MORZEA 
                //   Andrej = .-/-./-../.-././
                //   Vukelic = ...-/..-/-.-/./.-../../
                //   www.github.com/avukelic = .--/.--/.--/.-.-.-/--./../-/..../..-/-.../.-.-.-/-.-./---/--/null/.-/...-/..-/-.-/./.-../../
                prevediEngleski();
                break;
            case 1:
                //TEST PRIMJERI ZA MORZEA 
                //   .-/-.../-.-. = abc
                //   .-/-.../-.-. .-/-.../-.-. = abc abc
                //   .-/-./-../.-./---/../-../ -.././...-/./.-../---/.---./--/./-./-/ .-/-.-./.-/-.././--/-.--/ = android development academy
                prevediMorzeovKod();
                break;
            default:
                System.exit(0);
        }
    }

    private static void prevediEngleski() {
        StringBuilder sb = new StringBuilder();
        input = JOptionPane.showInputDialog("Unesite tekst na engleskom").toLowerCase();
        napuniMapuEngleski();
        for (int i = 0; i < input.length() - 1; i++) {
            if (!String.valueOf(input.charAt(i)).equals(" ")) {
                sb.append(e2m.get(String.valueOf(input.charAt(i))) + "/");
            } else {
                sb.append(" ");
            }
        }
        System.out.println("Uneseni tekst preveden u morzeov kod izgleda ovako: " + sb.toString());
    }

    private static void prevediMorzeovKod() {
        StringBuilder sb = new StringBuilder();
        StringBuilder recenica = new StringBuilder();
        JOptionPane.showMessageDialog(null, "Pravila unosa morzeovog koda:\nSvako slovo odvoji s /\nSvaku riječ odvoji razmakom\nZnakovi koje smiješ unositi su: \" . \",\" - \" , \" \" , \" / \"");
        input = JOptionPane.showInputDialog("Unesi tekst u morzeovom kodu ovdje");
        napuniMapuMorze();
        String[] niz = new String[6];
        int j = 0;
        int i = 0;
        while (i < input.length()) {
            if (String.valueOf(input.charAt(i)).equals(" ")
                    || String.valueOf(input.charAt(i)).equals(".")
                    || String.valueOf(input.charAt(i)).equals("-")
                    || String.valueOf(input.charAt(i)).equals("/")) {
                if (String.valueOf(input.charAt(i)).equals(" ")) {
                    i++;
                    recenica.append(" ");
                    continue;
                }
                try {
                    while (!String.valueOf(input.charAt(i)).equals("/")) {
                        niz[j] = String.valueOf(input.charAt(i));
                        i++;
                        j++;
                        if (i == 88) {
                            System.out.println(input.length());
                        }
                        if (i == input.length()) {
                            break;
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    JOptionPane.showMessageDialog(null, "Krivi unos");
                    prevediMorzeovKod();
                }
                for (int k = 0; k < j; k++) {
                    sb.append(niz[k]);
                }
                recenica.append(e2m.get(sb.toString()));
                sb = new StringBuilder();
                i++;
                j = 0;
            } else {
                JOptionPane.showMessageDialog(null, "Krivi unos");
                prevediMorzeovKod();
            }
        } 
        System.out.println("Uneseni tekst preveden u morzeov kod izgleda ovako: " + recenica.toString());

    }

    private static void napuniMapuEngleski() {
        for (int i = 0; i < english.length; i++) {
            e2m.put(english[i], morse[i]);
        }
    }

    private static void napuniMapuMorze() {
        for (int i = 0; i < english.length; i++) {
            e2m.put(morse[i], english[i]);
        }
    }

}
