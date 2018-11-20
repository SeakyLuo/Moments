package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class User {
    public static final String anonymous = "anonymous";
    public static User user;
    public static FirebaseUser firebaseUser;
    private String userid;
    private String username;
    private Bitmap icon;
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Post> drafts = new ArrayList<>();
    private ArrayList<Post.Key> collections = new ArrayList<>();
    private ArrayList<Comment> comments_made = new ArrayList<>();
    private ArrayList<Comment.Key> comments_recv = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();  // id
    private ArrayList<String> followers = new ArrayList<>();  // id
    private ArrayList<String> following = new ArrayList<>();  // id
    private ArrayList<Comment.Key> comments_notification = new ArrayList<>();
    private ArrayList<Post.Key> posts_notification = new ArrayList<>();
    private ArrayList<Rating.Key> ratings_notification = new ArrayList<>();
    private ArrayList<Post.Key> timeline = new ArrayList<>();

    public User(){}

    public User(String uid, String username){
        this.username = username;
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
    public ArrayList<Post.Key> getCollections() { return collections; }
    public ArrayList<Comment> getComments_made() { return comments_made; }
    public ArrayList<Comment> getComments_recv() {
        ArrayList<Comment> comments = new ArrayList<>();
        for (Comment.Key key : comments_recv){
            comments.add(Comment.findComment(key));
        }
        return comments;
    }
    public Boolean isAnonymous() { return userid.equals(anonymous); }
    public Boolean hasNewPost() { return posts_notification.size() != 0; }
    public Boolean hasNewComment() { return comments_notification.size() != 0; }
    private Boolean hasNewRating() { return ratings_notification.size() != 0; }
    public ArrayList getPostKeys() {
        ArrayList<Post.Key> keys = new ArrayList<>();
        for (Post post: posts) keys.add(post.getKey());
        return keys;
    }
    public ArrayList getTimeline(int count){
        ArrayList<Post> post_timeline = new ArrayList<>();
        for (Post.Key key : timeline){
            post_timeline.add(Post.findPost(key));
        }
        return post_timeline;
    }
    public void PostNotification(Post post){
        posts_notification.add(0, post.getKey());
        upload("posts_notification", posts_notification);
    }
    public void CommentNotification(Comment comment){
        comments_notification.add(0, comment.getKey());
        upload("comments_notification", comments_notification);
    }
    public void RatingNotification(Rating rating){
        boolean update = false;
        for (Rating.Key key : (ArrayList<Rating.Key>) ratings_notification.clone()){
            if (key.userid == rating.getUserid() && key.postKey == rating.getPostKey()){
                ratings_notification.set(ratings_notification.indexOf(key), rating.getKey());
                update = true;
                break;
            }
        }
        if(!update){
            ratings_notification.add(0, rating.getKey());
        }
        upload("comments_notification", comments_notification);
    }
    public void RefreshTimeline(){
        timeline.addAll(0, posts_notification);
        Collections.sort(timeline, new Post.TimeComparator());
        posts_notification.clear();
        upload("posts_notification", posts_notification);
        upload("timeline", timeline);
    }
    public void RefreshCommentsRecv(){
        comments_recv.addAll(comments_notification);
        Collections.sort(comments_recv, new Comment.TimeComparator());
        comments_notification.clear();
        upload("comments_notification", comments_notification);
        upload("comments_recv", comments_recv);
    }
    public void setIcon(Bitmap icon){
        this.icon = icon;
        upload("icon", icon);
    }
    public void make_post(Post post){
        posts.add(post);
        upload("posts", posts);
        for (String id : followers){
            findUser(id).PostNotification(post);
        }
    }
    public void delete_post(Post post){
        posts.remove(post);
        upload("posts", posts);
    }
    public void made_comment(Comment comment) {
        comments_made.add(comment);
        upload("comments_made", comments_made);
        findUser(comment.getUserid()).CommentNotification(comment);
    }
    public void delete_comment(Comment comment, Boolean made) {
    }
    public void follow(String userid) {
        following.add(userid);
        timeline.addAll(findUser(userid).getPostKeys());
        Collections.sort(timeline, new Post.TimeComparator());
        upload("following", following);
        upload("timeline", timeline);
    }
    public void unfollow(String userid) {
        following.remove(userid);
        for (Post.Key postKey : (ArrayList<Post.Key>) timeline.clone()){
            if (postKey.userid == userid) timeline.remove(postKey);
        }
        upload("following", following);
        upload("timeline", timeline);
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
        collections.add(post.getKey());
        upload("collections", collections);
    }
    public void uncollect(Post post){
        collections.remove(post.getKey());
        upload("collections", collections);
    }
    private void upload(String key, Object value){
        FirebaseHelper.updateUser(key, value);
    }

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static User fromJson(String json){
        return (new Gson()).fromJson(json, User.class);
    }
    public static User findUser(String id){
        if (User.user != null && id == User.user.getUserid()) return User.user;
        return FirebaseHelper.findUser(id);
    }
}
