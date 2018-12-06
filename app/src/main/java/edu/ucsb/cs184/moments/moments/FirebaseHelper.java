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

    private static FirebaseDatabase firebase;
    private static DatabaseReference db;
    private static DatabaseReference udb, gdb, uc, gc, uudb;
    private static DataSnapshot uds, gds, ucds, gcds;
    private static OnUDBReceivedListener uReceivedListener;
    private static OnGDBReceivedListener gReceivedListener;
    private static ArrayList<OnDataUpdatesListener> updateListeners = new ArrayList<>();
    private static AfterUserInsertedListener uInsertionListener;
    private static AfterGroupInsertedListener gInsertionListener;

    public static void init(){
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        firebase = FirebaseDatabase.getInstance();
        db = firebase.getReference();
        db.keepSynced(true);
        udb = db.child("users");
        udb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fuck","uds");
                uds = dataSnapshot;
                if (User.firebaseUser != null){
                    uudb = udb.child(User.firebaseUser.getUid());
                    User.user = uds.child(User.firebaseUser.getUid()).getValue(User.class);
                }
                if (uReceivedListener != null) uReceivedListener.onUDBReceived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        udb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uds = dataSnapshot;
                for (OnDataUpdatesListener listener: updateListeners)
                    listener.onUDBUpdates();
                // This updates user
                // So there is a TODO: gives a red dot on the bottom navigation when there is a new post
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gdb = db.child("groups");
        gdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fuck","gds");
                gds = dataSnapshot;
                if (gReceivedListener != null) gReceivedListener.onGDBReceived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // TODO: Since we update user, updating group is also necessary but has lower priority.
                for (OnDataUpdatesListener listener: updateListeners)
                    listener.onGDBUpdates();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        uc = db.child("user_count");
        uc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fuck","ucds");
                ucds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        gc = db.child("group_count");
        gc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fuck","gcds");
                gcds = dataSnapshot;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // I don't think these three should be used
    public static FirebaseDatabase getFirebase(){ return firebase; }
    public static DatabaseReference getUdb() { return udb; }
    public static DatabaseReference getGdb() { return gdb; }

    public static void insertUser(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int number = ucds.getValue(Integer.class);
                user.setNumber(number);
                uc.setValue(++number);
                udb.child(user.getId()).setValue(user);
                if (uInsertionListener != null) uInsertionListener.afterUserInserted(user);
            }
        }).start();
    }

    public static void insertGroup(final Group group){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String id = gdb.push().getKey();
                group.setId(id);
                int number = gcds.getValue(Integer.class);
                group.setNumber(number);
                gc.setValue(++number);
                gdb.child(id).setValue(group);
                if (gInsertionListener != null) gInsertionListener.afterGroupInserted(group);
            }
        }).start();
    }

    public static User findUser(String id){
        return uds.child(id).getValue(User.class);
    }
    public static Group findGroup(String id){
        return gds.child(id).getValue(Group.class);
    }

    public static ArrayList<Post> searchPosts(String keyword){
        ArrayList<Post> posts = new ArrayList<>();
        for (DataSnapshot ds: uds.getChildren()){
            for (Post post: (ArrayList<Post>) ds.child("posts").getValue(ArrayList.class)){
                if (post.getContent().contains(keyword))
                    posts.add(post);
            }
        }
        return posts;
    }
    public static ArrayList<User> searchUsers(String keyword) {
        ArrayList<User> users = new ArrayList<>();
        for (DataSnapshot ds: uds.getChildren()){
            User user = ds.getValue(User.class);
            if (user.getName().contains(keyword) || Integer.toString(user.getNumber()).contains(keyword))
                users.add(user);
        }
        return users;
    }
    public static ArrayList<Group> searchGroups(String keyword) {
        ArrayList<Group> groups = new ArrayList<>();
        for (DataSnapshot ds: uds.getChildren()){
            Group group = ds.getValue(Group.class);
            if (group.getName().contains(keyword) || Integer.toString(group.getNumber()).contains(keyword))
                groups.add(group);
        }
        return groups;
    }

    public static Post findPost(Post.Key key) {
        for (Post post: (ArrayList<Post>) uds.child(key.userid).child("posts").getValue(ArrayList.class)){
            if (post.getKey().equals(key)) return post;
        }
        return null;
    }
    public static Comment findComment(Comment.Key key){
        for (Comment comment: (ArrayList<Comment>) uds.child(key.userid).child("comments_made").getValue(ArrayList.class)){
            if (comment.getKey().equals(key)) return comment;
        }
        return null;
    }

    public static void updateUser(final String key, final Object data){
         new Thread(new Runnable() {
            @Override
            public void run() {
                if(uudb != null && User.user != null)
                    uudb.child(key).setValue(data);
            }
        }).start();
    }

    public static void updateGroup(final Group group, final String key, final Object data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(gdb != null && group != null)
                    gdb.child(group.getId()).child(key).setValue(data);
            }
        }).start();
    }

    public static void setOnUDBReceivedListener(OnUDBReceivedListener listener){
        uReceivedListener = listener;
    }
    public static void setOnGDBReceivedListener(OnGDBReceivedListener listener){
        gReceivedListener = listener;
    }
    public static void addOnDateUpdatesListener(OnDataUpdatesListener listener){
        updateListeners.add(listener);
    }
    public static void setAfterUserInsertionListener(AfterUserInsertedListener listener){
        uInsertionListener = listener;
    }
    public static void setAfterGroupInsertionListener(AfterGroupInsertedListener listener){
        gInsertionListener = listener;
    }

    public static boolean initFinished() { return uds != null && gds != null && ucds != null && gcds != null; }

    interface OnUDBReceivedListener{
        void onUDBReceived();
    }
    interface OnGDBReceivedListener{
        void onGDBReceived();
    }
    interface AfterUserInsertedListener {
        void afterUserInserted(User user);
    }
    interface AfterGroupInsertedListener {
        void afterGroupInserted(Group group);
    }

    interface OnDataUpdatesListener {
        void onUDBUpdates();
        void onGDBUpdates();
    }
}
