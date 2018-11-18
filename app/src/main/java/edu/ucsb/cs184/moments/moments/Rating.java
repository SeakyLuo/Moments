package edu.ucsb.cs184.moments.moments;

import com.google.gson.Gson;

import java.util.Date;

public class Rating {
    private String userid;
    private int rating;
    private Date date;
    public Rating(String userid, int rating, Date date){
        this.userid = userid;
        this.rating = rating;
        this.date = date;
    }
    public Boolean isAnonymous() { return userid == User.anonymous; }
    public String getUserid() { return userid; }
    public int getRating() { return rating; }
    public Date getDate() { return date; }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Rating fromJson(String json){
        return (new Gson()).fromJson(json, Rating.class);
    }

}
