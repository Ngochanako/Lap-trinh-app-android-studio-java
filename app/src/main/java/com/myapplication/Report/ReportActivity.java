package com.myapplication.Report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapplication.DBHelper;
import com.myapplication.R;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TableLayout tableAccessories, tableTop5Brands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHelper = new DBHelper(this);

        List<String> accessoriesGroupedByBrand = dbHelper.getAccessoryTypesGroupedByBrand();
        List<String> top5Brands = dbHelper.getTop5AccessoryBrands();

        tableAccessories = findViewById(R.id.tableAccessories);
        tableTop5Brands = findViewById(R.id.tableTop5Brands);

        // Phụ kiện theo hãng
        for (String line : accessoriesGroupedByBrand) {
            String[] parts = line.split(": ");
            String brand = parts[0];
            String accessories = parts.length > 1 ? parts[1] : "";

            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.table_row_accessories, null);
            TextView brandTextView = row.findViewById(R.id.brandTextView);
            TextView accessoriesTextView = row.findViewById(R.id.accessoriesTextView);

            brandTextView.setText(brand);
            accessoriesTextView.setText(accessories);

            tableAccessories.addView(row);
        }

        // Top 5 hãng nhiều phụ kiện
        for (String line : top5Brands) {
            String[] parts = line.split(": ");
            String brand = parts[0];
            String count = parts.length > 1 ? parts[1] : "";

            TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.table_row_top5, null);
            TextView brandTextView = row.findViewById(R.id.brandTextView);
            TextView countTextView = row.findViewById(R.id.countTextView);

            brandTextView.setText(brand);
            countTextView.setText(count);

            tableTop5Brands.addView(row);
        }
    }

}
