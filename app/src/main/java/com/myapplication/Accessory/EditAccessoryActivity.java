package com.myapplication.Accessory;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.Accessory;
import com.myapplication.R;

public class EditAccessoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Request code chọn ảnh
    private EditText edtName, edtPrice, edtDescription, edtCompatibleBrand;
    private ImageView imgPreview;
    private Button btnUpdate, btnCancel,btnSelectImage;
    private int accessoryId;
    private DBHelper dbHelper;
    private String imageName;
    private Spinner spinnerAccessoryType;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accessory); // XML bạn đã tạo

        dbHelper = new DBHelper(this);
        // Khởi tạo các View
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtCompatibleBrand = findViewById(R.id.edtCompatibleBrand);
        imgPreview = findViewById(R.id.imgPreview);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnChooseImage);
        spinnerAccessoryType = findViewById(R.id.spinnerAccessoryType);
        // Dữ liệu mẫu cho Spinner
        String[] types = {"Ốp lưng", "Tai nghe", "Sạc", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerAccessoryType.setAdapter(adapter);
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        accessoryId = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String description = intent.getStringExtra("description");
        String compatibleBrand = intent.getStringExtra("brand");
        imageName = intent.getStringExtra("image");
        String type = intent.getStringExtra("type");
        Log.d("TYPE_RECEIVED", "Type: " + type);
        Log.d("TYPE_RECEIVED", "Brand: " + compatibleBrand);
        // Load ảnh nếu có
        Log.d("abc",imageName.toString());
        imageUri = Uri.parse(imageName);
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.buds_samsung) // Ảnh mặc định
                .error(R.drawable.buds_samsung) // Nếu load fail
                .into(imgPreview);
        // Xử lý chọn ảnh
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        // Gán dữ liệu lên form
        if (type != null) {
            int position = adapter.getPosition(type);
            if (position >= 0) {
                spinnerAccessoryType.setSelection(position);
            }
        }
        // Gán dữ liệu lên form
        edtName.setText(name);
        edtPrice.setText(String.valueOf((long) price));
        edtDescription.setText(description);
        edtCompatibleBrand.setText(compatibleBrand);
        // Cập nhật thông tin
        btnUpdate.setOnClickListener(v -> {
            Log.d("DEBUG", "Update button clicked");
            try {
                String newName = edtName.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim().replace(",", "");
                String newDescription = edtDescription.getText().toString().trim();
                String newCompatibleBrand = edtCompatibleBrand.getText().toString().trim();
                String newType = spinnerAccessoryType.getSelectedItem().toString();
                // Kiểm tra không để trống
                if (newName.isEmpty() || priceStr.isEmpty() || newDescription.isEmpty() || newCompatibleBrand.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newPrice = Double.parseDouble(priceStr);
                Accessory updatedAccessory = new Accessory(accessoryId, newName, imageName, newDescription, newCompatibleBrand,newType, newPrice);
                dbHelper.updateAccessory(updatedAccessory); // Cập nhật dữ liệu trong DB
                // Ghi log các thông tin của updatedAccessory
                Log.d("UPDATED_ACCESSORY", "Accessory ID: " + updatedAccessory.getId());
                Log.d("UPDATED_ACCESSORY", "Name: " + updatedAccessory.getName());
                Log.d("UPDATED_ACCESSORY", "Price: " + updatedAccessory.getPrice());
                Log.d("UPDATED_ACCESSORY", "Description: " + updatedAccessory.getDescription());
                Log.d("UPDATED_ACCESSORY", "Compatible Brand: " + updatedAccessory.getCompatibleBrand());
                Log.d("UPDATED_ACCESSORY", "Image: " + updatedAccessory.getImageUri());

                // Trả kết quả về Adapter
                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", accessoryId);
                resultIntent.putExtra("name", newName);
                resultIntent.putExtra("price", newPrice);
                resultIntent.putExtra("brand", newCompatibleBrand);
                resultIntent.putExtra("imageUri", imageName);
                resultIntent.putExtra("description", newDescription);
                resultIntent.putExtra("type", newType);

                setResult(RESULT_OK); // Trả về kết quả thành công
                finish(); // Kết thúc Activity
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ. Vui lòng chỉ nhập số.", Toast.LENGTH_SHORT).show();
            }
        });

        // Hủy bỏ thao tác và quay lại
        btnCancel.setOnClickListener(v -> finish());
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageName = imageUri.toString(); // Cập nhật đường dẫn ảnh mới
            Glide.with(this).load(imageUri).into(imgPreview); // Hiển thị ảnh vừa chọn
        }
    }
}
