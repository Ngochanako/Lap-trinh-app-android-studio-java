package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.Category.Phone.PhoneBrandActivity;
import com.myapplication.CategoryAccessory.AccessoryTypeActivity;

public class ActivityCategory extends AppCompatActivity {
    LinearLayout btnPhoneCategory, btnAccessoryCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btnPhoneCategory = findViewById(R.id.btnPhoneCategory);
        btnAccessoryCategory = findViewById(R.id.btnAccessoryCategory);

        btnPhoneCategory.setOnClickListener(view -> {
            startActivity(new Intent(ActivityCategory.this, PhoneBrandActivity.class));
        });

        btnAccessoryCategory.setOnClickListener(view -> {
            startActivity(new Intent(ActivityCategory.this, AccessoryTypeActivity.class));
        });
    }
}
