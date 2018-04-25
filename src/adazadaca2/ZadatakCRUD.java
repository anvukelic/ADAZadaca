/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2;

import adazadaca2.controller.AuthorController;
import adazadaca2.controller.CategoryController;
import adazadaca2.controller.NewsController;
import adazadaca2.model.Category;
import adazadaca2.model.News;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author avukelic;
 */
public class ZadatakCRUD {

    private static Scanner input;

    public static void main(String[] args) {
        input = new Scanner(System.in);
        System.out.println("Welcome to ADA CMS");
        System.out.println("HINT: on every yes/no or when you have to choose attribute you can write ");
        System.out.println("back and go back to selecting CRUD action for that model");
        System.out.println("");
        selectModel();
    }

    public static void selectModel() {
        System.out.println("Choose one model");
        System.out.println("Models are: author, category, news");
        System.out.println("Write today for articles added on this day");
        for (;;) {
            switch (input.nextLine().trim().toLowerCase()) {
                case "author":
                    AuthorController.controlAuthor();
                    break;
                case "category":
                    CategoryController.controlCategory();
                    break;
                case "news":
                    NewsController.controlNews();
                    break;
                case "today":
                    makeNews();
                    break;
                default:
                    System.out.println("There is no model with that name.");
                    System.out.println("Models are: author, category, news");
            }
        }
    }

    private static void makeNews() {
        SimpleDateFormat sd = new SimpleDateFormat("EEE, dd.MM.yyyy.");

        if (NewsController.getNews().size() > 0) {
            System.out.println("------------------------------");
            System.out.println("|News on day " + sd.format(new Date()) + "|");
            int i = 0;
            for (News article : NewsController.getNews()) {
                if (i == 0) {
                    System.out.println("------------------------------");
                    i++;
                }
                System.out.println(article.getTitle() + " - " + article.getAuthor());
                System.out.println(article.getText());
                System.out.println("------------------------------");
            }
        } else {
            System.out.println("There is no articles today");
            System.out.println("Models are: author, category, news");
        }
    }

}
