package com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.myapplication.Modal.Laptop;
import com.myapplication.Modal.LoaiLaptop;
import com.myapplication.Modal.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "QLLaptop.sqlite";
    private static final String TABLE_Lap = "Laptop";
    private static final String COLUMN_MA = "MaLaptop";
    private static final String COLUMN_TEN = "Ten";
    private static final String COLUMN_ANH = "Anh";
    //private static final String COLUMN_LOAI = "Loai";
    private static final String COLUMN_GIA = "Gia";
    private static final String COLUMN_KG = "Kg";

    private static final String TABLE_Loai = "LoaiLaptop";
    private static final String COLUMN_MA_Loai = "MaLoai";
    private static final String COLUMN_TEN_LOAI = "TenLoai";

    private static final String TABLE_USER = "User";
    private static final String COLUMN_TK_User = "TaiKhoan";
    private static final String COLUMN_MK_User = "MatKhau";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPhanLoaiSanPham = "create table "+TABLE_Loai +"("
                + COLUMN_MA_Loai + " TEXT PRIMARY KEY,"
                + COLUMN_TEN_LOAI + " TEXT"
                + ")";
        db.execSQL(sqlPhanLoaiSanPham);

        String sql  = "create table "+TABLE_Lap + "("
                + COLUMN_MA + " TEXT primary key ,"
                + COLUMN_TEN + " TEXT,"
                + COLUMN_ANH + " BLOB,"
                + COLUMN_KG + " REAL,"
                + COLUMN_GIA + " INTEGER,"
                + COLUMN_MA_Loai + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_MA_Loai + ") REFERENCES " + TABLE_Loai + "(" + COLUMN_MA_Loai + ")"
                + ")" ;
        db.execSQL(sql);

        String sqlUser = " create table " +TABLE_USER +"("
                + COLUMN_TK_User + " TEXT PRIMARY KEY,"
                + COLUMN_MK_User + " TEXT"
                + ")";
        db.execSQL(sqlUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_Loai);
        db.execSQL("drop table if exists "+TABLE_Lap);
        db.execSQL("drop table if exists "+TABLE_USER);
        onCreate(db);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_TK_User, user.getTaikhoan());
        values.put(COLUMN_MK_User, user.getMatkhau());
        long row = db.insert(TABLE_USER,null,values);
        db.close();
        return row;
    }
    public boolean checkUser(String tk , String mk) {
        SQLiteDatabase db = getWritableDatabase();
        String sql ="select * from User where TaiKhoan=? and MatKhau=? ";
        String[] selectionArgs = {tk ,mk};
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        if(cursor!=null && cursor.moveToFirst()){
            return true;
        }
        db.close();
        return true;
    }

    public ArrayList<LoaiLaptop> getAllLoai() {
        ArrayList<LoaiLaptop> list = new ArrayList<LoaiLaptop>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_Loai,null);

        if(cursor!=null && cursor.moveToFirst()){
            do{
                LoaiLaptop loai = new LoaiLaptop();
                loai.setMaloai(cursor.getString(0));
                loai.setTenloai(cursor.getString(1));
                list.add(loai);
            }while(cursor.moveToNext());
        }
        db.close();
        return list;

    }
    public long insertLoaiLaptop(LoaiLaptop loai){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_MA_Loai, loai.getMaloai());
        values.put(COLUMN_TEN_LOAI, loai.getTenloai());
        long row = db.insert(TABLE_Loai,null,values);
        db.close();
        return row;
    }

    public ArrayList<Laptop> getAllLaptop() {
        ArrayList<Laptop> list = new ArrayList<Laptop>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_Lap,null);

        if(cursor!=null && cursor.moveToFirst()){
            do{
                Laptop lp = new Laptop();
                lp.setMa(cursor.getString(0));
                lp.setTen(cursor.getString(1));
                lp.setAnh(cursor.getBlob(2));
                lp.setKg(cursor.getFloat(3));
                lp.setGia(cursor.getInt(4));
                lp.setLoai(cursor.getString(5));

                list.add(lp);
            }while(cursor.moveToNext());
        }
        db.close();
        return list;
    }
    public long insertLaptop(Laptop lp){
        SQLiteDatabase db = getWritableDatabase();

        String sql ="select * from LoaiLaptop where MaLoai=?";
        String[] selectionArgs = {lp.getLoai()};
        Cursor cursor = db.rawQuery(sql,selectionArgs);

        if(cursor!=null && cursor.moveToFirst()){
            ContentValues values=new ContentValues();
            values.put(COLUMN_MA,lp.getMa());
            values.put(COLUMN_TEN,lp.getTen());
            values.put(COLUMN_ANH,lp.getAnh());
            values.put(COLUMN_MA_Loai,lp.getLoai());
            values.put(COLUMN_KG,lp.getKg());
            values.put(COLUMN_GIA,lp.getGia());
            long row = db.insert(TABLE_Lap,null,values);
            db.close();
            return row;
        }else{

            return 0;
        }

    }

    public ArrayList<String> getAllNameLoai() {
        ArrayList<String> list = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + COLUMN_TEN_LOAI + " from "+TABLE_Loai,null);

        if(cursor!=null && cursor.moveToFirst()){
            do{

                list.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public long updateLaptop(Laptop laptop) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_MA,laptop.getMa());
        values.put(COLUMN_TEN , laptop.getTen());
        values.put(COLUMN_ANH,laptop.getAnh());
        values.put(COLUMN_KG,laptop.getKg());
        values.put(COLUMN_GIA,laptop.getGia());
        values.put(COLUMN_MA_Loai,laptop.getLoai());

        long row = db.update(TABLE_Lap,values,COLUMN_MA + " = ?",new String[] {laptop.getMa()});
        db.close();
        return row;
    }
    public long updateLoai(LoaiLaptop loai) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_LOAI , loai.getTenloai());
        long row = db.update(TABLE_Loai,values,COLUMN_MA_Loai + " = ?",new String[] {loai.getMaloai()});
        db.close();
        return row;
    }
    public String getMaLoai(String tenLoai){
        SQLiteDatabase db = getWritableDatabase();
        String sql ="select "+ COLUMN_MA_Loai + " from LoaiLaptop where TenLoai=?";
        String[] selectionArgs = {tenLoai};
        String id="";
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        if(cursor!=null && cursor.moveToFirst()){
            do{

                 id =cursor.getString(0);
            }while(cursor.moveToNext());
        }
        db.close();
        return id;
    }
    public String getTenLoai(String maLoai){
        SQLiteDatabase db = getWritableDatabase();
        String sql ="select "+ COLUMN_TEN_LOAI + " from LoaiLaptop where MaLoai=?";
        String[] selectionArgs = {maLoai};
        String id="";
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        if(cursor!=null && cursor.moveToFirst()){
            do{

                id =cursor.getString(0);
            }while(cursor.moveToNext());
        }
        db.close();
        return id;
    }
    public long deleteLaptop(String maLaptop){
        SQLiteDatabase db = getWritableDatabase();
        long row = db.delete(TABLE_Lap,COLUMN_MA+" = ?",new String[] {maLaptop});
        db.close();
        return row;
    }

    public long deleteLoai(String maloai) {
        SQLiteDatabase db = getWritableDatabase();
        String sql ="select * from Laptop where MaLoai=?";
        String[] selectionArgs = {maloai};
        Cursor cursor = db.rawQuery(sql,selectionArgs);
        if(cursor!=null && cursor.moveToFirst()){
            return 0;
        }else {
            long row = db.delete(TABLE_Loai,COLUMN_MA_Loai+" = ?",new String[] {maloai});
            db.close();
            return row;
        }


    }
}
