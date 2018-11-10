package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class User {
    public static final int anonymous = 0;
    private int userid;
    private String username;
    private String email;
    private String password;
    private Bitmap icon;
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Post> collections = new ArrayList<>();
    private ArrayList<Integer> groups = new ArrayList<>();  // id
    private ArrayList<Integer> followers = new ArrayList<>();  // id
    private ArrayList<Integer> followerings = new ArrayList<>();  // id

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
        // generates a random user id
    }

    public void setIcon(Bitmap bitmap) { icon = bitmap; }
    public Bitmap getIcon() { return icon; }
    public String getUsername() { return username; }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public ArrayList<Post> getCollections() { return collections; }

    public void AddPost(Post post){
        posts.add(post);
    }
    public void DeletePost(Post post){
        posts.remove(post);
    }
    public void follow(int userid) {
        followerings.add(userid);
        // add the followed user's post to timeline
    }
    public void unfollow(int userid) {
        followerings.remove(userid);
        // remove followed user's post to timeline
    }
    public void joinGroup(int groupid){
        groups.add(groupid);
        //  add to group database
    }
    public void quitGroup(int groupid){
        groups.remove(groupid);
        //  remove from group database
    }
    public void collect(Post post){
        collections.add(post);
    }
    public void uncollect(Post post){
        collections.remove(post);
    }
    public static User findUser(int id){
        // to be implemented
        return new User("Seaky","haitianluo@ucsb.edu","Seaky");
    }
}
