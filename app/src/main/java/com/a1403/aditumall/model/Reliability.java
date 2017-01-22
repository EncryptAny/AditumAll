package com.a1403.aditumall.model;

/**
 * Created by dakfu on 1/21/2017.
 */

public class Reliability {


    private int upVotes;
    private int downVotes;

    public Reliability(int upVotes, int downVotes) {
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }


}
enum aiType{
    HAS_EPI_PENS,
    HAS_AED,
    BATHROOMS,

}
