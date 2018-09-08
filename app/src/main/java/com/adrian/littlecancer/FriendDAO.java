package com.adrian.littlecancer;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by adrian on 04.12.17.
 */

public class FriendDAO {
    private DatabaseHelper db;

    public FriendDAO(Context context){
        db = new DatabaseHelper(context);
    }

    public void deleteFriendByName(String name){
        db.deleteFriendByName(name);
    }

    public ArrayList<Friend> getFriends(){
        return db.getFriends();
    }

    public boolean addName(String name){
        return db.addName(name);
    }

    public void updateFriendName(String oldName, String newName){
        db.updateFriendName(oldName, newName);
    }
}
