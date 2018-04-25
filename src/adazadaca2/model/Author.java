/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adazadaca2.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avukelic;
 */
public class Author {

    private int id;
    private String name;
    private String lastname;
    private List<News> news = new ArrayList<>();

    public Author(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName() + " " + getLastname();
    }
    
}
