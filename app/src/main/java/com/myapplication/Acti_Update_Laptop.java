package com.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.myapplication.Adapter.Adapter_Spinner;
import com.myapplication.Modal.Laptop;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Acti_Update_Laptop extends AppCompatActivity {

    Spinner spinner;
    DBHelper dbHelper;
    Adapter_Spinner adapter;
    Laptop laptop;
    EditText edtma , edtten,edtkg , edtgia;
    ImageView anh;
    Button btnLuu , btnLoaiLaptop;
    ImageButton btnFolder;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_laptop);

        dbHelper =new DBHelper(this);

        addControls();
        addEvents();
        loadSpinner();

        if(getIntent().hasExtra("laptop")){
             laptop = (Laptop) getIntent().getSerializableExtra("laptop");
             edtma.setText(laptop.getMa());
             edtten.setText(laptop.getTen());
             edtgia.setText(laptop.getGia()+"");
             edtkg.setText(laptop.getKg()+"");
             Bitmap bitmap = BitmapFactory.decodeByteArray(laptop.getAnh(), 0,laptop.getAnh().length);
             anh.setImageBitmap(bitmap);
        }else laptop = new Laptop();
    }

    private void loadSpinner() {
        ArrayList<String> list=dbHelper.getAllNameLoai();
        adapter = new Adapter_Spinner(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void addEvents() {
        btnFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                laptop.setLoai(dbHelper.getMaLoai(selected));
                //Toast.makeText(Acti_Update_Laptop.this, "selected"+dbHelper.getMaLoai(selected), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laptop.setMa(edtma.getText().toString());
                laptop.setTen(edtten.getText().toString());
                byte[] img=getImageBytesFromImageView(anh);
                laptop.setAnh(img);
                laptop.setKg(Float.parseFloat(edtkg.getText().toString()));
                laptop.setGia(Integer.parseInt(edtgia.getText().toString()));
               // laptop.setLoai();

                Intent intent = new Intent();
                intent.putExtra("laptop",laptop);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        btnLoaiLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acti_Update_Laptop.this ,Acti_Loai.class);
                startActivity(intent);

            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.itemabout) {
                    Intent intent = new Intent(Acti_Update_Laptop.this, About.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.itemexit) {
                    Toast.makeText(Acti_Update_Laptop.this, "Exit", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });

    }

    private void addControls() {
        btnFolder = findViewById(R.id.btnFolder);
        btnLoaiLaptop =findViewById(R.id.btnLoaiLaptop);
        btnLuu=findViewById(R.id.btnLuuLaptop);
        spinner=findViewById(R.id.spinner);
        edtma=findViewById(R.id.edtMaLaptop);
        edtten=findViewById(R.id.edtTenLaptop);
        edtkg=findViewById(R.id.edtKg);
        edtgia=findViewById(R.id.edtGia);
        anh=findViewById(R.id.anh);
        toolbar =findViewById(R.id.toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==10 && resultCode==RESULT_OK && data!=null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap =BitmapFactory.decodeStream(inputStream);
                anh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private byte[] getImageBytesFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSpinner();
    }
}