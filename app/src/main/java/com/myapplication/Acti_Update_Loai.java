package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapplication.Modal.Laptop;
import com.myapplication.Modal.LoaiLaptop;

public class Acti_Update_Loai extends AppCompatActivity {

    DBHelper dbHelper;
    LoaiLaptop loaiLaptop;
    EditText edtMaLoai , edtTenLoai;
    Button btnLuuLoai;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_update_loai);

        dbHelper = new DBHelper(this);

        addControls();
        addEvents();

        if(getIntent().hasExtra("loai")){
            loaiLaptop = (LoaiLaptop) getIntent().getSerializableExtra("loai");
           edtMaLoai.setText(loaiLaptop.getMaloai());
           edtTenLoai.setText(loaiLaptop.getTenloai());

        }else loaiLaptop = new LoaiLaptop();
    }

    private void addEvents() {
        btnLuuLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaiLaptop.setMaloai(edtMaLoai.getText().toString());
                loaiLaptop.setTenloai(edtTenLoai.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("loai",loaiLaptop);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.itemabout) {
                    Intent intent = new Intent(Acti_Update_Loai.this, About.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.itemexit) {
                    Toast.makeText(Acti_Update_Loai.this, "Exit", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });
    }

    private void addControls() {
        edtMaLoai = findViewById(R.id.edtMaLoai);
        edtTenLoai = findViewById(R.id.edtTenLoai);
        btnLuuLoai = findViewById(R.id.btnLuuLoai);
        toolbar=findViewById(R.id.toolbar);

    }

}