package com.myapplication.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myapplication.DBHelper;
import com.myapplication.Modal.Laptop;
import com.myapplication.R;

import java.util.List;

public class Adapter_Laptop extends ArrayAdapter {
    private Activity activity;
    private int rsc;
    private List<Laptop> list;
    DBHelper dbHelper;

    public Adapter_Laptop(@NonNull Activity activity, int resource, @NonNull List objects) {
        super(activity, resource, objects);
        this.activity=activity;
        this.rsc=resource;
        this.list=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = this.activity.getLayoutInflater().inflate(this.rsc,null);
        TextView ma = item.findViewById(R.id.txtMa);
        TextView ten = item.findViewById(R.id.txtTen);
        TextView loai = item.findViewById(R.id.txtLoai);
        ImageView img = item.findViewById(R.id.img);

        Laptop lp = list.get(position);
        ma.setText(lp.getMa());
        ten.setText(lp.getTen());
        dbHelper = new DBHelper(this.getContext());


        loai.setText(dbHelper.getTenLoai(lp.getLoai()));

        byte[] hinhAnh = lp.getAnh();
        Bitmap bitmap  = BitmapFactory.decodeByteArray(hinhAnh,0,hinhAnh.length);
        img.setImageBitmap(bitmap);

        return item;
    }
}
