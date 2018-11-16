package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Group {
    private int groupid;
    private int managerid;
    private String groupname;
    private Bitmap icon;
    private ArrayList<Integer> members = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public Group(String groupname, int managerid){
        this.groupname = groupname;
        this.managerid = managerid;
        // generates a random group id
    }

    public int getGroupid() { return groupid; }
    public int getManagerid() { return managerid; }
    public void setManagerid(int userid) { this.managerid = userid; }
    public Bitmap getIcon() { return icon; }
    public void setIcon(Bitmap bitmap) { this.icon = bitmap; }
    public String getGroupname() { return groupname; }
    public void setGroupname(String name) { this.groupname = name; }
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
    public static Group findGroup(int groupid) {
        return null;
    }
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Group fromJson(String json){
        return (new Gson()).fromJson(json, Group.class);
    }

}
