package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class User implements Parcelable {
    public static final String ANONYMOUS = "ANONYMOUS";
    public static User user;
    public static FirebaseUser firebaseUser;
    private String id;
    private String name;
    private String intro = "";
    private Bitmap icon;
    private int user_number = 0;
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
    private ArrayList<String> search_history = new ArrayList<>();

    public User(){}

    public User(String id, String name){
        this.name = name;
        this.id = id;
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        intro = in.readString();
        icon = in.readParcelable(Bitmap.class.getClassLoader());
        user_number = in.readInt();
        gender = in.readString();
        posts = in.createTypedArrayList(Post.CREATOR);
        drafts = in.createTypedArrayList(Post.CREATOR);
        collections = in.createTypedArrayList(Post.Key.CREATOR);
        comments_made = in.createTypedArrayList(Comment.CREATOR);
        comments_recv = in.createTypedArrayList(Comment.Key.CREATOR);
        ratings_made = in.createTypedArrayList(Rating.CREATOR);
        ratings_recv = in.createTypedArrayList(Rating.Key.CREATOR);
        groups = in.createStringArrayList();
        followers = in.createStringArrayList();
        following = in.createStringArrayList();
        comments_notification = in.createTypedArrayList(Comment.Key.CREATOR);
        posts_notification = in.createTypedArrayList(Post.Key.CREATOR);
        ratings_notification = in.createTypedArrayList(Rating.Key.CREATOR);
        timeline = in.createTypedArrayList(Post.Key.CREATOR);
        search_history = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setNumber(int number){ this.user_number = number; }
    public int getNumber() { return user_number; }
    public Bitmap getIcon() { return icon; }
    public String getName() { return name; }
    public String getId() { return id; }
    public String getIntro() { return intro; }
    public String getGender() { return gender; }
    public ArrayList<Message> getMessages() { return messages; }
    public ArrayList<Group> getGroups() {
        ArrayList<Group> data = new ArrayList<>();
        for (String gid: groups) data.add(Group.findGroup(gid));
        return data;
    }
    public ArrayList<Post> getPosts() { return posts; }
    public ArrayList<Post> getDrafts() { return drafts; }
    public ArrayList<Post> getCollections() {
        ArrayList<Post> data = new ArrayList<>();
        for (Post.Key key: collections){
            Post post = Post.findPost(key);
            // Should let user know it's not found/deleted
            if (post != null)
                data.add(post);
        }
        return data;
    }
    public ArrayList<Post.Key> getCollectionKeys(){
        return collections;
    }
    public ArrayList<Comment> getComments_made() { return comments_made; }
    public ArrayList<String> getFollowers() { return followers; }
    public ArrayList<String> getFollowing() { return following; }
    public ArrayList<String> getSearchHistory() { return search_history; }
    public ArrayList<Comment> getComments_recv() {
        ArrayList<Comment> data = new ArrayList<>();
        for (Comment.Key key: comments_recv){
            Comment comment = Comment.findComment(key);
            if (comment != null)
                data.add(comment);
        }
        return data;
    }
    public boolean isAnonymous() { return id.equals(ANONYMOUS); }
    public boolean inGroup(String groupid) { return groups.contains(groupid); }
    public boolean isFollowing(String userid) { return following.contains(userid); }
    public boolean hasNewPost() { return posts_notification.size() != 0; }
    public boolean hasNewComment() { return comments_notification.size() != 0; }
    public boolean hasNewRating() { return ratings_notification.size() != 0; }
    public ArrayList<Post.Key> getPostKeys() {
        ArrayList<Post.Key> data = new ArrayList<>();
        for (Post post: posts) data.add(post.getKey());
        return data;
    }
    public ArrayList<Post> getTimeline(){
        ArrayList<Post> data = new ArrayList<>();
        for (Post.Key key: timeline){
            Post post = Post.findPost(key);
            if (post != null)
                data.add(post);
        }
        return data;
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
        for (Rating.Key key: (ArrayList<Rating.Key>) ratings_notification.clone()){
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
        else followers.add(0, followerid);
        upload("followers", followers);
    }
    public void refreshTimeline(){
        timeline.addAll(posts_notification);
        Collections.sort(timeline, new Post.TimeComparator());
        upload("timeline", timeline);
        posts_notification.clear();
        upload("posts_notification", posts_notification);
    }
    public void refreshGroups(){

    }
    public void refreshCommentsRecv(){
        comments_recv.addAll(comments_notification);
        Collections.sort(comments_recv, new Comment.TimeComparator());
        upload("comments_recv", comments_recv);
        comments_notification.clear();
        upload("comments_notification", comments_notification);
    }
    public void refreshRatingsRecv(){
        ratings_recv.addAll(ratings_notification);
        Collections.sort(ratings_recv, new Rating.TimeComparator());
        upload("ratings_recv", ratings_recv);
        ratings_notification.clear();
        upload("ratings_notification", ratings_notification);
    }
    public void setName(String name){
        this.name = name;
        upload("name", name);
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
    public void addHistory(String content){
        search_history.add(0, content);
        upload("search_history", search_history);
    }
    public void removeHistory(String content){
        search_history.remove(content);
        upload("search_history", search_history);
    }
    public void make_post(Post post){
        posts.add(0, post);
        upload("posts", posts);
        timeline.add(0, post.getKey());
        upload("timeline", timeline);
        for (String id: followers){
            findUser(id).PostNotification(post, false);
        }
    }
    public void remove_post(Post post){
        posts.remove(post);
        upload("posts", posts);
        timeline.remove(post.getKey());
        upload("timeline", timeline);
        for (String id: followers){
            findUser(id).PostNotification(post, true);
        }
    }
    public void make_comment(Comment comment) {
        comments_made.add(0, comment);
        Post.findPost(comment.getPostKey()).addComment(comment);
        upload("comments_made", comments_made);
        findUser(comment.getUserid()).CommentNotification(comment, false);
    }
    public void delete_comment(Comment comment, boolean made) {
        comments_made.remove(comment);
        Post.findPost(comment.getPostKey()).removeComment(comment);
        upload("comments_made", comments_made);
        findUser(comment.getUserid()).CommentNotification(comment, true);
    }
    public void follow(final String userid) {
        following.add(0, userid);
        upload("following", following);
        User user = findUser(userid);
        user.FollowerNotification(User.user.id, false);
        timeline.addAll(user.getPostKeys());
        Collections.sort(timeline, new Post.TimeComparator());
        upload("timeline", timeline);
    }
    public void unfollow(final String userid) {
        following.remove(userid);
        upload("following", following);
        for (Post.Key postKey: (ArrayList<Post.Key>) timeline.clone()){
            if (postKey.userid.equals(userid)) timeline.remove(postKey);
        }
        upload("timeline", timeline);
        User user = findUser(userid);
        user.FollowerNotification(User.user.id, true);
    }
    public void createGroup(String groupid){
        groups.add(0, groupid);
        upload("groups", groups);
    }
    public void joinGroup(String groupid){
        groups.add(0, groupid);
        upload("groups", groups);
        FirebaseHelper.findGroup(groupid).addMember(user.id);
    }
    public void quitGroup(String groupid){
        groups.remove(groupid);
        upload("groups", groups);
        FirebaseHelper.findGroup(groupid).removeMember(user.id);
    }
    public void collect(Post post){
        collections.add(0, post.getKey());
        upload("collections", collections);
    }
    public void uncollect(Post post){
        collections.remove(post.getKey());
        upload("collections", collections);
    }
    public void rate(Rating rating){
        boolean remove = rating.getRating() == 0;
        findUser(rating.getUserid()).RatingNotification(rating);
        if (remove) Post.findPost(rating.getPostKey()).removeRating(rating);
        else Post.findPost(rating.getPostKey()).addRating(rating);
    }
    public void saveAsDraft(Post post){
        drafts.add(0, post);
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
        if (User.user != null && User.user.getId().equals(id)) return User.user;
        return FirebaseHelper.findUser(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(intro);
        dest.writeParcelable(icon, flags);
        dest.writeInt(user_number);
        dest.writeString(gender);
        dest.writeTypedList(posts);
        dest.writeTypedList(drafts);
        dest.writeTypedList(collections);
        dest.writeTypedList(comments_made);
        dest.writeTypedList(comments_recv);
        dest.writeTypedList(ratings_made);
        dest.writeTypedList(ratings_recv);
        dest.writeStringList(groups);
        dest.writeStringList(followers);
        dest.writeStringList(following);
        dest.writeTypedList(comments_notification);
        dest.writeTypedList(posts_notification);
        dest.writeTypedList(ratings_notification);
        dest.writeTypedList(timeline);
        dest.writeStringList(search_history);
    }
}
