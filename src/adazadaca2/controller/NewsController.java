/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2.controller;

import adazadaca2.ZadatakCRUD;
import static adazadaca2.controller.AuthorController.controlAuthor;
import adazadaca2.model.Author;
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
public class NewsController {

    private static List<News> news = new ArrayList<>();
    private static int newsId = 0;
    private static Scanner input = new Scanner(System.in);

    public static void controlNews() {
        System.out.println();
        actionloop:
        for (;;) {
            System.out.println("What CRUD action do you want to make on news?");
            System.out.println("To select another model write back");
            switch (input.nextLine().trim().toLowerCase()) {
                case "create":
                    newsCreate();
                    break actionloop;
                case "read":
                    newsRead();
                    break actionloop;
                case "update":
                    newsUpdate();
                    break actionloop;
                case "delete":
                    newsDelete();
                    break;
                case "back":
                    ZadatakCRUD.selectModel();
                default:
                    System.out.println("You can not do that action");
                    break;
            }
        }

    }

    private static void newsCreate() {
        System.out.println();
        System.out.println("Create new article");
        News article = new News(newsId++);
        for (;;) {
            System.out.println("What is article's title?");
            String title = input.nextLine();
            if (title == null || title.trim().length() == 0) {
                System.out.println("Title can not be empty");
            } else {
                article.setTitle(title);
                break;
            }
        }
        for (;;) {
            System.out.println("What is article's text?");
            String text = input.nextLine();
            if (text == null || text.trim().length() == 0) {
                System.out.println("Text can not be empty");
            } else {
                article.setText(text);
                break;
            }
        }
        addCategory:
        for (;;) {
            if (CategoryController.getCategories().isEmpty()) {
                System.out.println("There is no categories yet");
                System.out.println("You can update article later");
                break;
            }
            if (CategoryController.getCategories().size() == article.getCategory().size()) {
                System.out.println("You added all categories to this article");
                break;
            }
            System.out.println("Add article to category?");
            chooseCategory:
            for (;;) {
                System.out.println("Categories:");
                System.out.println("----------------------------------");
                CategoryController.getCategories().forEach(category -> System.out.println(category.getId() + " " + category));
                System.out.println("----------------------------------");
                System.out.println("Choose category id to add");
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
                for (Category category : CategoryController.getCategories()) {
                    if (category.getId() == id) {
                        categoryExists = true;
                        for (Category categoryNews : article.getCategory()) {
                            if (id == categoryNews.getId()) {
                                System.out.println("You can not add duplicate categories");
                                for (;;) {
                                    System.out.println("Do you want to add another category?");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":
                                            continue chooseCategory;
                                        case "no":
                                            break chooseCategory;
                                        case "back":
                                            controlNews();
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        article.getCategory().add(category);
                        System.out.println("Article is added to " + category + " category");
                        for (;;) {
                            System.out.println("Do you want to add another category?(yes/no)");
                            switch (input.nextLine().trim().toLowerCase()) {
                                case "yes":
                                    continue addCategory;
                                case "no":
                                    break addCategory;
                                case "back":
                                    controlNews();
                                default:
                                    break;
                            }
                        }
                    }
                }
                if (!categoryExists) {
                    System.out.println("There is no category with that id");
                    continue addCategory;
                }
            }            
        }
        addAuthor:
        for (;;) {
            if (AuthorController.getAuthors().isEmpty()) {
                System.out.println("There is no authors yet");
                System.out.println("You can update article later");
                break addAuthor;
            }
            System.out.println("Who is author of this article?");
            chooseAuthor:
            for (;;) {
                System.out.println("----------------------------------");
                AuthorController.getAuthors().forEach(author -> System.out.println(author.getId() + " " + author));
                System.out.println("----------------------------------");
                System.out.println("Choose author id to add");
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
                for (Author author : AuthorController.getAuthors()) {
                    if (author.getId() == id) {
                        authorExists = true;
                        article.setAuthor(author);
                        break addAuthor;
                    }
                }
                if (!authorExists) {
                    System.out.println("There is no author with that id");
                }

            }

        }

        article.setDate(
                new Date());
        news.add(article);

        System.out.println(article + " is added");
        for (Category categoryOnArticle : article.getCategory()) {
            for (Category category : CategoryController.getCategories()) {
                if (category.getId() == categoryOnArticle.getId()) {
                    category.getNews().add(article);
                    break;
                }
            }
        }
        if (article.getAuthor() != null) {
            AuthorController.getAuthors().get(AuthorController.getAuthors().indexOf(article.getAuthor())).getNews().add(article);
        }
        for (;;) {
            System.out.println("Do you want to create another article?(yes/no)");
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    newsCreate();
                    break;
                case "no":
                    controlNews();
                    break;
                case "back":
                    controlNews();
                default:
                    break;
            }
        }

    }

    private static void newsRead() {
        System.out.println();
        System.out.println("News:");
        if (!news.isEmpty()) {
            int j = 0;
            for (News article : news) {
                if (j == 0) {
                    System.out.println("---------------------------------- ");
                    j++;
                }
                System.out.printf("Title: %s\n", article);
                System.out.println("Author: " + article.getAuthor());
                System.out.println("Date: " + getFormatedDate(article.getDate()));
                System.out.printf("Categories: ");
                int i = 0;
                for (Category category : article.getCategory()) {
                    if (i == 0) {
                        System.out.printf("%s", category);
                        i++;
                    } else {
                        System.out.printf(" - %s", category);
                    }
                };
                System.out.println("");
                System.out.println("---------------------------------- ");
            }
        } else {
            System.out.println("---------------------------------- ");
            System.out.printf("There is no authors yet");
            System.out.println("---------------------------------- ");
        }

        controlNews();
    }

    private static void newsUpdate() {
        System.out.println();
        System.out.println("Articles:");
        System.out.println("----------------------------------");
        if (!news.isEmpty()) {
            news.forEach(article -> System.out.println(article.getId() + " " + article));
        } else {
            System.out.println("There is no articles yet");
            controlAuthor();
        }
        System.out.println("----------------------------------");
        System.out.println("Choose article to update with id");

        updateArticle:
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
            for (News article : news) {
                if (article.getId() == id) {
                    authorExists = true;
                    System.out.println("Article to update " + article);
                    chooseAttribute:
                    for (;;) {
                        System.out.println("What attribute do you want to update?(title/text/category/author)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "title":
                                System.out.println("What is article new title?");
                                article.setTitle(input.nextLine());
                                for (;;) {
                                    System.out.println("Do you want to change another attribute?(yes/no)");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":
                                            continue chooseAttribute;
                                        case "no":
                                            break updateArticle;
                                        case "back":
                                            controlNews();
                                        default:
                                            break;
                                    }
                                }
                            case "text":
                                System.out.println("What is article new text?");
                                article.setText(input.nextLine());
                                for (;;) {
                                    System.out.println("Do you want to change another attribute?(yes/no)");
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "yes":
                                            continue chooseAttribute;
                                        case "no":
                                            break updateArticle;
                                        case "back":
                                            controlNews();
                                        default:
                                            break;
                                    }
                                }
                            case "category":
                                System.out.println("'" + article + "' have categories:");
                                System.out.println("----------------------------------");
                                for (Category category : article.getCategory()) {
                                    System.out.println(category);
                                }
                                System.out.println("----------------------------------");
                                System.out.println("Do you want to add or remove category?");
                                for (;;) {
                                    switch (input.nextLine().trim().toLowerCase()) {
                                        case "add":
                                            chooseCategory:
                                            for (;;) {
                                                System.out.println();
                                                System.out.println("Categories:");
                                                System.out.println("----------------------------------");
                                                CategoryController.getCategories().forEach(category -> System.out.println(category.getId() + " " + category));
                                                System.out.println("----------------------------------");
                                                System.out.println("Choose category id to add");
                                                int idCategory;
                                                for (;;) {
                                                    try {
                                                        idCategory = Integer.parseInt(input.nextLine());
                                                        break;
                                                    } catch (NumberFormatException e) {
                                                        System.out.println("You have to insert number");
                                                    }
                                                }
                                                boolean categoryExists = false;
                                                for (Category category : CategoryController.getCategories()) {
                                                    if (category.getId() == idCategory) {
                                                        categoryExists = true;
                                                        for (Category categoryNews : article.getCategory()) {
                                                            if (idCategory == categoryNews.getId()) {
                                                                System.out.println("You can not add duplicate categories");
                                                                for (;;) {
                                                                    switch (input.nextLine().trim().toLowerCase()) {
                                                                        case "yes":
                                                                            continue chooseCategory;
                                                                        case "no":
                                                                            break chooseCategory;
                                                                        case "back":
                                                                            controlNews();
                                                                        default:
                                                                            System.out.println("Do you want to add another category?(yes/no)");
                                                                            continue chooseCategory;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        article.getCategory().add(category);
                                                        CategoryController.getCategories().get(CategoryController.getCategories().indexOf(category)).getNews().add(article);
                                                        System.out.println("Article is added to " + category + " category");
                                                        for (;;) {
                                                            System.out.println("Do you want to change another attribute?(yes/no)");
                                                            switch (input.nextLine().trim().toLowerCase()) {
                                                                case "yes":
                                                                    continue chooseAttribute;
                                                                case "no":
                                                                    break updateArticle;
                                                                case "back":
                                                                    controlNews();
                                                                default:
                                                                    break;
                                                            }
                                                        }

                                                    }
                                                }
                                                if (!categoryExists) {
                                                    System.out.println("There is no category with that id");
                                                }
                                            }

                                            continue chooseAttribute;
                                        case "remove":
                                            chooseCategory:
                                            for (;;) {
                                                System.out.println();
                                                System.out.println("Categories:");
                                                System.out.println("---------------------------------- ");
                                                if (!article.getCategory().isEmpty()) {
                                                    article.getCategory().forEach(category -> System.out.println(category.getId() + " " + category));
                                                } else {
                                                    System.out.println("There is no categories yet");
                                                }
                                                System.out.println("-----------------------------------");
                                                System.out.println("Choose category id to remove");
                                                int idCategory;
                                                for (;;) {
                                                    try {
                                                        idCategory = Integer.parseInt(input.nextLine());
                                                        break;
                                                    } catch (NumberFormatException e) {
                                                        System.out.println("You have to insert number");
                                                    }
                                                }
                                                boolean categoryExists = false;
                                                for (Category category : article.getCategory()) {
                                                    if (category.getId() == idCategory) {
                                                        categoryExists = true;
                                                        article.getCategory().remove(category);
                                                        CategoryController.getCategories().get(CategoryController.getCategories().indexOf(category)).getNews().remove(article);
                                                        System.out.println("'" + category + "' is removed");
                                                        for (;;) {
                                                            System.out.println("Do you want to change another attribute?(yes/no)");
                                                            switch (input.nextLine().trim().toLowerCase()) {
                                                                case "yes":
                                                                    continue chooseAttribute;
                                                                case "no":
                                                                    break updateArticle;
                                                                case "back":
                                                                    controlNews();
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (!categoryExists) {
                                                    System.out.println("There is no category with that id");
                                                }
                                            }
                                        case "back":
                                            controlNews();
                                        default:
                                            System.out.println("Do you want to add or remove category?");
                                            break;
                                    }
                                }
                            case "author":
                                for (;;) {
                                    System.out.println("Who is author of this article?");
                                    chooseAuthor:
                                    for (;;) {
                                        System.out.println();
                                        System.out.println("---------------------------------- ");
                                        AuthorController.getAuthors().forEach(author -> System.out.println(author.getId() + " " + author));
                                        System.out.println("-----------------------------------");
                                        System.out.println("Choose author id to add");
                                        int idAuthor;
                                        AuthorController.getAuthors().get(AuthorController.getAuthors().indexOf(article.getAuthor())).getNews().remove(article);
                                        for (;;) {
                                            try {
                                                id = Integer.parseInt(input.nextLine());
                                                break;
                                            } catch (NumberFormatException e) {
                                                System.out.println("You have to insert number");
                                            }
                                        }
                                        authorExists = false;
                                        for (Author author : AuthorController.getAuthors()) {
                                            if (author.getId() == id) {
                                                authorExists = true;
                                                article.setAuthor(author);
                                                AuthorController.getAuthors().get(AuthorController.getAuthors().indexOf(article.getAuthor())).getNews().add(article);
                                                System.out.println("New author is " + author);
                                                for (;;) {
                                                    switch (input.nextLine().trim().toLowerCase()) {
                                                        case "yes":
                                                            System.out.println("What attribute do you want to update?(title/text/category/author)");
                                                            continue chooseAttribute;
                                                        case "no":
                                                            break updateArticle;
                                                        case "back":
                                                            controlNews();
                                                        default:
                                                            System.out.println("Do you want to change another attribute?(yes/no)");
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                        if (!authorExists) {
                                            System.out.println("There is no author with that id");
                                        }

                                    }

                                }
                            default:
                                break;
                        }
                    }
                }
            }
            if (!authorExists) {
                System.out.println("There is no author with that id");
                newsUpdate();
            }
        }
        for (;;) {
            System.out.println("Do you want update another author?(yes/no)");
            switch (input.nextLine().trim().toLowerCase()) {
                case "yes":
                    newsUpdate();
                case "no":
                    controlNews();
                case "back":
                    controlNews();
                default:
                    break;
            }
        }
    }

    private static void newsDelete() {
        System.out.println();
        System.out.println("Articles:");
        System.out.println("---------------------------------- ");
        if (!news.isEmpty()) {
            news.forEach(article -> System.out.println(article.getId() + " " + article.toString()));
        } else {
            System.out.println("There is no articles yet");
            controlNews();
        }
        System.out.println("-----------------------------------");
        System.out.println("Choose article to delete with id");

        deleteArticle:
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
            for (News article : news) {
                boolean articleExists = false;
                if (article.getId() == id) {
                    articleExists = true;
                    System.out.println("Article to delete " + article);
                    while (true) {
                        System.out.println("Are you sure?(yes/no)");
                        switch (input.nextLine().trim().toLowerCase()) {
                            case "yes":
                                news.remove(article);
                                break deleteArticle;
                            case "no":
                                controlNews();
                            case "back":
                                controlNews();
                            default:
                                break;
                        }
                    }
                }
                if (!articleExists) {
                    System.out.println("There is no article with that id");
                    newsDelete();
                }

            }
        }
        for (;;) {
            if (!news.isEmpty()) {
                System.out.println("Do you want delete article author?(yes/no)");
                switch (input.nextLine().trim().toLowerCase()) {
                    case "yes":
                        newsDelete();
                    case "no":
                        controlNews();
                    case "back":
                        controlNews();
                    default:
                        break;
                }
            } else {
                System.out.println("There is no article anymore");
                controlNews();
            }
        }
    }

    private static String getFormatedDate(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("EEE, d.MM.yyyy.");
        return sd.format(date);
    }

    public static List<News> getNews() {
        return news;
    }

    
}
