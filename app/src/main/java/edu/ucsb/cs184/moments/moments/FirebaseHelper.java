package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHelper {

    private static ArrayList<OnDataReceivedListener> listeners = new ArrayList<>();
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
                Log.d("fuck","uds");
                uds = dataSnapshot;
                for (OnDataReceivedListener listener : listeners)
                    listener.onUDBReceived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gdb = db.child("groups");
        gdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fuck","gds");
                gds = dataSnapshot;
                for (OnDataReceivedListener listener : listeners)
                    listener.onGDBReceived();
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
            for(Post post : (ArrayList<Post>) ds.child("posts").getValue()){
                if (post.getContent().contains(keyword)){
                    posts.add(post);
                }
            }
        }
        return posts;
    }

    public static User findUser(String id){
        return uds.child(id).getValue(User.class);
    }

    public static Group findGroup(String id){
        return gds.child(id).getValue(Group.class);
    }

    public static Post findPost(Post.Key key) {
        for (Post post : (ArrayList<Post>) uds.child(key.userid).child("posts").getValue()){
            if (post.getKey() == key) return post;
        }
        return null;
    }
    public static Comment findComment(Comment.Key key){
        for (Comment comment : (ArrayList<Comment>) uds.child(key.userid).child("comments_made").getValue()){
            if (comment.getKey() == key) return comment;
        }
        return null;
    }

    public static void updateUser(final String key, final Object data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                udb.child(User.user.getUserid()).child(key).setValue(data);
            }
        }).start();
    }

    public static void updateGroup(final Group group, final String key, final Object data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                gdb.child(group.getGroupid()).child(key).setValue(data);
            }
        }).start();
    }

    public static void addDataReceivedListener(OnDataReceivedListener listener){
        listeners.add(listener);
    }

    interface OnDataReceivedListener{
        void onUDBReceived();
        void onGDBReceived();
    }
}
