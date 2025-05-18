package com.myapplication.Phone;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.myapplication.DBHelper;
import com.myapplication.Modal.DienThoai;
import com.myapplication.R;

public class EditPhoneActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1; // Request code chọn ảnh
    private EditText edtName, edtPrice, edtBrand;
    private ImageView imgPreview;
    private Button btnUpdate, btnCancel,btnSelectImage;
    private int phoneId;
    private DBHelper dbHelper;
    private String imageName;
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone); // XML đã tạo

        dbHelper = new DBHelper(this);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtBrand = findViewById(R.id.edtBrand);
        imgPreview = findViewById(R.id.imgPreview);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnChooseImage);


        // Nhận dữ liệu
        Intent intent = getIntent();
        phoneId = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String brand = intent.getStringExtra("brand");
        imageName = intent.getStringExtra("image");
        // Load ảnh nếu có
        Log.d("IMAGE_NAME", "Received image name: " + imageName);
        imageUri = Uri.parse(imageName);
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.iphone_14_pro) // Ảnh mặc định
                .error(R.drawable.iphone_14_pro) // Nếu load fail
                .into(imgPreview);

        // Xử lý chọn ảnh
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        // Gán dữ liệu lên form
        edtName.setText(name);
        edtPrice.setText(String.valueOf((long) price));
        edtBrand.setText(brand);
        btnUpdate.setOnClickListener(v -> {
            Log.d("DEBUG", "Update button clicked");
            try {
                String newName = edtName.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim().replace(",", "");
                String newBrand = edtBrand.getText().toString().trim();

                // Kiểm tra không để trống
                if (newName.isEmpty() || priceStr.isEmpty() || newBrand.isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double newPrice = Double.parseDouble(priceStr);
                DienThoai updatedPhone = new DienThoai(phoneId, newName, newPrice, newBrand, imageName);
                dbHelper.updatePhone(updatedPhone);
                // Ghi log các thông tin của updatedPhone
                Log.d("UPDATED_PHONE", "Phone ID: " + updatedPhone.getId());
                Log.d("UPDATED_PHONE", "Name: " + updatedPhone.getName());
                Log.d("UPDATED_PHONE", "Price: " + updatedPhone.getPrice());
                Log.d("UPDATED_PHONE", "Brand: " + updatedPhone.getBrand());
                Log.d("UPDATED_PHONE", "Image: " + updatedPhone.getImageUri());
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
            imageName = imageUri.toString(); // Cập nhật đường dẫn ảnh mới
            Glide.with(this).load(imageUri).into(imgPreview); // Hiển thị ảnh vừa chọn
        }
    }
}
