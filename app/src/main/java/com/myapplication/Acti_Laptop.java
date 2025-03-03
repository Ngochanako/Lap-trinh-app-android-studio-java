package com.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.myapplication.Adapter.Adapter_Laptop;
import com.myapplication.Modal.Laptop;

import java.util.ArrayList;

public class Acti_Laptop extends AppCompatActivity {

    DBHelper dbHelper;
    ListView listView;
    ArrayList<Laptop> list;
    Adapter_Laptop adapterLaptop;
    FloatingActionButton btnThem;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_laptop);

        dbHelper = new DBHelper(this);


        addControls();
        addEvents();

//        Laptop lp1 =new Laptop("acer213","Laptop acer",new byte[]{12},"lpvp", 1.2F,10);
//
//        if(dbHelper.insertLaptop(lp1)>0){
//            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "Thêm Thất bại !", Toast.LENGTH_LONG).show();
//        }


        loadLaptop();

    }



    private void loadLaptop() {
        list= dbHelper.getAllLaptop();
        adapterLaptop = new Adapter_Laptop(this, R.layout.laptop_listview,list);
        listView.setAdapter(adapterLaptop);
        adapterLaptop.notifyDataSetChanged();
    }

    private void addEvents() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acti_Laptop.this, Acti_Update_Laptop.class);
                startActivityForResult(intent,1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Laptop laptop = list.get(position);
                Intent intent = new Intent(Acti_Laptop.this,Acti_Update_Laptop.class);
                intent.putExtra("laptop",laptop);
                startActivityForResult(intent,2);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Laptop laptop=list.get(position);

                new AlertDialog.Builder(Acti_Laptop.this).setTitle("Xóa lựa chọn").setMessage("Bạn có chắc muốn xóa mục này ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteLaptop(laptop.getMa());
                        loadLaptop();
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
                    Intent intent = new Intent(Acti_Laptop.this, About.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.itemexit) {
                    Toast.makeText(Acti_Laptop.this, "Exit", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return true;
                }
                return false;
            }
        });
    }

    private void addControls() {
        listView = findViewById(R.id.lvLaptop);
        btnThem=findViewById(R.id.btnThem);
        toolbar =findViewById(R.id.toolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            Laptop lp = (Laptop) data.getSerializableExtra("laptop");
            dbHelper.insertLaptop(lp);
            loadLaptop();
        } else if (requestCode==2 && resultCode==RESULT_OK && data!=null) {
            Laptop lp = (Laptop) data.getSerializableExtra("laptop");
            dbHelper.updateLaptop(lp);
            loadLaptop();
        }
    }

}