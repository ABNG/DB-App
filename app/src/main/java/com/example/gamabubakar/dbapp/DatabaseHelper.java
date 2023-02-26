package com.example.gamabubakar.dbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by gamAbubakar on 1/23/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    final static String dbname="Student";
    final static String tablename="Student_table";
    final static String col_1="id";
    final static String col_2="name";
    final static String col_3="marks";


    public DatabaseHelper(Context context) {
        super(context, dbname, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS "+tablename+"("+col_1+" integer not null primary key autoincrement,"+col_2+" text not null,"+col_3+" integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table IF EXISTS "+tablename+"");
        onCreate(db);

    }
    public boolean insertdata(String name,int marks){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col_2,name);
        cv.put(col_3,marks);
        long check=db.insert(tablename,null,cv);
        if(check==-1)
            return false;
        else
            return true;
    }
    public Cursor getalldata(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+tablename+"",null);
        return res;
    }
    public int updated(String id,String name,int marks){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(col_1,id);
        cv.put(col_2,name);
        cv.put(col_3,marks);
        int check=db.update(tablename,cv,""+col_1+"=?",new String[]{id});
            return check;
    }
    public int deletedata(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int result=db.delete(tablename,""+col_1+"=?",new String[]{id});
        return result;
    }

    public Cursor searchdata(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        String query="select * from Student_table where "+col_2+" like ?";
        Cursor res=db.rawQuery(query,new String[]{"%"+name+"%"});
        return res;
    }

}
