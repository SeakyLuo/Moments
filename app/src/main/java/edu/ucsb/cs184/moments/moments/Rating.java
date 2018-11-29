package edu.ucsb.cs184.moments.moments;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Comparator;
import java.util.Date;

public class Rating {
    private String userid;  //rater
    private int rating;
    private Date time;
    private Post.Key postKey;
    public Rating(String userid, int rating, Date time, Post.Key postKey){
        this.userid = userid;
        this.rating = rating;
        this.time = time;
        this.postKey = postKey;
    }
    public Boolean isAnonymous() { return userid.equals(User.ANONYMOUS); }
    public String getUserid() { return userid; }
    public int getRating() { return rating; }
    public Date getTime() { return time; }
    public Post.Key getPostKey() {
        return postKey;
    }
    public Key getKey(){ return new Key(userid, time.getTime(), postKey); }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Rating fromJson(String json){
        return (new Gson()).fromJson(json, Rating.class);
    }
    public class Key{
        String userid;
        Long time;
        Post.Key postKey;
        public Key(String userid, Long time, Post.Key postKey){
            this.userid = userid;
            this.time = time;
            this.postKey = postKey;
        }
        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof Key))
                return false;
            Key k = (Key) obj;
            return postKey.equals(k.postKey) && userid == k.userid;
        }
    }
    public static class TimeComparator implements Comparator<Key> {
        @Override
        public int compare(Key o1, Key o2) {
            return Long.compare(o1.time, o2.time) ;
        }
    }
}
