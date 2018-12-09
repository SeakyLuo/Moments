package edu.ucsb.cs184.moments.moments;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;

public class Group implements Parcelable {
    private String id;
    private String managerid;
    private String name;
    private int group_number;
    private Bitmap icon;
    private String intro = "";
    // private group?
    private ArrayList<String> members = new ArrayList<>();
    private ArrayList<Post> posts = new ArrayList<>();

    public Group(){}

    public Group(String name, String managerid){
        this.name = name;
        this.managerid = managerid;
        this.members.add(managerid);
    }

    protected Group(Parcel in) {
        id = in.readString();
        managerid = in.readString();
        name = in.readString();
        group_number = in.readInt();
        icon = in.readParcelable(Bitmap.class.getClassLoader());
        intro = in.readString();
        members = in.createStringArrayList();
        posts = in.createTypedArrayList(Post.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(managerid);
        dest.writeString(name);
        dest.writeInt(group_number);
        dest.writeParcelable(icon, flags);
        dest.writeString(intro);
        dest.writeStringList(members);
        dest.writeTypedList(posts);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public void setManagerid(String userid) { this.managerid = userid; }
    public void modifyManager(String userid){
        this.managerid = userid;
        upload("managerid", managerid);
    }
    public boolean IsManager(String userid) { return managerid.equals(userid); }
    public void setNumber(int number) { this.group_number = number; }
    public int getNumber() { return group_number;}
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
    public void SetIcon(Bitmap bitmap){
        icon = bitmap;
    }
    public StorageReference GetIcon(){
        return FirebaseHelper.getIcon(FirebaseHelper.GROUP_ICON, id);
    }

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

    public static class GroupComparator implements Comparator<Group> {
        @Override
        public int compare(Group o1, Group o2) { return new Post.TimeComparator().compare(o1.posts.get(0).GetKey(), o2.posts.get(0).GetKey()); }
    }
}
