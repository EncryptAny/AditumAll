package com.a1403.aditumall.model;

public class AccessibilityPoint {
    private String name;
    private int upvotes; //BACKEND
    private boolean upvoted;
    private int downvotes; //BACKEND
    private double lat;
    private double lon;

    public enum VoteStatus {
        NONE,
        UPVOTED,
        DOWNVOTED
    }

    private VoteStatus voteStatus;

    public AccessibilityPoint(String name, int upvotes, int downvotes, double lat, double lon) {
        this.name = name;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.voteStatus = VoteStatus.NONE;
        this.lat = lat;
        this.lon = lon;
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

    public VoteStatus getVoteStatus() { return this.voteStatus; }

    public boolean setVoteStatus(VoteStatus voteStatus) {
        if (this.voteStatus == voteStatus) {
            this.voteStatus = VoteStatus.NONE;
            return false;
        }
        else {
            this.voteStatus = voteStatus;
            return true;
        }
    }

    public int getScore() {
        if (this.voteStatus == VoteStatus.UPVOTED) return this.upvotes - this.downvotes + 1;
        else if (this.voteStatus == VoteStatus.DOWNVOTED) return this.upvotes - this.downvotes - 1;
        else return this.upvotes - this.downvotes;
    }
}
