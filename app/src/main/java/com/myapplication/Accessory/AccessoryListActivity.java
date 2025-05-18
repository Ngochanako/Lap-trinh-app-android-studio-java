package com.myapplication.Accessory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.Adapter.AccessoryAdapter;
import com.myapplication.DBHelper;
import com.myapplication.Modal.Accessory;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AccessoryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AccessoryAdapter adapter;
    private List<Accessory> accessoryList, filteredList;
    private EditText etSearch;
    private DBHelper dbHelper;
    private ImageButton btnAddAccessory;
    private ImageButton btnSearch;
    private static final int ADD_ACCESSORY_REQUEST_CODE = 2001;
    private static final int EDIT_ACCESSORY_REQUEST_CODE = 2002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_list);

        recyclerView = findViewById(R.id.recyclerViewAccessory);
        btnAddAccessory = findViewById(R.id.btnAddAccessory);
        btnSearch = findViewById(R.id.btnSearchAccessory);
        etSearch = findViewById(R.id.etSearchAccessory);

        dbHelper = new DBHelper(this);
        accessoryList = dbHelper.getAllAccessories();

        Log.d("DB_DEBUG", "Số lượng phụ kiện: " + accessoryList.size());

        filteredList = new ArrayList<>(accessoryList);
        adapter = new AccessoryAdapter(this, filteredList);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        btnAddAccessory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAccessoryActivity.class);
            startActivityForResult(intent, ADD_ACCESSORY_REQUEST_CODE);
        });

        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            searchAccessories(keyword);
        });
    }

    private void searchAccessories(String query) {
        List<Accessory> result = new ArrayList<>();
        if (query.isEmpty()) {
            result.addAll(accessoryList);
        } else {
            for (Accessory accessory : accessoryList) {
                if (accessory.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(accessory);
                }
            }
        }
        adapter.updateList(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_ACCESSORY_REQUEST_CODE || requestCode == EDIT_ACCESSORY_REQUEST_CODE) && resultCode == RESULT_OK) {
            loadAccessoryList();
            adapter.notifyDataSetChanged();
        }
    }

    private void loadAccessoryList() {
        accessoryList = dbHelper.getAllAccessories();
        filteredList.clear();
        filteredList.addAll(accessoryList);
        adapter.updateList(filteredList);
    }
}
