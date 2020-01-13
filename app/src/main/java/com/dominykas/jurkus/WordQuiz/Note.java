package com.dominykas.jurkus.WordQuiz;

public class Note {
    private String title;
    private int priority;

    public Note() {
        //empty constructor needed
    }

    public Note(String title, String description, int priority) {
        this.title = title;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }
}
