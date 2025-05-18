package com.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.DienThoai;
import com.myapplication.Phone.EditPhoneActivity;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    private List<DienThoai> phoneList;
    private Context context;

    public PhoneAdapter(Context context, List<DienThoai> phoneList) {
        this.context = context;
        this.phoneList = new ArrayList<>(phoneList); // Sao chép danh sách để tránh thay đổi ngoài ý muốn
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DienThoai phone = phoneList.get(position);
        holder.txtName.setText(phone.getName());
        holder.txtPrice.setText(String.format("%,.0f VND", phone.getPrice()));
        holder.txtBrand.setText(phone.getBrand());

        // Kiểm tra nếu ảnh tồn tại thì hiển thị
        String imageUriStr = phone.getImageUri();

        if (imageUriStr != null && !imageUriStr.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriStr);
            Log.d("abc",imageUriStr);
            Glide.with(context)  // context ở đây là từ Adapter
                    .load(imageUri)
                    .placeholder(R.drawable.iphone_14_pro)
                    .error(R.drawable.iphone_14_pro)
                    .into(holder.imgPhone);
        } else {
            holder.imgPhone.setImageResource(R.drawable.iphone_14_pro);
        }
        // chuyển màn hình edit phone
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPhoneActivity.class);
            intent.putExtra("id", phone.getId());
            intent.putExtra("name", phone.getName());
            intent.putExtra("price", phone.getPrice());
            intent.putExtra("brand", phone.getBrand());
            intent.putExtra("image", phone.getImageUri());
            ((Activity) context).startActivityForResult(intent, 1002); // REQUEST_CODE_EDIT
        });
        // chuyển màn hình delete
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này không?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.deletePhone(phone.getId()); // Xoá khỏi DB
                        removeItem(holder.getAdapterPosition()); // Xoá khỏi danh sách
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    // Cập nhật danh sách khi tìm kiếm hoặc sửa dữ liệu
    public void updateList(List<DienThoai> newList) {
        this.phoneList.clear();
        this.phoneList.addAll(newList);
        notifyDataSetChanged();
    }

    // Xoá một mục khỏi danh sách
    public void removeItem(int position) {
        if (position >= 0 && position < phoneList.size()) {
            phoneList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Sửa một mục trong danh sách
    public void editItem(int position, DienThoai updatedPhone) {
        if (position >= 0 && position < phoneList.size()) {
            phoneList.set(position, updatedPhone);
            notifyItemChanged(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtBrand;
        ImageView imgPhone;
        ImageButton btnEdit,btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtBrand = itemView.findViewById(R.id.txtBrand);
            imgPhone = itemView.findViewById(R.id.imgPhone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

    }
}
