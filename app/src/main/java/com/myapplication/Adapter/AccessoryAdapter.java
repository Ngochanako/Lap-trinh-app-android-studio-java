package com.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.myapplication.Accessory.EditAccessoryActivity;
import com.myapplication.Modal.Accessory;
import com.myapplication.DBHelper;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AccessoryAdapter extends RecyclerView.Adapter<AccessoryAdapter.ViewHolder> {
    private List<Accessory> accessoryList;
    private Context context;

    public AccessoryAdapter(Context context, List<Accessory> accessoryList) {
        this.context = context;
        this.accessoryList = new ArrayList<>(accessoryList); // Sao chép danh sách để tránh thay đổi ngoài ý muốn
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accessory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Accessory accessory = accessoryList.get(position);
        holder.txtName.setText(accessory.getName());
        holder.txtPrice.setText(String.format("%,.0f VND", accessory.getPrice()));
        holder.txtBrand.setText(accessory.getCompatibleBrand());
        holder.txtDescription.setText(accessory.getDescription());


        // Chuyển chuỗi ảnh thành ID trong drawable
        int imageId = context.getResources().getIdentifier(accessory.getImageResId(), "drawable", context.getPackageName());

        // Kiểm tra nếu ảnh tồn tại thì hiển thị
        if (imageId != 0) {
            holder.imgAccessory.setImageResource(imageId);
        } else {
            // Nếu không tìm thấy ảnh, đặt ảnh mặc định
            holder.imgAccessory.setImageResource(R.drawable.iphone_14_pro);
        }

        // Chuyển màn hình chỉnh sửa phụ kiện
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditAccessoryActivity.class);
            intent.putExtra("id", accessory.getId());
            intent.putExtra("name", accessory.getName());
            intent.putExtra("price", accessory.getPrice());
            intent.putExtra("brand", accessory.getCompatibleBrand());
            intent.putExtra("image", accessory.getImageResId());
            intent.putExtra("description", accessory.getDescription());
            ((Activity) context).startActivityForResult(intent, 1002); // REQUEST_CODE_EDIT
        });

        // Chuyển màn hình xoá phụ kiện
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá phụ kiện này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.deleteAccessory(accessory.getId()); // Xoá khỏi DB
                        removeItem(holder.getAdapterPosition()); // Xoá khỏi danh sách
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return accessoryList.size();
    }

    // Cập nhật danh sách khi tìm kiếm hoặc sửa dữ liệu
    public void updateList(List<Accessory> newList) {
        this.accessoryList.clear();
        this.accessoryList.addAll(newList);
        notifyDataSetChanged();
    }

    // Xoá một mục khỏi danh sách
    public void removeItem(int position) {
        if (position >= 0 && position < accessoryList.size()) {
            accessoryList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Sửa một mục trong danh sách
    public void editItem(int position, Accessory updatedAccessory) {
        if (position >= 0 && position < accessoryList.size()) {
            accessoryList.set(position, updatedAccessory);
            notifyItemChanged(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtBrand, txtDescription;
        ImageView imgAccessory;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtAccessoryName);
            txtPrice = itemView.findViewById(R.id.txtAccessoryPrice);
            txtBrand = itemView.findViewById(R.id.txtAccessoryBrand);
            txtDescription = itemView.findViewById(R.id.txtAccessoryDescription);
            imgAccessory = itemView.findViewById(R.id.imgAccessory);
            btnEdit = itemView.findViewById(R.id.btnEditAccessory);
            btnDelete = itemView.findViewById(R.id.btnDeleteAccessory);
        }
    }
}

