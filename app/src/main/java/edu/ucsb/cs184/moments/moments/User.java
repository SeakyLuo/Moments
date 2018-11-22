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
    private String intro = "";
    private Bitmap icon;
    private String gender = "Unknown";
    private ArrayList<Post> posts = new ArrayList<>();
    private ArrayList<Post> drafts = new ArrayList<>();
    private ArrayList<Post.Key> collections = new ArrayList<>();
    private ArrayList<Comment> comments_made = new ArrayList<>();
    private ArrayList<Comment.Key> comments_recv = new ArrayList<>();
    private ArrayList<Rating> ratings_made = new ArrayList<>();
    private ArrayList<Rating.Key> ratings_recv = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();  // id
    private ArrayList<String> followers = new ArrayList<>();  // id
    private ArrayList<String> following = new ArrayList<>();  // id
    private ArrayList<Comment.Key> comments_notification = new ArrayList<>();
    private ArrayList<Post.Key> posts_notification = new ArrayList<>();
    private ArrayList<Rating.Key> ratings_notification = new ArrayList<>();
    private ArrayList<Post.Key> timeline = new ArrayList<>();

    public User(){}

    public User(String userid, String username){
        this.username = username;
        this.userid = userid;
    }

    public Bitmap getIcon() { return icon; }
    public String getUsername() { return username; }
    public String getUserid() { return userid; }
    public String getIntro() { return intro; }
    public String getGender() { return gender; }
    public ArrayList<Message> getMessages() { return messages; }
    public ArrayList<String> getGroups() { return groups; }
    public ArrayList<Post> getPosts() {
        return posts;
    }
    public ArrayList<Post> getDrafts() { return drafts; }
    public ArrayList<Post.Key> getCollections() { return collections; }
    public ArrayList<Comment> getComments_made() { return comments_made; }
    public ArrayList<String> getFollowers() { return followers; }
    public ArrayList<String> getFollowing() { return following; }
    public ArrayList<Comment> getComments_recv() {
        ArrayList<Comment> comments = new ArrayList<>();
        for (Comment.Key key : comments_recv){
            comments.add(Comment.findComment(key));
        }
        return comments;
    }
    public boolean isAnonymous() { return userid.equals(anonymous); }
    public boolean isFollowing(String userid) { return following.contains(userid); }
    public boolean hasNewPost() { return posts_notification.size() != 0; }
    public boolean hasNewComment() { return comments_notification.size() != 0; }
    public boolean hasNewRating() { return ratings_notification.size() != 0; }
    public ArrayList getPostKeys() {
        ArrayList<Post.Key> keys = new ArrayList<>();
        for (Post post: posts) keys.add(post.getKey());
        return keys;
    }
    public ArrayList getTimeline(){
        ArrayList<Post> post_timeline = new ArrayList<>();
        for (Post.Key key : timeline){
            post_timeline.add(Post.findPost(key));
        }
        return post_timeline;
    }
    public void PostNotification(Post post, boolean remove){
        if (remove && posts_notification.contains(post.getKey())) posts_notification.remove(post.getKey());
        else posts_notification.add(0, post.getKey());
        upload("posts_notification", posts_notification);
    }
    public void CommentNotification(Comment comment, boolean remove){
        if (remove && comments_notification.contains(comment.getKey())) comments_notification.remove(comment.getKey());
        else comments_notification.add(0, comment.getKey());
        upload("comments_notification", comments_notification);
    }
    public void RatingNotification(Rating rating){
        boolean update = false;
        for (Rating.Key key : (ArrayList<Rating.Key>) ratings_notification.clone()){
            if (rating.getKey().equals(key)){
                if (rating.getRating() == 0) ratings_notification.remove(key);
                else ratings_notification.set(ratings_notification.indexOf(key), rating.getKey());
                update = true;
                break;
            }
        }
        if(!update){
            ratings_notification.add(0, rating.getKey());
        }
        upload("ratings_notification", comments_notification);
    }
    public void FollowerNotification(String followerid, boolean remove){
        if (remove) followers.remove(followerid);
        else followers.add(followerid);
        upload("followers", followers);
    }
    public void refreshTimeline(){
        timeline.addAll(0, posts_notification);
        Collections.sort(timeline, new Post.TimeComparator());
        posts_notification.clear();
        upload("posts_notification", posts_notification);
        upload("timeline", timeline);
    }
    public void refreshGroups(){

    }
    public void refreshCommentsRecv(){
        comments_recv.addAll(comments_notification);
        Collections.sort(comments_recv, new Comment.TimeComparator());
        comments_notification.clear();
        upload("comments_notification", comments_notification);
        upload("comments_recv", comments_recv);
    }
    public void refreshRatingsRecv(){
        ratings_recv.addAll(ratings_notification);
        Collections.sort(ratings_recv, new Rating.TimeComparator());
        ratings_notification.clear();
        upload("ratings_notification", ratings_notification);
        upload("ratings_recv", ratings_recv);
    }
    public void setUsername(String username){
        this.username = username;
        upload("username", username);
    }
    public void setIcon(Bitmap icon){
        this.icon = icon;
        upload("icon", icon);
    }
    public void setIntro(String intro){
        this.intro = intro;
        upload("intro", intro);
    }
    public void setGender(String gender){
        this.gender = gender;
        upload("gender", gender);
    }
    public void make_post(Post post){
        posts.add(post);
        upload("posts", posts);
        for (String id : followers){
            findUser(id).PostNotification(post, false);
        }
    }
    public void delete_post(Post post){
        posts.remove(post);
        upload("posts", posts);
        for (String id : followers){
            findUser(id).PostNotification(post, true);
        }
    }
    public void make_comment(Comment comment) {
        comments_made.add(comment);
        FirebaseHelper.findPost(comment.getPostKey()).addComment(comment);
        upload("comments_made", comments_made);
        findUser(comment.getUserid()).CommentNotification(comment, false);
    }
    public void delete_comment(Comment comment, boolean made) {
        comments_made.remove(comment);
        FirebaseHelper.findPost(comment.getPostKey()).removeComment(comment);
        upload("comments_made", comments_made);
        findUser(comment.getUserid()).CommentNotification(comment, true);
    }
    public void follow(final String userid) {
        following.add(userid);
        User user = findUser(userid);
        timeline.addAll(user.getPostKeys());
        user.FollowerNotification(user.userid, false);
        Collections.sort(timeline, new Post.TimeComparator());
        upload("following", following);
        upload("timeline", timeline);
    }
    public void unfollow(final String userid) {
        following.remove(userid);
        User user = findUser(userid);
        for (Post.Key postKey : (ArrayList<Post.Key>) timeline.clone()){
            if (postKey.userid.equals(userid)) timeline.remove(postKey);
        }
        user.FollowerNotification(user.userid, true);
        upload("following", following);
        upload("timeline", timeline);
    }
    public void joinGroup(String groupid){
        groups.add(groupid);
        upload("groups", groups);
        FirebaseHelper.findGroup(groupid).addMember(user.userid);
    }
    public void quitGroup(String groupid){
        groups.remove(groupid);
        upload("groups", groups);
        FirebaseHelper.findGroup(groupid).removeMember(user.userid);
    }
    public void collect(Post post){
        collections.add(post.getKey());
        upload("collections", collections);
    }
    public void uncollect(Post post){
        collections.remove(post.getKey());
        upload("collections", collections);
    }
    public void rate(Rating rating){
        boolean remove = rating.getRating() == 0;
        findUser(rating.getUserid()).RatingNotification(rating);
        if (remove) FirebaseHelper.findPost(rating.getPostKey()).removeRating(rating);
        else FirebaseHelper.findPost(rating.getPostKey()).addRating(rating);
    }
    public void saveAsDraft(Post post){
        drafts.add(post);
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
