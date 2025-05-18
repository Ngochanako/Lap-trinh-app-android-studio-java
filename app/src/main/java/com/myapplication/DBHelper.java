package com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.myapplication.Modal.Accessory;
import com.myapplication.Modal.DienThoai;

import java.util.List;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "phones.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "phones";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_BRAND = "brand";

    public static final String COLUMN_IMAGE = "image";
// account login
    private static final String TABLE_USERS = "User";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tạo bảng phones
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_IMAGE + " TEXT)";

        db.execSQL(createTable);
        // tạo bảng users
        String sqlUser = " create table " +TABLE_USERS +"("
                + COL_USERNAME + " TEXT PRIMARY KEY,"
                + COL_PASSWORD + " TEXT"
                + ")";
        db.execSQL(sqlUser);
        // Thêm tài khoản mặc định vào bảng user
        db.execSQL("INSERT INTO User(username, password) VALUES('admin', '123456')");
        // tạo bảng accessories
        String createAccessoryTable = "CREATE TABLE Accessories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "image TEXT, " +
                "description TEXT, " +
                "compatibleBrand TEXT, " +
                "type TEXT, " +
                "price REAL )";
        db.execSQL(createAccessoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("drop table if exists "+TABLE_USERS);
        db.execSQL("drop table if exists Accessories");
        onCreate(db);
    }
    // ✅ Kiểm tra tài khoản đã tồn tại
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // ✅ Đăng nhập
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password});

        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        return isLoggedIn;
    }
    // Thêm điện thoại mới
    public void insertPhone(DienThoai phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", phone.getId());
        values.put("name", phone.getName());
        values.put("price", phone.getPrice());
        values.put("brand", phone.getBrand());
        values.put("image", phone.getImageResId());
        db.insert("Phones", null, values);
        db.close();
    }

    // Lấy danh sách điện thoại
    public List<DienThoai> getAllPhones() {
        List<DienThoai> phoneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Phones", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String brand = cursor.getString(3);
                String imageResId = cursor.getString(4);

                phoneList.add(new DienThoai(id, name, price, brand, imageResId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return phoneList;
    }

    // Xóa điện thoại
    public void deletePhone(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Cập nhật điện thoại
    public void updatePhone(DienThoai phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", phone.getName());
        values.put("price", phone.getPrice());
        values.put("brand", phone.getBrand());
        values.put("image", phone.getImageResId());
        int rowsAffected=db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(phone.getId())});
        Log.d("DB_UPDATE", "Rows affected: " + rowsAffected);
        db.close();
    }


    // Tìm kiếm điện thoại theo tên
    public List<DienThoai> searchPhones(String query) {
        List<DienThoai> phoneList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                phoneList.add(new DienThoai(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return phoneList;
    }
    // check if name of phone is existed
    public boolean isPhoneNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE " + COLUMN_NAME + " = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Thêm phụ kiện mới
    public void insertAccessory(Accessory accessory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", accessory.getId());
        values.put("name", accessory.getName());
        values.put("image", accessory.getImageResId());
        values.put("description", accessory.getDescription());
        values.put("compatibleBrand", accessory.getCompatibleBrand());
        values.put("type",accessory.getTypeAccessory());
        values.put("price", accessory.getPrice()); // thêm giá
        db.insert("Accessories", null, values);
        db.close();
    }

    // Lấy danh sách tất cả phụ kiện
    public List<Accessory> getAllAccessories() {
        List<Accessory> accessoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Accessories", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String imageResId = cursor.getString(2);
                String description = cursor.getString(3);
                String compatibleBrand = cursor.getString(4);
                String type = cursor.getString(5); // 👈 Lấy cột "type"
                double price = cursor.getDouble(6);

                accessoryList.add(new Accessory(id, name, imageResId, description, compatibleBrand, type, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accessoryList;
    }

    // Xóa phụ kiện
    public void deleteAccessory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Accessories", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Cập nhật phụ kiện
    public void updateAccessory(Accessory accessory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", accessory.getName());
        values.put("image", accessory.getImageResId());
        values.put("description", accessory.getDescription());
        values.put("compatibleBrand", accessory.getCompatibleBrand());
        values.put("type", accessory.getTypeAccessory()); // 👈 Thêm loại
        values.put("price", accessory.getPrice());

        int rowsAffected = db.update("Accessories", values, "id = ?", new String[]{String.valueOf(accessory.getId())});
        Log.d("DB_UPDATE_ACCESSORY", "Rows affected: " + rowsAffected);
        db.close();
    }

    // Tìm kiếm phụ kiện theo tên
    public List<Accessory> searchAccessories(String query) {
        List<Accessory> accessoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Accessories WHERE name LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String imageResId = cursor.getString(2);
                String description = cursor.getString(3);
                String compatibleBrand = cursor.getString(4);
                String type = cursor.getString(5); // 👈 Lấy loại phụ kiện
                double price = cursor.getDouble(6);

                accessoryList.add(new Accessory(id, name, imageResId, description, compatibleBrand, type, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accessoryList;
    }

    // Kiểm tra tên phụ kiện có tồn tại
    public boolean isAccessoryNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Accessories WHERE name = ?", new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Trả về danh sách các loại phụ kiện theo từng hãng điện thoại
    public List<String> getAccessoryTypesGroupedByBrand() {
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Lấy các loại phụ kiện duy nhất theo từng hãng
        String query = "SELECT compatibleBrand, GROUP_CONCAT(DISTINCT type) " +
                "FROM Accessories GROUP BY compatibleBrand";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String brand = cursor.getString(0);
                String types = cursor.getString(1);
                result.add(brand + ": " + types);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return result;
    }


    // Trả về top 5 hãng có nhiều phụ kiện
    public List<String> getTop5AccessoryBrands() {
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT compatibleBrand, COUNT(*) FROM Accessories GROUP BY compatibleBrand ORDER BY COUNT(*) DESC LIMIT 5", null);
        if (cursor.moveToFirst()) {
            do {
                String brand = cursor.getString(0);
                int count = cursor.getInt(1);
                result.add(brand + ": " + count + " phụ kiện");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

}
