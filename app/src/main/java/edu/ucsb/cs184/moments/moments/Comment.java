package edu.ucsb.cs184.moments.moments;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Comment {
    private String userid; // Commentator
    private String content;
    private Date time;
    private Post.Key postKey;
    private Comment.Key parent;

    private ArrayList<Comment> replies = new ArrayList<>();
    private ArrayList<Rating> ratings = new ArrayList<>();

    public Comment(String userid, String content, Date time, Post.Key postKey){
        this.userid = userid;
        this.content = content;
        this.time = time;
        this.postKey = postKey;
    }
    public Comment(String userid, String content, Date time, Comment.Key parent){
        this.userid = userid;
        this.content = content;
        this.time = time;
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
    public Date getTime() { return time; }
    public Key getKey() { return new Key(userid, time.getTime()); }
    public Post.Key getPostKey() { return postKey; }
    public Comment.Key getParent() { return parent; }
    public Boolean hasParent() { return parent == null; }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Comment fromJson(String json){
        return (new Gson()).fromJson(json, Comment.class);
    }
    public static Comment findComment(Key key){
        return FirebaseHelper.findComment(key);
    }
    public class Key{
        String userid;
        Long time;
        public Key(String userid, Long time){
            this.userid = userid;
            this.time = time;
        }
        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof Key))
                return false;
            Key k = (Key) obj;
            return time == k.time && userid == k.userid;
        }
    }
    public static class TimeComparator implements Comparator<Key> {
        @Override
        public int compare(Key o1, Key o2) {
            return Long.compare(o1.time, o2.time) ;
        }
    }
}
