package edu.ucsb.cs184.moments.moments;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class Comment {
    private String userid;
    private String content;
    private Date date;
    private Comment parent = null;

    private ArrayList<Comment> replies = new ArrayList<>();
    private ArrayList<Rating> ratings = new ArrayList<>();

    public Comment(String userid, String content, Date date){
        this.userid = userid;
        this.content = content;
        this.date = date;
    }
    public Comment(String userid, String content, Date date, Comment parent){
        this.userid = userid;
        this.content = content;
        this.date = date;
        this.parent = parent;
    }

    public int replies_received() { return replies.size(); }
    public ArrayList<Comment> getComments() { return  replies; }
    public int ratings_received() { return ratings.size(); }
    public float ratings_avg(){
        float sum = 0;
        int count = ratings.size();
        if (count == 0) return 0f;
        for (int i = 0; i < count; i++)
            sum += ratings.get(i).getRating();
        return sum / count;
    }
    public String getUserid() { return userid; }
    public String getContent() { return content; }
    public Date getDate() { return date; }
    public Comment getParent() { return parent; }
    public Boolean hasParent() { return parent == null; }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Comment fromJson(String json){
        return (new Gson()).fromJson(json, Comment.class);
    }
    public static Comment TestComment(){ return new Comment("test", new Date().toString(), new Date());}
}
