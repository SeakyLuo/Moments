package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class User {
    public static final String anonymous = "anonymous";
    public static User user;
    public static FirebaseUser firebaseUser;
    private String userid;
    private String username;
    private String email;
    private Bitmap icon;
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Post> collections = new ArrayList<>();
    private ArrayList<Post> drafts = new ArrayList<>();
    private ArrayList<Comment> comments_made = new ArrayList<>();
    private ArrayList<Comment> comments_recv = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();  // id
    private ArrayList<String> followers = new ArrayList<>();  // id
    private ArrayList<String> following = new ArrayList<>();  // id

    public User(String uid, String username, String email){
        this.username = username;
        this.email = email;
        this.userid = uid;
    }

    public Bitmap getIcon() { return icon; }
    public String getUsername() { return username; }
    public String getUserid() { return userid; }
    public ArrayList<Message> getMessages() { return messages; }
    public ArrayList<String> getGroups() { return groups; }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public ArrayList<Post> getCollections() { return collections; }
    public ArrayList<Comment> getComments_made() { return comments_made; }
    public ArrayList<Comment> getComments_recv() { return comments_recv; }
    public Boolean isAnonymous() { return userid.equals(anonymous); }

    public void getNotified(){
        // to be implemented
    }
    public void setIcon(Bitmap icon){
        this.icon = icon;
        upload("icon", icon);
    }
    public void AddPost(Post post){
        posts.add(post);
        upload("posts", posts);
    }
    public void DeletePost(Post post){
        posts.remove(post);
        upload("posts", posts);
    }
    public void made_comment(Comment comment) {
        comments_made.add(comment);
        upload("comments_made", comments_made);
    }
    public void recv_comment(Comment comment) {
        comments_recv.add(comment);
        upload("comments_recv", comments_recv);
    }
    public void delete_comment(Comment comment, Boolean made) {
    }
    public void follow(String userid) {
        following.add(userid);
        // add the followed user's post to timeline
    }
    public void unfollow(String userid) {
        following.remove(userid);
        // remove followed user's post to timeline
    }
    public void joinGroup(String groupid){
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
    private void upload(String key, Object value){
        FirebaseHelper.updateUser(key, value);
    }
    public ArrayList<Post> Timeline(int count){
        ArrayList<Post> timeline = new ArrayList<>();
        for (String id : following){
            ArrayList<Post> fPosts = findUser(id).posts;
        }
        return timeline;
    }
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static User fromJson(String json){
        return (new Gson()).fromJson(json, User.class);
    }
    public static User findUser(String id){
        if (id == User.user.getUserid()) return User.user;
        return FirebaseHelper.findUser(id);
    }
}
