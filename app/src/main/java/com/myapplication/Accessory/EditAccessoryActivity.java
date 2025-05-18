package com.myapplication.Accessory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.DBHelper;
import com.myapplication.Modal.Accessory;
import com.myapplication.R;

public class EditAccessoryActivity extends AppCompatActivity {

    private EditText edtName, edtPrice, edtDescription, edtCompatibleBrand;
    private ImageView imgPreview;
    private Button btnUpdate, btnCancel;
    private int accessoryId;
    private DBHelper dbHelper;
    private String imageName;
    private Spinner spinnerAccessoryType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accessory); // XML bạn đã tạo

        dbHelper = new DBHelper(this);
        // Dữ liệu mẫu cho Spinner
        String[] types = {"Ốp lưng", "Tai nghe", "Sạc", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerAccessoryType.setAdapter(adapter);
        // Khởi tạo các View
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtCompatibleBrand = findViewById(R.id.edtCompatibleBrand);
        imgPreview = findViewById(R.id.imgPreview);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        spinnerAccessoryType = findViewById(R.id.spinnerAccessoryType);
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        accessoryId = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String description = intent.getStringExtra("description");
        String compatibleBrand = intent.getStringExtra("compatibleBrand");
        imageName = intent.getStringExtra("image");

        // Gán dữ liệu lên form
        edtName.setText(name);
        edtPrice.setText(String.valueOf((long) price));
        edtDescription.setText(description);
        edtCompatibleBrand.setText(compatibleBrand);

        // Load ảnh nếu có
        Log.d("IMAGE_NAME", "Received image name: " + imageName);
        int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imgPreview.setImageResource(imageResId);

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
                Log.d("UPDATED_ACCESSORY", "Image: " + updatedAccessory.getImageResId());

                setResult(RESULT_OK); // Trả về kết quả thành công
                finish(); // Kết thúc Activity
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá không hợp lệ. Vui lòng chỉ nhập số.", Toast.LENGTH_SHORT).show();
            }
        });

        // Hủy bỏ thao tác và quay lại
        btnCancel.setOnClickListener(v -> finish());
    }
}
