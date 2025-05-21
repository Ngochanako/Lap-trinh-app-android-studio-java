package com.myapplication.Accessory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.Accessory;
import com.myapplication.R;

import java.util.List;

public class EditAccessoryActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtPrice, edtDescription, edtCompatibleBrand;
    private ImageView imgPreview;
    private Button btnUpdate, btnCancel, btnSelectImage;
    private Spinner spinnerAccessoryType;

    private int accessoryId;
    private DBHelper dbHelper;
    private String imageName;
    private Uri imageUri;

    private ArrayAdapter<String> spinnerAdapter;
    private List<String> typeList; // Danh sách loại phụ kiện từ DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accessory);

        dbHelper = new DBHelper(this);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtCompatibleBrand = findViewById(R.id.edtCompatibleBrand);
        imgPreview = findViewById(R.id.imgPreview);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnChooseImage);
        spinnerAccessoryType = findViewById(R.id.spinnerAccessoryType);

        // Lấy loại phụ kiện từ DB
        typeList = dbHelper.getAllAccessoryTypes(); // Hàm bạn phải định nghĩa trong DBHelper

        // Thiết lập adapter cho Spinner
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        spinnerAccessoryType.setAdapter(spinnerAdapter);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        accessoryId = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String description = intent.getStringExtra("description");
        String compatibleBrand = intent.getStringExtra("brand");
        imageName = intent.getStringExtra("image");
        String type = intent.getStringExtra("type");

        // Gán dữ liệu lên form
        edtName.setText(name);
        edtPrice.setText(String.valueOf((long) price));
        edtDescription.setText(description);
        edtCompatibleBrand.setText(compatibleBrand);

        // Load ảnh
        imageUri = Uri.parse(imageName);
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.buds_samsung)
                .error(R.drawable.buds_samsung)
                .into(imgPreview);

        // Chọn đúng vị trí loại phụ kiện
        if (type != null && typeList != null) {
            int position = typeList.indexOf(type);
            if (position >= 0) {
                spinnerAccessoryType.setSelection(position);
            }
        }

        // Chọn ảnh mới
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // Cập nhật
        btnUpdate.setOnClickListener(v -> {
            try {
                String newName = edtName.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim().replace(",", "");
                String newDescription = edtDescription.getText().toString().trim();
                String newCompatibleBrand = edtCompatibleBrand.getText().toString().trim();
                String newType = spinnerAccessoryType.getSelectedItem().toString();

                if (newName.isEmpty() || priceStr.isEmpty() || newDescription.isEmpty() || newCompatibleBrand.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newPrice = Double.parseDouble(priceStr);
                Accessory updatedAccessory = new Accessory(accessoryId, newName, imageName, newDescription, newCompatibleBrand, newType, newPrice);
                dbHelper.updateAccessory(updatedAccessory);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", accessoryId);
                resultIntent.putExtra("name", newName);
                resultIntent.putExtra("price", newPrice);
                resultIntent.putExtra("brand", newCompatibleBrand);
                resultIntent.putExtra("imageUri", imageName);
                resultIntent.putExtra("description", newDescription);
                resultIntent.putExtra("type", newType);

                setResult(RESULT_OK);
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ. Vui lòng chỉ nhập số.", Toast.LENGTH_SHORT).show();
            }
        });

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
            imageName = imageUri.toString();
            Glide.with(this).load(imageUri).into(imgPreview);
        }
    }
}
