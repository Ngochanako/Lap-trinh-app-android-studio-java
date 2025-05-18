package com.myapplication.Phone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.myapplication.Adapter.PhoneAdapter;
import com.myapplication.DBHelper;
import com.myapplication.Modal.DienThoai;

import java.util.ArrayList;
import java.util.List;

import com.myapplication.R;

public class PhoneListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PhoneAdapter adapter;
    private List<DienThoai> phoneList, filteredList;
    private EditText etSearch;
    private DBHelper dbHelper;
    private ImageButton btnAddPhone;
    private ImageButton btnSearch;
    private static final int ADD_PHONE_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_list);
        recyclerView = findViewById(R.id.myRecyclerView);
        btnAddPhone = findViewById(R.id.btnAddPhone);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        dbHelper = new DBHelper(this);
        phoneList = dbHelper.getAllPhones();
        Log.d("DB_DEBUG", "Số lượng điện thoại trong DB: " + phoneList.size());

        if (phoneList.isEmpty()) {
            dbHelper.insertPhone(new DienThoai(1, "Samsung Galaxy S23", 19990000, "Samsung", "samsung_s23"));
            dbHelper.insertPhone(new DienThoai(2, "iPhone 14 Pro", 28990000, "Apple", "iphone_14_pro"));
            phoneList = dbHelper.getAllPhones();
        }
// hiển thị danh sách
        filteredList = new ArrayList<>(phoneList);
        adapter = new PhoneAdapter(this,filteredList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
// thêm điện thoại moi
        btnAddPhone.setOnClickListener(v -> {
            Intent intent = new Intent(PhoneListActivity.this, AddPhoneActivity.class);
            startActivityForResult(intent,ADD_PHONE_REQUEST_CODE);
        });
// Sự kiện click nút tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            searchPhones(keyword);
        });

    }
// tìm kiếm điện thoai
private void searchPhones(String query) {
    List<DienThoai> result = new ArrayList<>();
    if (query.isEmpty()) {
        result.addAll(phoneList);
    } else {
        for (DienThoai phone : phoneList) {
            if (phone.getName().toLowerCase().contains(query.toLowerCase())) {
                result.add(phone);
            }
        }
    }
    adapter.updateList(result); // Gửi danh sách mới cho adapter
}
    // cập nhập lại danh sách
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            // Cập nhật lại danh sách sau khi chỉnh sửa
            loadPhoneList(); // Gọi lại hàm load từ DB
        }
        if (requestCode == ADD_PHONE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Load lại danh sách từ DB
           loadPhoneList();
        }
    }
    private void loadPhoneList() {
        phoneList = dbHelper.getAllPhones(); // Cập nhật danh sách gốc
        filteredList.clear();
        filteredList.addAll(phoneList); // Đồng bộ lại danh sách lọc
        adapter.updateList(filteredList); // Cập nhật adapter
    }
}
