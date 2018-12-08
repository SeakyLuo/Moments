package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Group implements Parcelable {
    private String id;
    private String managerid;
    private String name;
    private int group_number;
    private String intro = "";
    private Bitmap icon;
    private ArrayList<String> members = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public Group(){}

    public Group(String name, String managerid, Bitmap icon){
        this.name = name;
        this.managerid = managerid;
        this.members.add(managerid);
        this.icon = icon;
    }

    protected Group(Parcel in) {
        id = in.readString();
        managerid = in.readString();
        name = in.readString();
        group_number = in.readInt();
        intro = in.readString();
        icon = in.readParcelable(Bitmap.class.getClassLoader());
        members = in.createStringArrayList();
        posts = in.createTypedArrayList(Post.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public void setId(String id) { if (id != null) this.id = id; }
    public String getId() { return id; }
    public String getManagerid() { return managerid; }
    public void setManagerid(String userid) {
        this.managerid = userid;
        upload("managerid", managerid);
    }
    public void setNumber(int number) { this.group_number = number; }
    public int getNumber() { return group_number;}
    public Bitmap getIcon() { return icon; }
    public void setIcon(Bitmap icon) {
        this.icon = icon;
        upload("icon", icon);
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void modifyName(String name){
        this.name = name;
        upload("name", name);
    }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
    public void modifyIntro(String intro) {
        this.intro = intro;
        upload("intro", intro);
    }
    public ArrayList<String> getMembers() { return members; }
    public ArrayList<Post> getPosts() { return posts; }

    public void addMember(String userid){
        members.add(userid);
        upload("members", members);
    }
    public void addPost(Post post){
        posts.add(0, post);
        upload("posts", posts);
    }
    public void removeMember(String userid){
        members.remove(userid);
        upload("members", members);
    }
    public void removePost(Post post){
        posts.remove(post);
        upload("posts", posts);
    }
    private void upload(String key, Object value){
        FirebaseHelper.updateGroup(id, key, value);
    }
    public boolean containsKeyword(String keyword) { return name.contains(keyword) || Integer.toString(group_number).contains(keyword); }
    public static Group findGroup(String id) { return FirebaseHelper.findGroup(id); }
    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Group fromJson(String json){
        return (new Gson()).fromJson(json, Group.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Group)) return false;
        return id.equals(((Group) obj).id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(managerid);
        dest.writeString(name);
        dest.writeInt(group_number);
        dest.writeString(intro);
        dest.writeParcelable(icon, flags);
        dest.writeStringList(members);
        dest.writeTypedList(posts);
    }
}
