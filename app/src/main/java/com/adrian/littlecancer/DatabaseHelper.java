package com.adrian.littlecancer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by adrian on 14.09.17.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "money";
    private static final String TABLE_FRIEND = "friend";
    private static final String TABLE_TRANSACTION = "trns";

    private static final String FRIEND_ID = "id";
    private static final String FRIEND_NAME = "name";

    private static final String TRANSACTION_ID = "id";
    private static final String TRANSACTION_VALUE = "value";
    private static final String TRANSACTION_TIMESTAMP = "timestamp";
    private static final String TRANSACTION_FRIEND_ID = "friend_id";
    private static final String TRANSACTION_NOTE = "note";

    private static final String CREATE_TABLE_FRIEND = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s TEXT)",TABLE_FRIEND, FRIEND_ID, FRIEND_NAME);

    private static final String CREATE_TABLE_TRANSACTION = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s REAL," +
            "%s TEXT," +
            "%s DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "%s INTEGER)",
            TABLE_TRANSACTION, TRANSACTION_ID, TRANSACTION_VALUE, TRANSACTION_NOTE, TRANSACTION_TIMESTAMP, TRANSACTION_FRIEND_ID);

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_FRIEND);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }

    public boolean addName(String name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRIEND_NAME, name);

        long result = db.insert(TABLE_FRIEND, null, contentValues);
        return result != -1;
    }

    public ArrayList<Friend> getFriends(){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT " + FRIEND_NAME + ", IFNULL(" + TRANSACTION_TIMESTAMP + ", DATETIME('now')) AS TIMESTAMP" +
                " FROM " + TABLE_FRIEND + " fr LEFT JOIN " + TABLE_TRANSACTION + " tr" +
                    " ON fr." + FRIEND_ID + " = " + " tr." + TRANSACTION_FRIEND_ID +
                " WHERE NOT EXISTS(SELECT 1 " +
                        " FROM " + TABLE_TRANSACTION + " tr2" +
                        " WHERE tr." + TRANSACTION_FRIEND_ID + " = tr2." + TRANSACTION_FRIEND_ID + ")" +
                " OR TIMESTAMP >= (" +
                        "SELECT MAX(" + TRANSACTION_TIMESTAMP + ")" +
                        " FROM " + TABLE_TRANSACTION + " tr2" +
                        " WHERE tr." + TRANSACTION_FRIEND_ID + " = tr2." + TRANSACTION_FRIEND_ID + ")" +
                " ORDER BY TIMESTAMP DESC"
                , null);
        ArrayList<Friend> friends = new ArrayList();
        while (c.moveToNext()){
            friends.add(new Friend(c.getString(0), getBalance(c.getString(0))));
        }
        c.close();
        return friends;
    }

    public Cursor getNames(){
        return getReadableDatabase().rawQuery("SELECT " + FRIEND_NAME + " FROM " + TABLE_FRIEND, null);
    }

    public ArrayList<Transaction> getTransactionsByName(String name){
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT " + TRANSACTION_VALUE + ", " + TRANSACTION_NOTE + ", " + TRANSACTION_TIMESTAMP +
                " FROM  " + TABLE_TRANSACTION + " tr, " + TABLE_FRIEND + " fr "+
                " WHERE " + TRANSACTION_FRIEND_ID + " = fr." + FRIEND_ID +
                " AND   " + FRIEND_NAME + " = '" + name + "'" +
                " ORDER BY " + TRANSACTION_TIMESTAMP, null);

        ArrayList<Transaction> transactions = new ArrayList();
        while (c.moveToNext()){
            transactions.add(new Transaction(c.getFloat(0), c.getString(1)));
        }
        c.close();
        return transactions;
    }

    public int getIdByName(String name){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + FRIEND_ID + " FROM " + TABLE_FRIEND +
                " WHERE " + FRIEND_NAME + " = '" + name + "'", null);
        cursor.moveToNext();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public void deleteFriendByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FRIEND, FRIEND_NAME + " = ?", new String[]{name});
    }

    public void updateFriendName(String oldName, String newName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FRIEND_NAME, newName);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_FRIEND, contentValues, FRIEND_ID + " = ?", new String[]{String.valueOf(getIdByName(oldName))});
    }

    public float getBalance(String name){
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT TOTAL(" + TRANSACTION_VALUE + ") FROM " + TABLE_TRANSACTION +
                " WHERE " + TRANSACTION_FRIEND_ID + " = " + getIdByName(name), null);
        cursor.moveToNext();
        float balance = cursor.getFloat(0);
        cursor.close();
        return balance;
    }

    public boolean addTransaction(String name, Float value, String note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRANSACTION_FRIEND_ID, getIdByName(name));
        contentValues.put(TRANSACTION_VALUE, value);
        contentValues.put(TRANSACTION_NOTE, note);

        long result = db.insert(TABLE_TRANSACTION, null, contentValues);
        return result != -1;
    }
}
