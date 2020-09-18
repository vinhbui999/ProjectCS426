package com.example.projectcs426;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String HelperInfor_TABLE_NAME = "HelperInfor";

    public static final String HelperInfor_ID = "ID"; // primary key
    public static final String HelperInfor_COLUMN_Name = "FullName";
    public static final String HelperInfor_COLUMN_Phone = "Phone";
    public static final String HelperInfor_COLUMN_SEX = "Gender";
    public static final String HelperInfor_COLUMN_DOB = "DOB";
    public static final String HelperInfor_COLUMN_Add = "Address";
    public static final String HelperInfor_COLUMN_Note = "Note";
    public static final String HelperInfor_COLUMN_RATING = "Rating";
    public static final String HelperInfor_COLUMN_AVATAR = "Avatar";
    public static final String HelperInfor_COLUMN_AVAILABLE = "Available";

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS HelperInfor" +
                "(ID integer primary key autoincrement, FullName text, Phone text, Gender text, DOB text, Address text, Note text, Rating float, Avatar text, Available text)");

        //create table user hire helper
        // create table user's favorite helper
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS HelperInfor");
        //onCreate(sqLiteDatabase);
    }

    public boolean insertHelper(String name, String phone, String gen, String dob, String add, String note, Float rate, String avatar, String avai){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FullName",name);
        contentValues.put("Phone",phone);
        contentValues.put("Gender",gen);
        contentValues.put("DOB",dob);
        contentValues.put("Address",add);
        contentValues.put("Note",note);
        contentValues.put("Rating",rate);
        contentValues.put("Avatar",avatar);
        contentValues.put("Available",avai);
        db.insert("HelperInfor", null, contentValues);
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM HelperInfor", null);
        return res;
    }

    public boolean updateHelperInfor(int id){ // hired
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Available", "false");
        db.update("HelperInfor", contentValues, "ID="+id, null);
        return true;
    }

    public boolean updateHelperInforFree(int id){ // free
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Available", "true");
        db.update("HelperInfor", contentValues, "ID="+id, null);
        return true;
    }

}
