package com.ecoscan.app.ui.product;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EcoScanDatabase db = EcoScanDatabase.getInstance(this);
        PantryItem item = getIntent().getParcelableExtra("item");

        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvBarcode = findViewById(R.id.tv_detail_barcode);
        TextView tvDateAdded = findViewById(R.id.tv_date_added);
        TextView tvExpiryDate = findViewById(R.id.tv_expiry_date);

        if (item == null) {
            Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName.setText(item.itemName);
        tvBarcode.setText(item.barcode);
        tvDateAdded.setText(Math.toIntExact(item.dateAdded));
        tvExpiryDate.setText(Math.toIntExact(item.expiryDate));

        // Delete button
        MaterialButton btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            db.pantryDao().delete(item);
            Toast.makeText(this, "Product removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}