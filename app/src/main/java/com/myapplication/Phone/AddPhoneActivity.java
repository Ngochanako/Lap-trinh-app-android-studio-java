package com.myapplication.Phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.DienThoai;
import com.myapplication.R;

import java.io.IOException;
import java.util.List;

public class AddPhoneActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtPrice;
    private Spinner spinnerBrand;
    private ImageView imgPhone;
    private Button btnSave, btnCancel, btnSelectImage;
    private DBHelper dbHelper;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        spinnerBrand = findViewById(R.id.spinnerBrand);
        imgPhone = findViewById(R.id.imgPreview);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnChooseImage);
        dbHelper = new DBHelper(this);

        loadBrands();

        btnSelectImage.setOnClickListener(v -> openImagePicker());

        btnSave.setOnClickListener(v -> savePhone());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadBrands() {
        List<String> brands = dbHelper.getAllBrands();
        if (brands.isEmpty()) {
            brands.add("Chưa có thương hiệu nào");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, brands);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(adapter);
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
                    .into(imgPhone);
            try {
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
        String brand = spinnerBrand.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty() || brand.isEmpty() || brand.equals("Chưa có thương hiệu nào")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isPhoneNameExists(name)) {
            Toast.makeText(this, "Tên điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        int newId = (int) System.currentTimeMillis();

        String imageUriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";

        DienThoai newPhone = new DienThoai(newId, name, price, brand, imageUriString);

        dbHelper.insertPhone(newPhone);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", newId);
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("price", price);
        resultIntent.putExtra("brand", brand);
        resultIntent.putExtra("imageUri", imageUriString);

        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Thêm điện thoại thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
