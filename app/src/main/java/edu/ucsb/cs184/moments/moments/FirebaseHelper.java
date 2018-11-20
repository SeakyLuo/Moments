package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {

    private static FirebaseDatabase firebase;
    private static DatabaseReference db;
    private static DatabaseReference udb, gdb;
    private static DataSnapshot uds, gds;

    public static void init(){
        firebase = FirebaseDatabase.getInstance();
        db = firebase.getReference();
        udb = db.child("users");
        udb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                android.util.Log.d("fuck","fuck1")
                uds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gdb = db.child("groups");
        gdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // I don't think these should be used
    public static FirebaseDatabase getFirebase(){ return firebase; }
    public static DatabaseReference getUdb() { return udb; }
    public static DatabaseReference getGdb() { return gdb; }

    public static void insertUser(User user){
        udb.child(user.getUserid()).setValue(user);
    }

    public static void insertGroup(Group group){
        String id = gdb.push().getKey();
        group.setGroupid(id);
        gdb.child(id).setValue(group);
    }

    public static ArrayList<Post> findPosts(String keyword){
        ArrayList<Post> posts = new ArrayList<>();
        for (DataSnapshot ds : uds.getChildren()){
            for(Post post : (ArrayList<Post>) ds.child("Posts").getValue()){
                if (post.getContent().contains(keyword)){
                    posts.add(post);
                }
            }
        }
        return posts;
    }

    public static User findUser(String id){
        return (User) uds.child(id).getValue();
    }

    public static Group findGroup(String id){
        return (Group) gds.child(id).getValue();
    }

    public static void updateUser(String key, Object data){
        udb.child(User.user.getUserid()).child(key).setValue(data);
    }

    public static void updateGroup(Group group, String key, Object data){
        gdb.child(group.getGroupid()).child(key).setValue(data);
    }

    public static boolean initFinished(){
        return uds != null && gds != null;
    }

    public static interface OnDataSnapshotInit{
        public void onGDSinit();
        public void onUDSinit();
    }
}
