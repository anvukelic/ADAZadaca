/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2.controller;

import adazadaca2.ZadatakCRUD;
import adazadaca2.model.Category;
import adazadaca2.model.News;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author avukelic;
 */
public class CategoryController {

    private static List<Category> categories = new ArrayList<>();
    private static int categoryId = 0;
    private static Scanner input = new Scanner(System.in);

    public static void controlCategory() {
        System.out.println();
        actionloop:
        for (;;) {
            System.out.println("What CRUD action do you want to make on categories?");
            System.out.println("To select another model write back");
            switch (input.nextLine().trim().toLowerCase()) {
                case "create":
                    categoryCreate();
                    break actionloop;
                case "read":
                    categoryRead();
                    break actionloop;
                case "update":
                    categoryUpdate();
                    break actionloop;
                case "delete":
                    categoryDelete();
                    break;
                case "back":
                    ZadatakCRUD.selectModel();
                default:
                    System.out.println("You can not do that action");
                    break;
            }
        }

    }

    private static void categoryCreate() {
        System.out.println();
        System.out.println("Create new category");
        Category category = new Category(categoryId++);
        for (;;) {
            System.out.println("What is category's name?");
            String name = input.nextLine();
            if (name == null || name.trim().length() == 0) {
                System.out.println("Name can not be empty");
            } else {
                category.setName(name);
                break;
            }
        }
        categories.add(category);
        System.out.println(category + " is added");

        for (;;) {
            System.out.println("Do you want to create another category?(yes/no)");
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    categoryCreate();
                    break;
                case "no":
                    controlCategory();
                    break;
                case "back":
                    controlCategory();
                default:
                    break;
            }
        }

    }

    private static void categoryRead() {
        System.out.println();
        System.out.println("Categories:");
        System.out.println("----------------------------------");
        if (!categories.isEmpty()) {
            for (Category category : categories) {
                System.out.printf("%d %s", category.getId(), category);
                for (News news : category.getNews()) {
                    System.out.printf(" - %s", news.toString());
                };
                System.out.println();
            }
        } else {
            System.out.printf("There is no categories yet");
        }
        System.out.println("----------------------------------");
        controlCategory();
    }

    private static void categoryUpdate() {
        System.out.println();
        showCategories();
        System.out.println("Choose category to update with id");

        updateCategory:
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
            boolean categoryExists = false;
            for (Category category : categories) {
                if (category.getId() == id) {
                    categoryExists = true;
                    System.out.println("Category to update " + category);
                    chooseAttribute:
                    for (;;) {
                        System.out.println("What attribute do you want to update?(name)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "name":
                                System.out.println("What is category new name?");
                                category.setName(input.nextLine());
                                for (;;) {
                                    System.out.println("Do you want to change another attribute?(yes/no)");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":
                                            continue chooseAttribute;
                                        case "no":
                                            break updateCategory;
                                        case "back":
                                            controlCategory();
                                        default:
                                            break;
                                    }
                                }
                            default:
                                break;
                        }
                    }
                }
            }
            if (!categoryExists) {
                System.out.println("There is no category with that id");
                categoryUpdate();
            }
        }
        for (;;) {
            System.out.println("Do you want update another category?(yes/no)");
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    categoryUpdate();
                case "no":
                    controlCategory();
                case "back":
                    controlCategory();
                default:
                    break;
            }
        }
    }

    private static void categoryDelete() {
        System.out.println();
        showCategories();
        System.out.println("Choose category to delete with id");
        deleteCategory:
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
            boolean categoryExists = false;
            for (Category category : categories) {
                categoryExists = false;
                if (category.getId() == id) {
                    categoryExists = true;
                    if (category.getNews().size() > 0) {
                        System.out.println("You can not delete category that is on at least article");
                        break deleteCategory;
                    }
                    System.out.println("Category to delete " + category);
                    while (true) {
                        System.out.println("Are you sure?(yes/no)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "yes":
                                categories.remove(category);
                                break deleteCategory;
                            case "no":
                                controlCategory();
                            case "back":
                                controlCategory();
                            default:
                                break;
                        }
                    }
                }

            }
            if (!categoryExists) {
                System.out.println("There is no category with that id");
                categoryDelete();
            }
        }
        for (;;) {
            if (!categories.isEmpty()) {
                System.out.println("Do you want delete another category?(yes/no)");
                switch (input.nextLine().trim().toLowerCase()) {
                    case "yes":
                        categoryDelete();
                    case "no":
                        controlCategory();
                    case "back":
                        controlCategory();
                    default:
                        break;
                }
            } else {
                System.out.println("There is no authors anymore");
                System.out.println();
                controlCategory();
            }
        }
    }

    public static void showCategories() {
        System.out.println("Categories:");
        System.out.println("----------------------------------");
        if (!categories.isEmpty()) {
            categories.forEach(category -> System.out.println(category.getId() + " " + category));
        } else {
            System.out.println("There is no categories yet");
            controlCategory();
        }
        System.out.println("----------------------------------");
    }

    public static List<Category> getCategories() {
        return categories;
    }

}
