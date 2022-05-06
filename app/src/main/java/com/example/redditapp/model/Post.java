package com.example.redditapp.model;

import java.time.LocalDate;

public class Post {
    private String title;
    private String text;
    private LocalDate creationDate;
    private String imagePath;

    public Post() {
    }

    public Post(String title, String text, LocalDate creationDate, String imagePath) {
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.imagePath = imagePath;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", creationDate=" + creationDate +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
