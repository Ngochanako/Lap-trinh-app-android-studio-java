package com.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.Modal.User;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Button btnDN ;
    EditText edttk ,edtmk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
//        User user = new User("admin","admin");
//        dbHelper.insertUser(user);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbHelper.checkUser(edttk.getText().toString(),edtmk.getText().toString())){
                    Intent intent = new Intent(MainActivity.this , QuanLyActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void addControls() {
        btnDN = findViewById(R.id.btnDangNhap);
        edttk=findViewById(R.id.edtTK);
        edtmk=findViewById(R.id.edtMK);

    }
}