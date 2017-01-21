package com.a1403.aditumall.model;

public class AccessibilityPoint {
    private String name;
    private int upvotes;
    private int downvotes;

    public AccessibilityPoint(String name, int upvotes, int downvotes) {
        this.name = name;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public String getName() {
        return name;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void incUpvotes() {
        this.upvotes = upvotes + 1;
    }

    public void incDownvotes() {
        this.downvotes = downvotes + 1;
    }

    public int getScore() {
        return this.upvotes - this.downvotes;
    }
}
