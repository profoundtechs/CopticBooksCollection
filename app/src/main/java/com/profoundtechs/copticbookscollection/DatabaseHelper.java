package com.profoundtechs.copticbookscollection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME="copticbookscollection.db";
    public static final String DB_LOCATION="/data/data/com.profoundtechs.copticbookscollection/databases/";
    private Context context;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void openDatabase(){
        String dbPath=context.getDatabasePath(DB_NAME).getPath();
        if (mDatabase!=null && mDatabase.isOpen()){
            return;
        }
        mDatabase=SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if (mDatabase!=null){
            mDatabase.close();
        }
    }

    public List<Content> getListContent(String booktitle,int id){
        id+=1;
        Content content=null;
        List<Content> contentList=new ArrayList<>();
        openDatabase();
        Cursor cursor=mDatabase.rawQuery("SELECT * FROM '"+booktitle+"' WHERE id = '"+id+"'",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            content=new Content(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getBlob(3),cursor.getBlob(4));
            contentList.add(content);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return contentList;
    }
}
