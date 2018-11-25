package edu.ucsb.cs184.moments.moments;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Post implements Serializable {
    private String userid;
    private String content;
    private Date time;

    private ArrayList<Rating> ratings = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    public Post(String userid, String content, Date time){
        this.userid = userid;
        this.content = content;
        this.time = time;
    }

    public Key getKey() { return new Key(userid, time.getTime()); }
    public String getUserid() { return userid; }
    public String getContent() { return content; }
    public Date getTime() { return time; }
    public int comments_received() { return comments.size(); }
    public ArrayList<Comment> getComments() { return  comments; }
    public int ratings_received() { return ratings.size(); }
    public float ratings_avg(){
        float sum = 0;
        int count = ratings.size();
        if (count == 0) return 0f;
        for (int i = 0; i < count; i++)
            sum += ratings.get(i).getRating();
        return sum / count;
    }
    public Boolean isAnonymous() { return userid.equals(User.anonymous); }
    public Boolean isCollected(String userid) {
        ArrayList<Key> keys = User.findUser(userid).getCollections();
        for (Key key : keys) if (key.userid == userid) return true;
        return false;
    }
    public void addComment(Comment comment) { comments.add(comment); }
    public void addRating(Rating rating) { ratings.add(rating); }
    public void removeComment(Comment comment) { comments.remove(comment); }
    public void removeRating(Rating rating) { ratings.remove(rating); }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Post fromJson(String json){
        return (new Gson()).fromJson(json, Post.class);
    }
    public static Post findPost(Key key) { return FirebaseHelper.findPost(key); }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Post))
            return false;
        Post p = (Post) obj;
        return p.getKey().equals(((Post) obj).getKey());
    }

    public class Key {
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

    public static class RatingComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return Double.compare(o1.ratings_avg(), o2.ratings_avg());
        }
    }
}
