package com.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class About extends AppCompatActivity {
    Toolbar toolbar;
    Button btnPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar=findViewById(R.id.toolbar);
        btnPhone = findViewById(R.id.btnPhone);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.itemabout) {

                    return true;
                } else if (itemId == R.id.itemexit) {
                    Toast.makeText(About.this, "Exit", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
               intent.setData(Uri.parse("tel:"+btnPhone.getText().toString()));
                    startActivity(intent);
            }
        });



    }
}