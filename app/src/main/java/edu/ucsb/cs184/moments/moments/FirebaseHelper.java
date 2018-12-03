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
    private static DatabaseReference udb, gdb, uc, gc;
    private static DataSnapshot uds, gds, ucds, gcds;

    public static void init(){
        firebase = FirebaseDatabase.getInstance();
        db = firebase.getReference();
        udb = db.child("users");

        udb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uds = dataSnapshot;
                // This updates user
                // So there is a TODO: gives a red dot on the bottom navigation when there is a new post
                if (User.firebaseUser != null)
                    User.user = uds.child(User.firebaseUser.getUid()).getValue(User.class);
                for (OnDataReceivedListener listener: listeners)
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
                gds = dataSnapshot;
                Log.d("fuck","fuck4");
                // TODO: Since we update user, updating group is also necessary but has lower priority.
                for (OnDataReceivedListener listener: listeners)
                    listener.onGDBReceived();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        uc = db.child("user_count");
        uc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

    public static void insertUser(User user){
        int number = ucds.getValue(Integer.class);
        user.setNumber(number);
        uc.setValue(number++);
        udb.child(user.getId()).setValue(user);
    }

    public static void insertGroup(Group group){
        String id = gdb.push().getKey();
        group.setId(id);
        int number = gcds.getValue(Integer.class);
        group.setNumber(number);
        gc.setValue(number++);
        gdb.child(id).setValue(group);
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
                if(udb != null) udb.child(User.user.getId()).child(key).setValue(data);
            }
        }).start();
    }

    public static void updateGroup(final Group group, final String key, final Object data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(gdb != null) gdb.child(group.getId()).child(key).setValue(data);
            }
        }).start();
    }

    public static void addDataReceivedListener(OnDataReceivedListener listener){
        listeners.add(listener);
    }

    public static boolean initFinished() { return uds != null && gds != null; }

    interface OnDataReceivedListener{
        void onUDBReceived();
        void onGDBReceived();
    }
}
