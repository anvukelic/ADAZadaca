/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author avukelic;
 */
public class News {
    
    private int id;
    private String title;
    private String text;
    private List<Category> category = new ArrayList<>();
    private Author author;
    private Date date;

    public News(int id) {
        this.id = id;
    }  

    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }   

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getTitle();
    }
    
    
    
    
}
