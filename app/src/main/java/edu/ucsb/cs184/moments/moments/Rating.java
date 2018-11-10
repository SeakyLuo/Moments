package edu.ucsb.cs184.moments.moments;

import java.sql.Time;

public class Rating {
    private int userid;
    private int rating;
    private Time time;
    public Rating(int userid, int rating, Time time){
        this.userid = userid;
        this.rating = rating;
        this.time = time;
    }
    public Boolean isAnonymous() { return userid == User.anonymous; }
    public int getUserid() { return userid; }
    public int getRating() { return rating; }
    public Time getTime() { return time; }
}
