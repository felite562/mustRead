package com.lu.bookbook.mustread;

/**
 * Created by Luba on 23.04.2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper {
    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }


    public void insertData (String name, String author, byte[] image){
        SQLiteDatabase db=getWritableDatabase();
        String sql = "INSERT INTO RECORD VALUES(NULL, ?, ?, ?)";
        SQLiteStatement statement =db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, author);
        statement.bindBlob(3,image);
        statement.executeInsert();




    }

    public void updateData(String name, String author, byte[] image, int id){
        SQLiteDatabase db=getWritableDatabase();
        String sql="UPDATE RECORD SET name=?, author=?, image=? WHERE id=?";
        SQLiteStatement statement =db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, author);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double) id);
        statement.execute();
        db.close();

    }

    public Cursor getData(String sql){
        SQLiteDatabase db=getReadableDatabase();
        return db.rawQuery(sql,null);


    }

    public void deleteData(int id){
        SQLiteDatabase db=getWritableDatabase();
        String sql="DELETE FROM RECORD WHERE id=?";
        SQLiteStatement statement =db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);
        statement.execute();
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
