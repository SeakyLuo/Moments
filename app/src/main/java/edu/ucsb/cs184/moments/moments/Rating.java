package edu.ucsb.cs184.moments.moments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Comparator;

public class Rating implements Parcelable {
    private String userid;  //rater
    private int rating;
    private Long time;
    private Post.Key postKey;
    public Rating(){}
    public Rating(String userid, int rating, Long time, Post.Key postKey){
        this.userid = userid;
        this.rating = rating;
        this.time = time;
        this.postKey = postKey;
    }

    protected Rating(Parcel in) {
        userid = in.readString();
        rating = in.readInt();
        time = in.readLong();
        postKey = in.readParcelable(Post.Key.class.getClassLoader());
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    public Boolean IsAnonymous() { return userid.equals(User.ANONYMOUS); }
    public String getUserid() { return userid; }
    public int getRating() { return rating; }
    public Long getTime() { return time; }
    public Post.Key GetPostKey() {
        return postKey;
    }
    public Key GetKey(){ return new Key(userid, time, postKey); }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Post))
            return false;
        return GetKey().equals(((Rating) obj).GetKey());
    }
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Rating fromJson(String json){
        return (new Gson()).fromJson(json, Rating.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeInt(rating);
        dest.writeLong(time);
        dest.writeParcelable(postKey, 0);
    }

    public static class Key implements Parcelable{
        String userid;
        Long time;
        Post.Key postKey;
        public Key() {}
        public Key(String userid, Long time, Post.Key postKey){
            this.userid = userid;
            this.time = time;
            this.postKey = postKey;
        }

        protected Key(Parcel in) {
            userid = in.readString();
            time = in.readLong();
            postKey = in.readParcelable(Post.Key.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userid);
            dest.writeLong(time);
            dest.writeParcelable(postKey, flags);
        }

        @Override
        public int describeContents() {
            return 0;
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
            return postKey.equals(k.postKey) && userid.equals(k.userid);
        }
    }
    public static class TimeComparator implements Comparator<Key> {
        @Override
        public int compare(Key o1, Key o2) {
            return Long.compare(o2.time, o1.time) ;
        }
    }
}
