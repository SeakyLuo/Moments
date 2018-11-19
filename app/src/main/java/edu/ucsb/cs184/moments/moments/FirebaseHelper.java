package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private static FirebaseDatabase firebase;
    private static DatabaseReference db;
    private static DatabaseReference udb, gdb;

    public static void init(){
        firebase = FirebaseDatabase.getInstance();
        db = firebase.getReference();
        udb = db.child("users");
        gdb = db.child("groups");

        gdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("fuck", "Canceled:" + databaseError.toString());
            }
        });
    }

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

    public static User findUser(final String id){
        final boolean[] hasChild = new boolean[1];
        udb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hasChild[0] = dataSnapshot.hasChild(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("fuck", "Canceled:" + databaseError.toString());
            }
        });
        if (!hasChild[0]) return null;
        final User[] data = new User[1];
        udb.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data[0] = (User) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("fuck", "Canceled:" + databaseError.toString());
            }
        });
        return data[0];
//        return new User("Moments Developer","haitianluo@ucsb.edu","test");
    }

    public static Group findGroup(final String id){
        final boolean[] hasChild = new boolean[1];
        gdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hasChild[0] = dataSnapshot.hasChild(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("fuck", "Canceled:" + databaseError.toString());
            }
        });
        if (!hasChild[0]) return null;
        final Group[] data = new Group[1];
        udb.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data[0] = (Group) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("fuck", "Canceled:" + databaseError.toString());
            }
        });
        return data[0];
    }

    public static void updateUser(String key, Object data){
        udb.child(User.user.getUserid()).child(key).setValue(data);
    }

    public static void updateGroup(Group group, String key, Object data){
        gdb.child(group.getGroupid()).child(key).setValue(data);
    }
}
