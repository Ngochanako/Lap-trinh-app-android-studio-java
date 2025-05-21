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

public class PhoneBrandAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<String> phoneBrands;
    private LayoutInflater inflater;
    private DBHelper dbHelper;

    public PhoneBrandAdapter(Context context, ArrayList<String> phoneBrands) {
        this.context = context;
        this.phoneBrands = phoneBrands;
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public int getCount() {
        return phoneBrands.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneBrands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvBrandName;
        ImageButton btnEdit, btnDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_phone_brand, parent, false);
            holder = new ViewHolder();
            holder.tvBrandName = convertView.findViewById(R.id.tvBrandName);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = phoneBrands.get(position);
        holder.tvBrandName.setText(item);

        // SỬA
        holder.btnEdit.setOnClickListener(v -> showEditDialog(position));

        // XOÁ
        holder.btnDelete.setOnClickListener(v -> {
            int id = Integer.parseInt(item.split("\\.")[0]); // lấy ID từ "1. Samsung"
            dbHelper.deletePhoneBrand(id);
            phoneBrands.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }

    private void showEditDialog(int position) {
        String item = phoneBrands.get(position);
        int id = Integer.parseInt(item.split("\\.")[0]);

        EditText input = new EditText(context);
        input.setText(item.substring(item.indexOf(" ") + 1));

        new AlertDialog.Builder(context)
                .setTitle("Sửa tên hãng")
                .setView(input)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        dbHelper.updatePhoneBrand(id, newName);
                        phoneBrands.set(position, id + ". " + newName);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    public void updateData(ArrayList<String> newList) {
        this.phoneBrands = newList;
        notifyDataSetChanged();
    }
}
