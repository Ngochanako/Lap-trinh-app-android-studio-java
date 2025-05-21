package com.myapplication.Accessory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.Accessory;
import com.myapplication.R;

import java.io.IOException;
import java.util.List;

public class AddAccessoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Spinner spinnerAccessoryType;
    private EditText edtName, edtPrice, edtBrand, edtDescription;
    private ImageView imgAccessory;
    private Button btnSave, btnCancel, btnChooseImage;
    private Uri selectedImageUri;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accessory);
        spinnerAccessoryType = findViewById(R.id.spinnerAccessoryType);
        dbHelper = new DBHelper(this);
        loadAccessoryTypes();
        // Ánh xạ view
        edtName = findViewById(R.id.edtAccessoryName);
        edtPrice = findViewById(R.id.edtAccessoryPrice);
        edtBrand = findViewById(R.id.edtAccessoryBrand);
        edtDescription = findViewById(R.id.edtAccessoryDescription);
        imgAccessory = findViewById(R.id.imgAccessoryPreview);
        btnSave = findViewById(R.id.btnSaveAccessory);
        btnCancel = findViewById(R.id.btnCancelAccessory);
        btnChooseImage = findViewById(R.id.btnChooseAccessoryImage);
        spinnerAccessoryType = findViewById(R.id.spinnerAccessoryType);
        btnChooseImage.setOnClickListener(v -> openImagePicker());
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveAccessory());
    }
    private void loadAccessoryTypes() {
        List<String> types = dbHelper.getAllAccessoryTypes();
        if (types.isEmpty()) {
            types.add("Chưa có loại phụ kiện nào");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccessoryType.setAdapter(adapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imgAccessory);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imgAccessory.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi hiển thị ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveAccessory() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String type = spinnerAccessoryType.getSelectedItem().toString();
        if (name.isEmpty() || priceStr.isEmpty() || brand.isEmpty() || description.isEmpty() || type.equals("Chưa có loại phụ kiện nào")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isAccessoryNameExists(name)) {
            Toast.makeText(this, "Tên phụ kiện đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageUri = (selectedImageUri != null) ? selectedImageUri.toString() : "";
        int newId = (int) System.currentTimeMillis(); // Tạo ID duy nhất

        Accessory accessory = new Accessory(newId, name, imageUri, description, brand,type, price);
        dbHelper.insertAccessory(accessory);
        // Trả kết quả về Activity trước
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", newId);
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("price", price);
        resultIntent.putExtra("brand", brand);
        resultIntent.putExtra("imageUri", imageUri);
        resultIntent.putExtra("description", description);
        resultIntent.putExtra("type", type);

        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Thêm phụ kiện thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
