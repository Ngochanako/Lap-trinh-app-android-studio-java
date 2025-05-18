package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.Accessory.AccessoryListActivity;
import com.myapplication.Phone.PhoneListActivity;
import com.myapplication.Report.ReportActivity;

public class QuanLyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
    }

    // Mở màn hình Điện thoại
    public void openDienThoai(View view) {
        Intent intent = new Intent(this, PhoneListActivity.class);
        startActivity(intent);
    }

    // Mở màn hình Phụ kiện
    public void openPhuKien(View view) {
        Intent intent = new Intent(this, AccessoryListActivity.class);
        startActivity(intent);
    }

    // Mở màn hình Thống kê
    public void openThongKe(View view) {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }
}
