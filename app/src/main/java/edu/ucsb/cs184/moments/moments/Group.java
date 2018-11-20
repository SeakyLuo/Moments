package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupid; // To Be Changed
    private String managerid;
    private String name;
    private Bitmap icon;
    private ArrayList<Integer> members = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public Group(){}

    public Group(String name, String managerid, Bitmap icon){
        this.name = name;
        this.managerid = managerid;
        this.icon = icon;
    }

    public void setGroupid(String groupid) { if (groupid != null) this.groupid = groupid; }
    public String getGroupid() { return groupid; }
    public String getManagerid() { return managerid; }
    public void setManagerid(String userid) { this.managerid = userid; }
    public Bitmap getIcon() { return icon; }
    public void setIcon(Bitmap icon) {
        this.icon = icon;
        upload("icon", icon);
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ArrayList<Integer> getMembers() { return members; }
    public ArrayList<Post> getPosts() { return posts; }

    public void addMember(int userid){
        members.add(userid);
    }
    public void addPost(Post post){
        posts.add(post);
    }
    public void removeMember(int userid){
        members.remove(userid);
    }
    public void deletePost(Post post){
        posts.remove(post);
    }
    private void upload(String key, Object value){
        FirebaseHelper.updateGroup(this, key, value);
    }
    public static Group findGroup(String id) { return FirebaseHelper.findGroup(id); }
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Group fromJson(String json){
        return (new Gson()).fromJson(json, Group.class);
    }

}
