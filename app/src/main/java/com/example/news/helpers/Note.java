package com.example.news.helpers;

import java.util.List;

public class Note {
    private String name;
    private String description;
    private List<String> tags;
    private String date;
    private int id;

    public Note(final String name,
                final String description,
                final List<String> tags,
                final String date,
                final int id) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
