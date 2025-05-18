package com.myapplication.Phone;

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

import com.myapplication.DBHelper;
import com.myapplication.Modal.DienThoai;
import com.myapplication.R;

import java.io.IOException;

public class AddPhoneActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Request code chọn ảnh

    private EditText edtName, edtPrice, edtBrand;
    private ImageView imgPhone;
    private Button btnSave, btnCancel, btnSelectImage;
    private DBHelper dbHelper;

    private Uri selectedImageUri; // Lưu URI ảnh đã chọn

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtBrand = findViewById(R.id.edtBrand);
        imgPhone = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnChooseImage);
        dbHelper = new DBHelper(this);

        // Xử lý chọn ảnh
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // Xử lý lưu
        btnSave.setOnClickListener(v -> savePhone());

        // Xử lý hủy
        btnCancel.setOnClickListener(v -> finish());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData(); // Lưu URI của ảnh đã chọn

            try {
                // Hiển thị ảnh trên ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imgPhone.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePhone() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || brand.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tên điện thoại đã tồn tại chưa
        if (dbHelper.isPhoneNameExists(name)) {
            Toast.makeText(this, "Tên điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        int newId = (int) System.currentTimeMillis(); // Tạo ID duy nhất

        // Kiểm tra xem người dùng có chọn ảnh không
        String imageUriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";

        // Tạo đối tượng điện thoại
        DienThoai newPhone = new DienThoai(newId, name, price, brand, imageUriString);

        // Lưu vào database
        dbHelper.insertPhone(newPhone);

        // Gửi kết quả về PhoneListActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", newId);
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("price", price);
        resultIntent.putExtra("brand", brand);
        resultIntent.putExtra("imageUri", imageUriString);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
