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
public class Category {
    
    private int id ;
    private String name;
    private List<News> news = new ArrayList<>();

    public Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
   
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    
    
        
}
