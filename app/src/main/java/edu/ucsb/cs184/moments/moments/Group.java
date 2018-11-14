package edu.ucsb.cs184.moments.moments;

import android.view.Gravity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Group {
    private int groupid;
    private int managerid;
    private String groupname;
    private ArrayList<Integer> members = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public Group(String groupname, int managerid){
        this.groupname = groupname;
        this.managerid = managerid;
        // generates a random group id
    }

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
