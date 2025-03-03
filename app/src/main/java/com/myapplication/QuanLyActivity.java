package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class QuanLyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
    }

    // Mở màn hình Món ăn
    public void openDienThoai(View view) {
        Intent intent = new Intent(this, DienThoai.class);
        startActivity(intent);
    }

    // Mở màn hình Loại món
    public void openLoaiMon(View view) {
        Intent intent = new Intent(this, PhuKien.class);
        startActivity(intent);
    }

    // Mở màn hình Thống kê
    public void openThongKe(View view) {
        Intent intent = new Intent(this, Thongke.class);
        startActivity(intent);
    }
}
