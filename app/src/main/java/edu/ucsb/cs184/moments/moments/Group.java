package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupid;
    private String managerid;
    private String name;
    private String intro = "";
    private Bitmap icon;
    private ArrayList<String> members = new ArrayList<>();
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
    public void setManagerid(String userid) {
        this.managerid = userid;
        upload("managerid", managerid);
    }
    public Bitmap getIcon() { return icon; }
    public void setIcon(Bitmap icon) {
        this.icon = icon;
        upload("icon", icon);
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        upload("name", name);
    }
    public String getIntro() { return intro; }
    public void setIntro(String intro) {
        this.intro = intro;
        upload("icon", icon);
    }
    public ArrayList<String> getMembers() { return members; }
    public ArrayList<Post> getPosts() { return posts; }

    public void addMember(String userid){
        members.add(userid);
        upload("members", members);
    }
    public void addPost(Post post){
        posts.add(post);
        upload("posts", posts);
    }
    public void removeMember(String userid){
        members.remove(userid);
        upload("members", members);
    }
    public void deletePost(Post post){
        posts.remove(post);
        upload("posts", posts);
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
