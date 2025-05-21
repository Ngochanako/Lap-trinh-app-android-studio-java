package com.myapplication.CategoryAccessory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.Adapter.AccessoryTypeAdapter;
import com.myapplication.DBHelper;
import com.myapplication.R;

import java.util.ArrayList;

public class AccessoryTypeActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> accessoryTypes;
    AccessoryTypeAdapter adapter;
    Button btnAddType;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_brand); // chú ý đúng tên file XML

        listView = findViewById(R.id.listViewTypeAccessory);
        btnAddType = findViewById(R.id.btnAddType);
        dbHelper = new DBHelper(this);

        loadAccessoryTypesFromDatabase();

        adapter = new AccessoryTypeAdapter(this, accessoryTypes, dbHelper);
        listView.setAdapter(adapter);

        btnAddType.setOnClickListener(v -> showAddDialog());
    }

    private void loadAccessoryTypesFromDatabase() {
        accessoryTypes = new ArrayList<>(dbHelper.getAllAccessoryTypesWithId());
    }

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint("Nhập tên loại phụ kiện");

        new AlertDialog.Builder(this)
                .setTitle("Thêm loại phụ kiện")
                .setView(input)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String newType = input.getText().toString().trim();
                    if (!newType.isEmpty()) {
                        long result = dbHelper.addAccessoryType(newType);
                        if (result != -1) {
                            Toast.makeText(this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                            loadAccessoryTypesFromDatabase();
                            adapter.updateData(accessoryTypes);
                        } else {
                            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
