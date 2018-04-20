/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2.controller;

import adazadaca2.ZadatakCRUD;
import static adazadaca2.controller.NewsController.controlNews;
import adazadaca2.model.Author;
import adazadaca2.model.News;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author avukelic;
 */
public class AuthorController {

    private static List<Author> authors = new ArrayList<>();
    private static int authorId = 0;
    private static Scanner input = new Scanner(System.in);

    public static void controlAuthor() {
        System.out.println();
        actionloop:
        for (;;) {
            System.out.println("What CRUD action do you want to make on authors?");
            System.out.println("To select another model write back");
            switch (input.nextLine().trim().toLowerCase()) {
                case "create":
                    authorCreate();
                    break actionloop;
                case "read":
                    authorRead();
                    break actionloop;
                case "update":
                    authorUpdate();
                    break actionloop;
                case "delete":
                    authorDelete();
                    break;
                case "back":
                    ZadatakCRUD.selectModel();
                default:
                    System.out.println("You can not do that action");
                    break;
            }
        }

    }

    private static void authorCreate() {
        System.out.println();
        System.out.println("Create new author");
        Author author = new Author(authorId++);
        for (;;) {
            System.out.println("What is author's name?");
            String name = input.nextLine();
            if (name == null || name.trim().length() == 0) {
                System.out.println("Name can not be empty");
            } else {
                author.setName(name);
                break;
            }
        }
        for (;;) {
            System.out.println("What is author's last name?");
            String lastname = input.nextLine();
            if (lastname == null || lastname.trim().length() == 0) {
                System.out.println("Last name can not be empty");
            } else {
                author.setLastname(lastname);
                break;
            }
        }
        authors.add(author);
        System.out.println(author + " is added");
        System.out.println("Do you want to create another author?(yes/no)");
        for (;;) {
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    authorCreate();
                    break;
                case "no":
                    controlAuthor();
                    break;
                case "back":
                    controlAuthor();
                default:
                    System.out.println("Do you want to create another author?(yes/no)");
            }
        }

    }

    private static void authorRead() {
        System.out.println();
        System.out.println("Authors:");
        System.out.println("---------------------------------- ");
        if (!authors.isEmpty()) {
            for (Author author : authors) {
                System.out.printf("%d %s", author.getId(), author);
                for (News news : author.getNews()) {
                    System.out.printf(" - %s", news.toString());
                };
                System.out.println();
            }
        } else {
            System.out.printf("There is no authors yet");
        }
        System.out.println("-----------------------------------");
        controlAuthor();
    }

    private static void authorUpdate() {
        System.out.println();
        showAuthors();
        System.out.println("Choose author to update with id");

        updateAuthor:
        for (;;) {
            int id;
            for (;;) {
                try {
                    id = Integer.parseInt(input.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("You have to insert number");
                }
            }
            boolean authorExists = false;
            for (Author author : authors) {
                authorExists = false;
                if (author.getId() == id) {
                    authorExists = true;
                    System.out.println("Author to update " + author);
                    chooseAttribute:
                    for (;;) {
                        System.out.println("What attribute do you want to update?(name/lastname)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "name":
                                System.out.println("What is author new name?");
                                author.setName(input.nextLine());
                                for (;;) {
                                    System.out.println("Do you want to change another attribute?(yes/no)");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":

                                            continue chooseAttribute;
                                        case "no":
                                            break updateAuthor;
                                        case "back":
                                            controlAuthor();
                                        default:
                                            break;
                                    }
                                }
                            case "lastname":
                                System.out.println("What is author new last name?");
                                author.setLastname(input.nextLine());
                                for (;;) {
                                    System.out.println("Do you want to change another attribute?(yes/no)");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":
                                            continue chooseAttribute;
                                        case "no":
                                            break updateAuthor;
                                        case "back":
                                            controlAuthor();
                                        default:
                                            break;
                                    }
                                }
                            case "back":
                                controlAuthor();
                            default:
                                break;
                        }
                    }
                }
            }
            if (!authorExists) {
                System.out.println("There is no author with that id");
                authorUpdate();
            }
        }
        for (;;) {
            System.out.println("Do you want update another author?(yes/no)");
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    authorUpdate();
                case "no":
                    controlAuthor();
                case "back":
                    controlAuthor();
                default:
                    break;
            }
        }
    }

    private static void authorDelete() {
        System.out.println();
        System.out.println("Authors:");
        System.out.println("----------------------------------");
        if (!authors.isEmpty()) {
            authors.forEach(author -> System.out.println(author.getId() + " " + author.toString()));
        } else {
            System.out.println("There is no authors yet");
            controlAuthor();
        }
        System.out.println("----------------------------------");
        System.out.println("Choose author to delete with id");

        deleteAuthor:
        for (;;) {
            int id;
            for (;;) {
                try {
                    id = Integer.parseInt(input.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("You have to insert number");
                }
            }
            boolean authorExists = false;
            for (Author author : authors) {
                if (author.getId() == id) {
                    authorExists = true;
                    System.out.println("Author to delete " + author);
                    if (author.getNews().size() > 0) {
                        System.out.println("You can not delete author that have at least one article");
                        break deleteAuthor;
                    }
                    while (true) {
                        System.out.println("Are you sure?(yes/no)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "yes":
                                authors.remove(author);
                                break deleteAuthor;
                            case "no":
                                controlAuthor();
                            case "back":
                                controlAuthor();
                            default:
                                break;
                        }
                    }
                }
            }
            if (!authorExists) {
                System.out.println("There is no author with that id");
                authorDelete();
            }
        }
        for (;;) {
            if (!authors.isEmpty()) {
                System.out.println("Do you want delete another author?(yes/no)");
                switch (input.nextLine().trim().toLowerCase()) {
                    case "yes":
                        authorDelete();
                    case "no":
                        controlAuthor();
                    case "back":
                        controlAuthor();
                    default:
                        break;
                }
            } else {
                System.out.println("There is no authors anymore");
                controlAuthor();
            }
        }
    }

    public static void showAuthors() {
        System.out.println("Authors:");
        System.out.println("----------------------------------");
        if (!authors.isEmpty()) {
            authors.forEach(author -> System.out.println(author.getId() + " " + author));
        } else {
            System.out.println("There is no authors yet");
            controlAuthor();
        }
        System.out.println("----------------------------------");
    }

    public static List<Author> getAuthors() {
        return authors;
    }

}
