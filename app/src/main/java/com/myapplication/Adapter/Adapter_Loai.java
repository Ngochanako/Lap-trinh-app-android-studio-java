package com.myapplication.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myapplication.Modal.LoaiLaptop;
import com.myapplication.R;

import java.util.List;

public class Adapter_Loai extends ArrayAdapter {
    private Activity activity;
    private int rsc;
    private List<LoaiLaptop> list;

    public Adapter_Loai(@NonNull Activity activity, int resource, @NonNull List objects) {
        super(activity, resource, objects);
        this.activity=activity;
        this.rsc=resource;
        this.list=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = this.activity.getLayoutInflater().inflate(this.rsc,null);
        TextView maloai = item.findViewById(R.id.txtMaLoai);
        TextView tenloai = item.findViewById(R.id.txtTenLoai);

        LoaiLaptop loaiLaptop = list.get(position);
        maloai.setText(loaiLaptop.getMaloai().toString());
        tenloai.setText(loaiLaptop.getTenloai().toString());

        return item;
    }
}
