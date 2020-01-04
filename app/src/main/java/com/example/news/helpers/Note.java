package com.example.news.helpers;

import java.util.List;

public class Note {
    private String name;
    private String description;
    private List<String> tags;
    private String date;

    public Note(final String name,
                final String description,
                final List<String> tags,
                final String date) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.date = date;
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
}
