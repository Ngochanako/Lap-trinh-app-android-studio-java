package com.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.myapplication.DBHelper;
import com.myapplication.R;

import java.util.ArrayList;

public class AccessoryTypeAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<String> accessoryTypes;
    private LayoutInflater inflater;
    private DBHelper dbHelper;

    public AccessoryTypeAdapter(Context context, ArrayList<String> accessoryTypes, DBHelper dbHelper) {
        this.context = context;
        this.accessoryTypes = accessoryTypes;
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return accessoryTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return accessoryTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvAccessoryName;
        ImageButton btnEdit, btnDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_accessory_brand, parent, false);
            holder = new ViewHolder();
            holder.tvAccessoryName = convertView.findViewById(R.id.tvAccessoryName);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = accessoryTypes.get(position);
        holder.tvAccessoryName.setText(item);

        // Sửa
        holder.btnEdit.setOnClickListener(v -> showEditDialog(position));

        // Xoá
        holder.btnDelete.setOnClickListener(v -> {
            int id = Integer.parseInt(item.split("\\.")[0]); // Lấy ID từ "1. Tai nghe"
            dbHelper.deleteAccessoryType(id);
            accessoryTypes.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }

    private void showEditDialog(int position) {
        String item = accessoryTypes.get(position);
        int id = Integer.parseInt(item.split("\\.")[0]);
        String currentName = item.substring(item.indexOf(" ") + 1);

        EditText input = new EditText(context);
        input.setText(currentName);

        new AlertDialog.Builder(context)
                .setTitle("Sửa loại phụ kiện")
                .setView(input)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        dbHelper.updateAccessoryType(id, newName);
                        accessoryTypes.set(position, id + ". " + newName);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    public void updateData(ArrayList<String> newList) {
        this.accessoryTypes = newList;
        notifyDataSetChanged();
    }
}

