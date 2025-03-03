package com.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapplication.Adapter.Adapter_Loai;
import com.myapplication.Modal.Laptop;
import com.myapplication.Modal.LoaiLaptop;

import java.util.ArrayList;

public class Acti_Loai extends AppCompatActivity {
    ListView listView;
    Adapter_Loai adapterLoai;

    DBHelper dbHelper;

    ArrayList<LoaiLaptop> list;
    FloatingActionButton btnThemLoai;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_lap_top);
        dbHelper = new DBHelper(this);

        addControls();
        addEvents();

//        LoaiLaptop lp1 = new LoaiLaptop("lpdh","Đồ họa");
//        LoaiLaptop lp2 = new LoaiLaptop("lpgm","Gaming");
//        dbHelper.insertLoaiLaptop(lp1);
        //dbHelper.insertLoaiLaptop(lp2);
        loadLoai();
    }

    private void loadLoai() {
        list = dbHelper.getAllLoai();
        adapterLoai = new Adapter_Loai(this ,R.layout.loai_listview,list);
        listView.setAdapter(adapterLoai);
        adapterLoai.notifyDataSetChanged();

    }

    private void addEvents() {
        btnThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acti_Loai.this,Acti_Update_Loai.class);
                startActivityForResult(intent,5);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoaiLaptop loai = list.get(position);
                Intent intent = new Intent(Acti_Loai.this,Acti_Update_Loai.class);
                intent.putExtra("loai",loai);
                startActivityForResult(intent,6);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LoaiLaptop loai = list.get(position);
                new AlertDialog.Builder(Acti_Loai.this).setTitle("Xóa lựa chọn").setMessage("Bạn có chắc muốn xóa mục này ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dbHelper.deleteLoai(loai.getMaloai())==0){
                            Toast.makeText(Acti_Loai.this, "Không thể xóa!", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(Acti_Loai.this, "Xóa thành công! ", Toast.LENGTH_SHORT).show();
                        loadLoai();
                    }
                }).setNegativeButton("cancle",null).show();
                return false;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.itemabout) {
                    Intent intent = new Intent(Acti_Loai.this, About.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.itemexit) {
                    Toast.makeText(Acti_Loai.this, "Exit", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });
    }

    private void addControls() {
        listView = findViewById(R.id.lvLoai);
        btnThemLoai = findViewById(R.id.btnThemLoai);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5 && resultCode==RESULT_OK && data!=null){
            LoaiLaptop loai = (LoaiLaptop) data.getSerializableExtra("loai");
            dbHelper.insertLoaiLaptop(loai);
            loadLoai();
        } else if (requestCode==6 && resultCode==RESULT_OK && data!=null) {
            LoaiLaptop loai = (LoaiLaptop) data.getSerializableExtra("loai");
            dbHelper.updateLoai(loai);
            loadLoai();
        }
    }
}