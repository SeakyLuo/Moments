package edu.ucsb.cs184.moments.moments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Comment implements Parcelable {
    private String userid; // Commentator
    private String content;
    private Long time;
    private Post.Key postKey;
    private Comment.Key parent;

    private ArrayList<Comment> replies = new ArrayList<>();
    private ArrayList<Rating> ratings = new ArrayList<>();

    public Comment(){}
    public Comment(String userid, String content, Long time, Post.Key postKey){
        this.userid = userid;
        this.content = content;
        this.time = time;
        this.postKey = postKey;
    }
    public Comment(String userid, String content, Long time, Comment.Key parent){
        this.userid = userid;
        this.content = content;
        this.time = time;
        this.parent = parent;
    }

    protected Comment(Parcel in) {
        userid = in.readString();
        content = in.readString();
        time = in.readLong();
        // TODO read postkey or parent
        replies = in.createTypedArrayList(Comment.CREATOR);
        ratings = in.createTypedArrayList(Rating.CREATOR);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

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
    public Date getTime() { return new Date(time); }
    public Key getKey() { return new Key(userid, time); }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(content);
        dest.writeLong(time);
        dest.writeTypedList(replies);
        dest.writeTypedList(ratings);
    }

    public static class Key implements Parcelable{
        String userid;
        Long time;
        public Key() {}
        public Key(String userid, Long time){
            this.userid = userid;
            this.time = time;
        }

        protected Key(Parcel in) {
            userid = in.readString();
            time = in.readLong();
        }

        public static final Creator<Key> CREATOR = new Creator<Key>() {
            @Override
            public Key createFromParcel(Parcel in) {
                return new Key(in);
            }

            @Override
            public Key[] newArray(int size) {
                return new Key[size];
            }
        };

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof Key))
                return false;
            Key k = (Key) obj;
            return time.equals(k.time) && userid.equals(k.userid);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userid);
            dest.writeLong(time);
        }
    }
    public static class TimeComparator implements Comparator<Key> {
        @Override
        public int compare(Key o1, Key o2) {
            return Long.compare(o1.time, o2.time) ;
        }
    }
}
