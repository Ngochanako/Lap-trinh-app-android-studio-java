package com.myapplication.Category.Phone;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.myapplication.Adapter.PhoneBrandAdapter;
import com.myapplication.DBHelper;
import com.myapplication.R;
import java.util.ArrayList;

public class PhoneBrandActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> phoneBrands;
    PhoneBrandAdapter adapter;
    Button btnAddBrand;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_brand);

        listView = findViewById(R.id.listViewPhoneBrands);
        btnAddBrand = findViewById(R.id.btnAddBrand);
        dbHelper = new DBHelper(this);

        loadBrandsFromDatabase();

        adapter = new PhoneBrandAdapter(this, phoneBrands);
        listView.setAdapter(adapter);

        btnAddBrand.setOnClickListener(v -> showAddDialog());
    }

    private void loadBrandsFromDatabase() {
        phoneBrands = new ArrayList<>(dbHelper.getAllPhoneBrands());
    }

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint("Nhập tên hãng");

        new AlertDialog.Builder(this)
                .setTitle("Thêm hãng mới")
                .setView(input)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String newBrand = input.getText().toString().trim();
                    if (!newBrand.isEmpty()) {
                        long result = dbHelper.addPhoneBrand(newBrand);
                        if (result != -1) {
                            Toast.makeText(this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                            loadBrandsFromDatabase();
                            adapter.updateData(phoneBrands);
                        } else {
                            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
