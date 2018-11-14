package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;

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
    private ArrayList<Post> drafts = new ArrayList<>();
    private ArrayList<Comment> comments_made = new ArrayList<>();
    private ArrayList<Comment> comments_recv = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Integer> groups = new ArrayList<>();  // id
    private ArrayList<Integer> followers = new ArrayList<>();  // id
    private ArrayList<Integer> followerings = new ArrayList<>();  // id

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
//        this.userid = 10000000 + number of users in database
    }

    public void setIcon(Bitmap bitmap) { icon = bitmap; }
    public Bitmap getIcon() { return icon; }
    public String getUsername() { return username; }
    public int getUserid() { return userid; }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public ArrayList<Post> getCollections() { return collections; }
    public ArrayList<Comment> getComments_made() { return comments_made; }
    public ArrayList<Comment> getComments_recv() { return comments_recv; }

    public Boolean isAnonymous() { return this.userid == anonymous; }
    public void getNotified(){
        // to be implemented
    }
    public void AddPost(Post post){
        posts.add(post);
    }
    public void DeletePost(Post post){
        posts.remove(post);
    }
    public void made_comment(Comment comment) { comments_made.add(comment); }
    public void recv_comment(Comment comment) { comments_recv.add(comment); }
    public void delete_comment(Comment comment, Boolean made) {
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
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static User fromJson(String json){
        return (new Gson()).fromJson(json, User.class);
    }
    public static User findUser(int id){
        // to be implemented
        return new User("Moments Developer","haitianluo@ucsb.edu","moments");
    }
}
